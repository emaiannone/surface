package org.surface.surface.out.exporters;

import org.surface.surface.core.metrics.results.ProjectMetricsResults;
import org.surface.surface.out.writers.Writer;

import java.io.IOException;

public class RemoteGitResultsExporter extends ResultsExporter {

    public RemoteGitResultsExporter(Writer writer) {
        super(writer);
    }

    @Override
    public void export(ProjectMetricsResults projectMetricsResults) throws IOException {
        // TODO Analogous to LocalDirectoryExporter, but with URL instead of path?
    }
}
