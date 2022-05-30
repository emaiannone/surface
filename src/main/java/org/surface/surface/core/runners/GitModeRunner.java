package org.surface.surface.core.runners;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.surface.surface.common.RevisionMode;
import org.surface.surface.core.analysis.HistoryAnalyzer;
import org.surface.surface.core.analysis.selectors.RevisionSelector;
import org.surface.surface.core.analysis.setup.SetupEnvironmentAction;
import org.surface.surface.core.metrics.results.ProjectMetricsResults;

import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

public abstract class GitModeRunner extends ModeRunner<Map<String, ProjectMetricsResults>> {
    private static final Logger LOGGER = LogManager.getLogger();

    private final RevisionSelector revisionSelector;
    private SetupEnvironmentAction setupEnvironmentAction;

    GitModeRunner(List<String> metrics, String target, Pair<String, String> outFile, String filesRegex, Pair<RevisionMode, String> revision) {
        super(metrics, target, outFile, filesRegex);
        this.revisionSelector = RevisionSelector.newRevisionSelector(revision);
    }

    void setSetupEnvironmentAction(SetupEnvironmentAction setupEnvironmentAction) {
        this.setupEnvironmentAction = setupEnvironmentAction;
    }

    String getProjectName() {
        return Paths.get(getTarget()).getFileName().toString();
    }

    @Override
    public void run() throws Exception {
        HistoryAnalyzer historyAnalyzer = new HistoryAnalyzer(getProjectName(), getDefaultFilesRegex(), getMetrics(), revisionSelector, setupEnvironmentAction);
        Map<String, ProjectMetricsResults> allResults = historyAnalyzer.analyze();
        exportResults(allResults);
    }
}
