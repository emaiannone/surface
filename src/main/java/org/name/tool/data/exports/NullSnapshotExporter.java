package org.name.tool.data.exports;

import org.name.tool.data.bean.Snapshot;
import org.name.tool.results.ProjectMetricsResults;

public class NullSnapshotExporter implements SnapshotExporter {
    public static final String CODE = "";

    @Override
    public boolean export(Snapshot snapshot, ProjectMetricsResults projectMetricsResults) {
        return true;
    }
}
