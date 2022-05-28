package org.surface.surface.core.runners;

import org.surface.surface.core.analysis.selectors.RevisionSelector;
import org.surface.surface.core.metrics.results.ProjectMetricsResults;
import org.surface.surface.out.exporters.MixedProjectsResultsExporter;
import org.surface.surface.out.writers.Writer;
import org.surface.surface.out.writers.WriterFactory;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public class FlexibleModeRunner extends ModeRunner<Map<String, Map<String, ProjectMetricsResults>>> {
    private final Path cloneDirPath;
    private final RevisionSelector revisionSelector;

    public FlexibleModeRunner(List<String> metrics, String target, Path outFilePath, String filesRegex, Path cloneDirPath, RevisionSelector revisionSelector) {
        super(metrics, target, outFilePath, filesRegex);
        this.cloneDirPath = cloneDirPath;
        this.revisionSelector = revisionSelector;
        Writer writer = new WriterFactory().getWriter(getOutFilePath());
        setResultsExporter(new MixedProjectsResultsExporter(writer));
    }

    @Override
    public void run() {
        // TODO Read the YAML, interpret it, and decide WHICH and HOW MANY HistoryAnalyzer (new class) to instantiate
    }
}
