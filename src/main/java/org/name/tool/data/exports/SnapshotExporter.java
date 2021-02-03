package org.name.tool.data.exports;

import org.name.tool.core.results.ProjectMetricsResults;
import org.name.tool.data.bean.Snapshot;

import java.io.IOException;

public interface SnapshotExporter {
    boolean export(Snapshot snapshot, ProjectMetricsResults projectMetricsResults) throws IOException;
}
