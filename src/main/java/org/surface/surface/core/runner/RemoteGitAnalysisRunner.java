package org.surface.surface.core.runner;

import org.eclipse.jgit.util.FileUtils;
import org.surface.surface.core.filter.RevisionFilter;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class RemoteGitAnalysisRunner extends AnalysisRunner {
    private final File cloneDir;
    private final RevisionFilter revisionFilter;

    public RemoteGitAnalysisRunner(List<String> metrics, String target, File outFile, String filesRegex, File cloneDir, RevisionFilter revisionFilter) {
        super(metrics, target, outFile, filesRegex);
        this.cloneDir = cloneDir;
        this.revisionFilter = revisionFilter;
    }

    public File getCloneDir() {
        return cloneDir;
    }

    public RevisionFilter getRevisionFilter() {
        return revisionFilter;
    }

    @Override
    public void run() {
        /* TODO Legacy code: reimplement
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
                    String commitHash = repoSnapshot.getCommitHash();
                    System.out.println("\n* [" + (i + 1) + "/" + repoSnapshots.size() + "] Checking out commit " + commitHash);
                    // Checkout commit
                    try {
                        git.reset().setMode(ResetCommand.ResetType.HARD).call();
                        git.checkout().setName(commitHash).call();
                    } catch (GitAPIException e) {
                        System.out.println("* Cannot checkout to " + commitHash + ": skipping commit.");
                        e.printStackTrace();
                        continue;
                    }
                    try {
                        // Analyze and compute metrics
                        ProjectMetricsResults projectMetricsResults = super.analyzeProject(destinationDir.toPath());
                        // Export
                        RemoteProjectResultsExporter remoteProjectResultsExporter = new RemoteProjectResultsExporter(repoSnapshot, projectMetricsResults, getMetricsCodes());
                        try {
                            remoteProjectResultsExporter.export(getExportFormat(), getOutFile());
                        } catch (IOException e) {
                            System.out.println("* Could not export results: skipping commit.");
                            e.printStackTrace();
                        }
                    }
                    catch (RuntimeException e) {
                        System.out.println("* Filed analyzing this snapshot: skipping commit.");
                        e.printStackTrace();
                    }
                }
            } catch (GitAPIException e) {
                System.out.println("* Cannot clone " + repositoryURI + ": skipping repository.");
                continue;
            }
            // Delete after finishing
            delete(destinationDir);
        }
         */
    }

    private void delete(File dir) {
        try {
            FileUtils.delete(dir, FileUtils.RECURSIVE);
        } catch (IOException ignored) {
        }
    }
}
