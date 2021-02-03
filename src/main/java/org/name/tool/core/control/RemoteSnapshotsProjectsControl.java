package org.name.tool.core.control;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.util.FileUtils;
import org.name.tool.data.bean.Snapshot;
import org.name.tool.data.exports.ProjectMetricsResultsExporter;
import org.name.tool.data.imports.CSVSnapshotsImporter;
import org.name.tool.results.ProjectMetricsResults;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class RemoteSnapshotsProjectsControl extends ProjectsControl {
    private final Path remoteProjectsAbsolutePath;
    public static final String BASE_DIR = "/tmp";
    private final String exportFormat;

    public RemoteSnapshotsProjectsControl(String[] metricsCodes, Path remoteProjectsAbsolutePath, String exportFormat) {
        super(metricsCodes);
        this.remoteProjectsAbsolutePath = remoteProjectsAbsolutePath;
        this.exportFormat = exportFormat;
    }

    @Override
    public void run() {
        System.out.println("* Using " + remoteProjectsAbsolutePath + " file for cloning and analyzing repositories.");
        List<Snapshot> snapshots;
        try {
            snapshots = new CSVSnapshotsImporter().extractSnapshots(remoteProjectsAbsolutePath);
        } catch (IOException e) {
            System.out.println("* File " + remoteProjectsAbsolutePath + " cannot be read: quitting.");
            return;
        }
        Set<String> repositoryURIs = snapshots.stream()
                .map(Snapshot::getRepositoryURI)
                .collect(Collectors.toCollection(LinkedHashSet::new));
        for (String repositoryURI : repositoryURIs) {
            String projectName = Paths.get(repositoryURI).getFileName().toString();
            File destinationDir = Paths.get(BASE_DIR, projectName).toFile();

            // Delete destination directory (with all its content) if it already exists
            delete(destinationDir);
            System.out.println("* Cloning " + repositoryURI + " to " + destinationDir);
            try (Git git = Git.cloneRepository().setDirectory(destinationDir).setURI(repositoryURI).call()) {
                List<Snapshot> repoSnapshots = snapshots.stream()
                        .filter(sn -> sn.getRepositoryURI().equals(repositoryURI))
                        .collect(Collectors.toList());
                for (int i = 0; i < repoSnapshots.size(); i++) {
                    Snapshot repoSnapshot = repoSnapshots.get(i);
                    String commitSha = repoSnapshot.getCommitSha();
                    System.out.println("* [" + (i + 1) + "/" + repoSnapshots.size() + "] Checking out commit " + commitSha);
                    // Checkout commit
                    git.checkout().setName(commitSha).call();
                    // Analyze and compute metrics
                    ProjectMetricsResults projectMetricsResults = super.processProject(destinationDir.toPath());
                    // Export
                    ProjectMetricsResultsExporter projectMetricsResultsExporter = new ProjectMetricsResultsExporter(repoSnapshot, projectMetricsResults);
                    try {
                        projectMetricsResultsExporter.exportAs(exportFormat);
                    } catch (IOException e) {
                        System.out.println("* Could not export results: skipping.");
                    }
                }
            } catch (GitAPIException e) {
                System.out.println("* Failed to clone " + repositoryURI + ": skipping.");
                continue;
            }
            // Delete after finishing
            delete(destinationDir);
        }
    }

    private void delete(File dir) {
        try {
            FileUtils.delete(dir, FileUtils.RECURSIVE);
        } catch (IOException ignored) {
        }
    }
}
