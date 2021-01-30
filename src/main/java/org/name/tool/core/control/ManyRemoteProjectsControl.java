package org.name.tool.core.control;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.util.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ManyRemoteProjectsControl extends ProjectsControl {
    private final Path remoteProjectsAbsolutePath;

    public static final String BASE_DIR = "/tmp";

    public ManyRemoteProjectsControl(String[] metricsCodes, String exportFormat, Path remoteProjectsAbsolutePath) {
        super(metricsCodes, exportFormat);
        this.remoteProjectsAbsolutePath = remoteProjectsAbsolutePath;
    }

    public Path getRemoteProjectsAbsolutePath() {
        return remoteProjectsAbsolutePath;
    }

    @Override
    public void run() {
        System.out.println("* Using " + remoteProjectsAbsolutePath + " file for cloning and analyzing repositories.");
        // TODO Process remoteProjectsAbsolutePath csv
        try {
            List<String> repositoriesURI = new ArrayList<>();
            Scanner scanner = new Scanner(new File(String.valueOf(remoteProjectsAbsolutePath)));
            while (scanner.hasNext()) {
                String line = scanner.nextLine();
                try {
                    repositoriesURI.add(line);
                } catch (InvalidPathException e) {
                    System.out.println("* Path " + line + " is invalid: skipping.");
                }
            }
            for (String repositoryURI : repositoriesURI) {
                try {
                    String projectName = Paths.get(repositoryURI).getFileName().toString();
                    File destinationDir = Paths.get(BASE_DIR, projectName).toFile();
                    // Delete destination directory (with all its conrent) if it already exists
                    delete(destinationDir);
                    System.out.println("* Cloning " + repositoryURI + " to " + destinationDir);
                    try (Git repo = Git.cloneRepository()
                            .setDirectory(destinationDir)
                            .setURI(repositoryURI)
                            .call()) {
                        processProject(destinationDir.toPath());
                    }
                    // Delete after finishing
                    delete(destinationDir);
                } catch (GitAPIException e) {
                    System.out.println("* Failed to clone " + repositoryURI + ": skipping.");
                }
            }
            System.out.println("* NOT IMPLEMENTED: Remote projects cloning.");
        } catch (FileNotFoundException e) {
            System.out.println("* File " + remoteProjectsAbsolutePath + " cannot be found: quitting.");
        }
    }

    private void delete(File dir) {
        try {
            FileUtils.delete(dir, FileUtils.RECURSIVE);
        } catch (IOException ignored) {
        }
    }
}
