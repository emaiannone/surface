package org.surface.surface.core.runners;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.surface.surface.core.analysis.HistoryAnalyzer;
import org.surface.surface.core.analysis.selectors.RevisionSelector;
import org.surface.surface.core.analysis.setup.SetupEnvironmentAction;
import org.surface.surface.core.metrics.api.MetricsManager;
import org.surface.surface.core.metrics.results.ProjectMetricsResults;
import org.surface.surface.core.out.writers.FileWriter;

import java.util.Map;

public abstract class GitModeRunner extends ModeRunner<Map<String, ProjectMetricsResults>> {
    private static final Logger LOGGER = LogManager.getLogger();

    private final RevisionSelector revisionSelector;
    private SetupEnvironmentAction setupEnvironmentAction;

    GitModeRunner(MetricsManager metricsManager, FileWriter writer, String filesRegex, boolean includeTests, RevisionSelector revisionSelector) {
        super(metricsManager, writer, filesRegex, includeTests);
        this.revisionSelector = revisionSelector;
    }

    abstract String getProjectName();

    public void setSetupEnvironmentAction(SetupEnvironmentAction setupEnvironmentAction) {
        this.setupEnvironmentAction = setupEnvironmentAction;
    }

    public RevisionSelector getRevisionSelector() {
        return revisionSelector;
    }

    @Override
    public void run() throws Exception {
        HistoryAnalyzer historyAnalyzer = new HistoryAnalyzer(getProjectName(), getFilesRegex(),
                getMetricsManager(), isIncludeTests(), revisionSelector, setupEnvironmentAction);
        // TODO Wrap this into a different object, that exposes method to query it from clients without depending on metrics pakcage
        Map<String, ProjectMetricsResults> allResults = historyAnalyzer.analyze().getResults();
        exportResults(allResults);
        LOGGER.info("* Exported results to {}", getWriter().getOutFile());
    }
}
