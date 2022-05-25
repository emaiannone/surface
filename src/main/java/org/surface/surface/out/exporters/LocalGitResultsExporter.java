package org.surface.surface.out.exporters;

import org.surface.surface.core.metrics.results.ProjectMetricsResults;
import org.surface.surface.out.writers.Writer;

import java.io.IOException;

public class LocalGitResultsExporter extends ResultsExporter {

    public LocalGitResultsExporter(Writer writer) {
        super(writer);
    }

    @Override
    public void export(ProjectMetricsResults projectMetricsResults) throws IOException {
        // TODO Analogous to LocalDirectoryResultsExport but with commit info, too... how? Should i change the parameter to a List<ProjectMetricsResults>
    }
}
