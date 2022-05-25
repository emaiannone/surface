package org.surface.surface.data.exports.writer;

import org.surface.surface.core.metrics.results.ProjectMetricsResults;

import java.nio.file.Path;

public class NullWriter implements ResultsWriter {
    public static final String CODE = "";

    @Override
    public boolean export(ProjectMetricsResults projectMetricsResults, Path outFilePath) {
        return true;
    }
}
