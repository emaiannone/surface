package org.surface.surface.core.configuration.runners;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.surface.surface.core.configuration.runners.results.GitRunResults;
import org.surface.surface.core.engine.analysis.HistoryAnalyzer;
import org.surface.surface.core.engine.analysis.results.HistoryAnalysisResults;
import org.surface.surface.core.engine.analysis.selectors.RevisionSelector;
import org.surface.surface.core.engine.metrics.api.MetricsManager;
import org.surface.surface.core.engine.writers.FileWriter;

import java.nio.file.Path;

public abstract class GitRunningMode extends SingleProjectRunningMode<GitRunResults> {
    private static final Logger LOGGER = LogManager.getLogger();

    private final RevisionSelector revisionSelector;
    private final boolean excludeWorkTree;
    private String repoLocation;

    GitRunningMode(Path workDirPath, FileWriter writer, MetricsManager metricsManager, String filesRegex, boolean includeTests, RevisionSelector revisionSelector, boolean excludeWorkTree) {
        super(workDirPath, writer, metricsManager, filesRegex, includeTests);
        if (revisionSelector == null) {
            throw new IllegalArgumentException("The revision selector must not be null.");
        }
        this.revisionSelector = revisionSelector;
        this.excludeWorkTree = excludeWorkTree;
    }

    public String getRepoLocation() {
        return repoLocation;
    }

    public void setRepoLocation(String repoLocation) {
        this.repoLocation = repoLocation;
    }

    @Override
    public void run() throws Exception {
        HistoryAnalyzer historyAnalyzer = new HistoryAnalyzer(getProjectName(), getFilesRegex(),
                getMetricsManager(), isIncludeTests(), excludeWorkTree, revisionSelector, getSetupEnvironmentAction());
        HistoryAnalysisResults analysisResults = historyAnalyzer.analyze();
        GitRunResults gitRunResults = getRunResults();
        gitRunResults.setAnalysisResults(analysisResults);
        exportResults(gitRunResults);
        LOGGER.info("* Exported results to {}", getWriter().getOutFile());
    }
}
