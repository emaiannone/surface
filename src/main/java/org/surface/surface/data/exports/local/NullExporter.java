package org.surface.surface.data.exports.local;

import org.surface.surface.results.ProjectMetricsResults;

public class NullExporter implements ResultsExporter {
    public static final String CODE = "";

    @Override
    public boolean export(ProjectMetricsResults projectMetricsResults, String outFile) {
        return true;
    }
}
