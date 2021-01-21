package org.name.tool.core;

import java.nio.file.Path;

public class ToolInput {
    private final Path projectAbsolutePath;
    private final String[] metricsCodes;
    private final String exportFormat;

    public ToolInput(Path projectAbsolutePath, String[] metricsCodes, String exportFormat) {
        this.projectAbsolutePath = projectAbsolutePath;
        this.metricsCodes = metricsCodes;
        this.exportFormat = exportFormat;
    }

    public Path getProjectAbsolutePath() {
        return projectAbsolutePath;
    }

    public String[] getMetricsCodes() {
        return metricsCodes.clone();
    }

    public String getExportFormat() {
        return exportFormat;
    }
}
