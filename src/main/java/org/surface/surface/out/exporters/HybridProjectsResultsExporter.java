package org.surface.surface.out.exporters;

import org.surface.surface.core.metrics.results.ProjectMetricsResults;
import org.surface.surface.out.writers.Writer;

import java.io.IOException;
import java.util.Map;

public class HybridProjectsResultsExporter extends ResultsExporter<Map<String, Map<String, ProjectMetricsResults>>> {

    public HybridProjectsResultsExporter(Writer writer) {
        super(writer);
    }

    @Override
    public void export(Map<String, Map<String, ProjectMetricsResults>> projectMetricsResults) throws IOException {
        // TODO Analogous to LocalDirectoryExporter, but with >1 projects
    }
}
