package org.surface.surface.out.exporters;

import org.surface.surface.core.metrics.results.ProjectMetricsResults;
import org.surface.surface.out.writers.Writer;

import java.io.IOException;
import java.util.Map;

public class GitProjectResultsExporter extends ResultsExporter<Map<String, ProjectMetricsResults>> {

    public GitProjectResultsExporter(Writer writer) {
        super(writer);
    }

    @Override
    public void export(Map<String, ProjectMetricsResults> projectMetricsResults) throws IOException {
        // TODO Analogous to LocalDirectoryExporter but one result per commit
    }
}
