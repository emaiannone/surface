package org.surface.surface.core.out.exporters;

import org.surface.surface.core.metrics.results.ProjectMetricsResults;
import org.surface.surface.core.out.writers.FileWriter;

import java.io.IOException;
import java.util.Map;

public class MixedProjectsResultsExporter extends ResultsExporter<Map<String, Map<String, ProjectMetricsResults>>> {

    @Override
    public void export(Map<String, Map<String, ProjectMetricsResults>> projectMetricsResults, FileWriter writer) throws IOException {
        // TODO Analogous to LocalDirectoryExporter, but with >1 projects
    }
}
