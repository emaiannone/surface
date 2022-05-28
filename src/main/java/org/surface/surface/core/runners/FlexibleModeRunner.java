package org.surface.surface.core.runners;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.surface.surface.common.RevisionMode;
import org.surface.surface.core.analysis.selectors.RevisionSelector;
import org.surface.surface.core.analysis.selectors.RevisionSelectorFactory;
import org.surface.surface.core.metrics.results.ProjectMetricsResults;
import org.surface.surface.out.exporters.MixedProjectsResultsExporter;
import org.surface.surface.out.writers.Writer;
import org.surface.surface.out.writers.WriterFactory;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public class FlexibleModeRunner extends ModeRunner<Map<String, Map<String, ProjectMetricsResults>>> {
    private static final Logger LOGGER = LogManager.getLogger();

    private final RevisionSelector revisionSelector;
    private final Path workDirPath;

    FlexibleModeRunner(List<String> metrics, String target, Path outFilePath, String filesRegex, Pair<RevisionMode, String> revision, Path workDirPath) {
        super(metrics, target, outFilePath, filesRegex);
        this.revisionSelector = new RevisionSelectorFactory().selectRevisionSelector(revision);
        this.workDirPath = workDirPath;
        Writer writer = new WriterFactory().getWriter(getOutFilePath());
        setResultsExporter(new MixedProjectsResultsExporter(writer));
    }

    @Override
    public void run() {
        // TODO Read the YAML, interpret it, and decide WHICH and HOW MANY HistoryAnalyzers instantiate
        //  Update the JSON after a projects is fully analyzed steps instead o waiting till the end?
    }
}
