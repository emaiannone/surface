package org.surface.surface.core.configuration.runners;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.surface.surface.core.configuration.runners.results.GitRunResults;
import org.surface.surface.core.engine.analysis.HistoryAnalyzer;
import org.surface.surface.core.engine.analysis.results.HistoryAnalysisResults;
import org.surface.surface.core.engine.analysis.selectors.RevisionSelector;
import org.surface.surface.core.engine.analysis.setup.SetupEnvironmentAction;
import org.surface.surface.core.engine.metrics.api.MetricsManager;
import org.surface.surface.core.engine.writers.FileWriter;

public abstract class GitRunningMode extends RunningMode<GitRunResults> {
    private static final Logger LOGGER = LogManager.getLogger();

    private final boolean excludeWorkTree;
    private final RevisionSelector revisionSelector;
    private String projectName;
    private String repoLocation;
    private SetupEnvironmentAction setupEnvironmentAction;

    GitRunningMode(FileWriter writer, MetricsManager metricsManager, RevisionSelector revisionSelector, String filesRegex, boolean includeTests, boolean excludeWorkTree) {
        super(writer, metricsManager, filesRegex, includeTests);
        if (revisionSelector == null) {
            throw new IllegalArgumentException("The revision selector must not be null.");
        }
        this.excludeWorkTree = excludeWorkTree;
        this.revisionSelector = revisionSelector;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getRepoLocation() {
        return repoLocation;
    }

    public void setRepoLocation(String repoLocation) {
        this.repoLocation = repoLocation;
    }

    public void setSetupEnvironmentAction(SetupEnvironmentAction setupEnvironmentAction) {
        this.setupEnvironmentAction = setupEnvironmentAction;
    }

    public RevisionSelector getRevisionSelector() {
        return revisionSelector;
    }

    @Override
    public void run() throws Exception {
        HistoryAnalyzer historyAnalyzer = new HistoryAnalyzer(getProjectName(), getFilesRegex(),
                getMetricsManager(), isIncludeTests(), excludeWorkTree, revisionSelector, setupEnvironmentAction);
        HistoryAnalysisResults analysisResults = historyAnalyzer.analyze();
        GitRunResults gitRunResults = getRunResults();
        gitRunResults.setAnalysisResults(analysisResults);
        exportResults(gitRunResults);
        LOGGER.info("* Exported results to {}", getWriter().getOutFile());
    }
}
