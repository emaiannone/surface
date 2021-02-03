package org.name.tool.data.exports;

import org.name.tool.data.bean.Snapshot;
import org.name.tool.results.ProjectMetricsResults;

import java.io.IOException;

public interface SnapshotExporter {
    boolean export(Snapshot snapshot, ProjectMetricsResults projectMetricsResults) throws IOException;
}
