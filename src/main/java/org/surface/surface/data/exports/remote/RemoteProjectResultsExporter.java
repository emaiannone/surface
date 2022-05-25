package org.surface.surface.data.exports.remote;

import org.surface.surface.core.metrics.results.ProjectMetricsResults;
import org.surface.surface.data.bean.Snapshot;

import java.io.IOException;

public class RemoteProjectResultsExporter {
    private final Snapshot snapshot;
    private final ProjectMetricsResults projectMetricsResults;
    private final String[] metricsCodes;

    public RemoteProjectResultsExporter(Snapshot snapshot, ProjectMetricsResults projectMetricsResults, String[] metricsCodes) {
        this.snapshot = snapshot;
        this.projectMetricsResults = projectMetricsResults;
        this.metricsCodes = metricsCodes;
    }

    public boolean export(String exportFormat, String outFile) throws IOException {
        SnapshotExporter snapshotExporter = new SnapshotExporterFactory().getExporter(exportFormat);
        return snapshotExporter.export(snapshot, projectMetricsResults, metricsCodes, outFile);
    }
}
