package org.surface.surface.data.exports.remote;

import org.surface.surface.core.metrics.results.ProjectMetricsResults;
import org.surface.surface.data.bean.Snapshot;

public class NullSnapshotExporter implements SnapshotExporter {
    public static final String CODE = "";

    @Override
    public boolean export(Snapshot snapshot, ProjectMetricsResults projectMetricsResults, String[] metricsCodes, String outFile) {
        return true;
    }
}
