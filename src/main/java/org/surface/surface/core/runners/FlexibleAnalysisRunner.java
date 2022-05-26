package org.surface.surface.core.runners;

import org.surface.surface.common.selectors.RevisionSelector;
import org.surface.surface.core.metrics.results.ProjectMetricsResults;
import org.surface.surface.out.exporters.HybridProjectsResultsExporter;
import org.surface.surface.out.writers.Writer;
import org.surface.surface.out.writers.WriterFactory;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public class FlexibleAnalysisRunner extends AnalysisRunner<Map<String, Map<String, ProjectMetricsResults>>> {
    private final Path cloneDirPath;
    private final RevisionSelector revisionSelector;

    public FlexibleAnalysisRunner(List<String> metrics, String target, Path outFilePath, String filesRegex, Path cloneDirPath, RevisionSelector revisionSelector) {
        super(metrics, target, outFilePath, filesRegex);
        this.cloneDirPath = cloneDirPath;
        this.revisionSelector = revisionSelector;
        Writer writer = new WriterFactory().getWriter(getOutFilePath());
        setResultsExporter(new HybridProjectsResultsExporter(writer));
    }

    @Override
    public void run() {
        // TODO implement: read the YAML file (target) and understand what to do
    }
}
