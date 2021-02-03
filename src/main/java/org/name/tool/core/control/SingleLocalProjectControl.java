package org.name.tool.core.control;

import java.nio.file.Path;

public class SingleLocalProjectControl extends ProjectsControl {
    private final Path projectAbsolutePath;

    public SingleLocalProjectControl(String[] metricsCodes, Path projectAbsolutePath) {
        super(metricsCodes);
        this.projectAbsolutePath = projectAbsolutePath;
    }

    public Path getProjectAbsolutePath() {
        return projectAbsolutePath;
    }

    @Override
    public void run() {
        System.out.println("* Using " + projectAbsolutePath + " as project root.");
        processProject(projectAbsolutePath);
    }
}