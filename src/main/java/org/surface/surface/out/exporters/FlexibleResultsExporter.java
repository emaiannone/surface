package org.surface.surface.out.exporters;

import org.surface.surface.core.metrics.results.ProjectMetricsResults;
import org.surface.surface.out.writers.Writer;

import java.io.IOException;

public class FlexibleResultsExporter extends ResultsExporter {

    public FlexibleResultsExporter(Writer writer) {
        super(writer);
    }

    @Override
    public void export(ProjectMetricsResults projectMetricsResults) throws IOException {
        // TODO Analogous to LocalDirectoryExporter, but with >1 projects
    }
}
