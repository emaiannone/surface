package org.surface.surface.core.configuration.runners;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.surface.surface.core.engine.analysis.HistoryAnalyzer;
import org.surface.surface.core.engine.analysis.results.HistoryAnalysisResults;
import org.surface.surface.core.engine.analysis.selectors.RevisionSelector;
import org.surface.surface.core.engine.exporters.RunResultsExporter;
import org.surface.surface.core.engine.metrics.api.MetricsManager;

import java.nio.file.Path;

public abstract class GitRunningMode extends SingleProjectRunningMode {
    private static final Logger LOGGER = LogManager.getLogger();

    private final RevisionSelector revisionSelector;
    private final boolean excludeWorkTree;
    private String repoLocation;

    GitRunningMode(Path workDirPath, RunResultsExporter runResultsExporter, MetricsManager metricsManager, String filesRegex, boolean includeTests, RevisionSelector revisionSelector, boolean excludeWorkTree) {
        super(workDirPath, runResultsExporter, metricsManager, filesRegex, includeTests);
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
        HistoryAnalyzer historyAnalyzer = new HistoryAnalyzer(getProjectName(), getRepoLocation(), getFilesRegex(),
                getMetricsManager(), isIncludeTests(), excludeWorkTree, revisionSelector, getSetupEnvironmentAction());
        HistoryAnalysisResults analysisResults = historyAnalyzer.analyze();
        addAnalysisResults(getProjectName(), analysisResults);
        exportResults();
        LOGGER.info("* Exported results to {}", getFormatter().getOutFile());
    }
}
