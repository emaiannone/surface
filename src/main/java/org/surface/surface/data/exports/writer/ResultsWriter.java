package org.surface.surface.data.exports.writer;

import org.surface.surface.core.metrics.results.ProjectMetricsResults;

import java.io.IOException;
import java.nio.file.Path;

public interface ResultsWriter {
    boolean export(ProjectMetricsResults projectMetricsResults, Path outFilePath) throws IOException;

}
