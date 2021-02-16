package org.surface.surface.data.exports;

import org.surface.surface.data.bean.Snapshot;
import org.surface.surface.results.ProjectMetricsResults;

import java.io.IOException;

public class ProjectMetricsResultsExporter {
    private final Snapshot snapshot;
    private final ProjectMetricsResults projectMetricsResults;
    private final String[] metricsCodes;

    public ProjectMetricsResultsExporter(Snapshot snapshot, ProjectMetricsResults projectMetricsResults, String[] metricsCodes) {
        this.snapshot = snapshot;
        this.projectMetricsResults = projectMetricsResults;
        this.metricsCodes = metricsCodes;
    }

    public boolean exportAs(String exportFormat) throws IOException {
        SnapshotExporter snapshotExporter = new SnapshotExporterFactory().getExporter(exportFormat);
        return snapshotExporter.export(snapshot, projectMetricsResults, metricsCodes);
    }
}
