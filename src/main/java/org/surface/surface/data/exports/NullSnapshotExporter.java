package org.surface.surface.data.exports;

import org.surface.surface.data.bean.Snapshot;
import org.surface.surface.results.ProjectMetricsResults;

public class NullSnapshotExporter implements SnapshotExporter {
    public static final String CODE = "";

    @Override
    public boolean export(Snapshot snapshot, ProjectMetricsResults projectMetricsResults) {
        return true;
    }
}
