package org.name.tool.core.control;

import java.nio.file.Path;

public class ManyRemoteProjectsControl extends ProjectsControl {
    private final Path remoteProjectsAbsolutePath;

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
        // TODO Read file lines, loop -> clone, call parent analyzeProject()
        System.out.println("* NOT IMPLEMENTED: Remote projects cloning.");
    }
}
