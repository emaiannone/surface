package org.surface.surface.core;

import java.nio.file.Path;
import java.nio.file.Paths;

public class SurfaceInput {
    private final String[] metricsCodes;
    private final Path remoteProjectsAbsolutePath;
    private final Path projectAbsolutePath;
    private final String exportFormat;
    private final String outFile;

    public SurfaceInput(String[] metricsCodes, String remoteProjectsAbsolutePath, String projectAbsolutePath, String exportFormat, String outFile) {
        this.metricsCodes = metricsCodes;
        this.remoteProjectsAbsolutePath = remoteProjectsAbsolutePath != null ? Paths.get(remoteProjectsAbsolutePath).toAbsolutePath() : null;
        this.projectAbsolutePath = projectAbsolutePath != null ? Paths.get(projectAbsolutePath).toAbsolutePath() : null;
        this.exportFormat = exportFormat;
        this.outFile = outFile;
    }

    public String[] getMetricsCodes() {
        return metricsCodes.clone();
    }

    public Path getRemoteProjectsAbsolutePath() {
        return remoteProjectsAbsolutePath;
    }

    public Path getProjectAbsolutePath() {
        return projectAbsolutePath;
    }

    public String getExportFormat() {
        return exportFormat;
    }

    public String getOutFile() {
        return outFile;
    }
}
