package org.surface.surface.data.exports;

import org.surface.surface.data.bean.Snapshot;
import org.surface.surface.results.ProjectMetricsResults;

import java.io.IOException;

public class ProjectMetricsResultsExporter {
    private final Snapshot snapshot;
    private final ProjectMetricsResults projectMetricsResults;

    public ProjectMetricsResultsExporter(Snapshot snapshot, ProjectMetricsResults projectMetricsResults) {
        this.snapshot = snapshot;
        this.projectMetricsResults = projectMetricsResults;
    }

    public boolean exportAs(String exportFormat) throws IOException {
        SnapshotExporter snapshotExporter = new SnapshotExporterFactory().getExporter(exportFormat);
        return snapshotExporter.export(snapshot, projectMetricsResults);
    }
}
