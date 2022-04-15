package org.surface.surface.data.exports.local;

import org.surface.surface.results.ProjectMetricsResults;

import java.io.IOException;

public interface ResultsExporter {
    boolean export(ProjectMetricsResults projectMetricsResults) throws IOException;

}
