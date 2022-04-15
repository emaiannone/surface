package org.surface.surface.data.exports.remote;

import org.surface.surface.data.bean.Snapshot;
import org.surface.surface.results.ProjectMetricsResults;

import java.io.IOException;

public interface SnapshotExporter {
    boolean export(Snapshot snapshot, ProjectMetricsResults projectMetricsResults, String[] metricsCodes, String outFile) throws IOException;
}
