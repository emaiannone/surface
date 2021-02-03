package org.name.tool.data.exports;

import org.name.tool.core.results.ProjectMetricsResults;
import org.name.tool.data.bean.Snapshot;

public class NullSnapshotExporter implements SnapshotExporter {
    public static final String CODE = "";

    @Override
    public boolean export(Snapshot snapshot, ProjectMetricsResults projectMetricsResults) {
        return true;
    }
}
