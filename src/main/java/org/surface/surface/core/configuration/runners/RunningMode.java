package org.surface.surface.core.configuration.runners;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.surface.surface.core.configuration.runners.results.RunResults;
import org.surface.surface.core.engine.analysis.results.FormattableAnalysisResults;
import org.surface.surface.core.engine.metrics.api.MetricsManager;
import org.surface.surface.core.exporters.RunResultsExporter;

import java.io.IOException;
import java.util.Set;
import java.util.regex.Pattern;

public abstract class RunningMode {
    private static final Logger LOGGER = LogManager.getLogger();

    private final Set<Pattern> classifiedPatterns;
    private final MetricsManager metricsManager;
    private final RunResultsExporter runResultsExporter;
    private final String filesRegex;
    private final boolean includeTests;
    private final RunResults runResults;
    private String codeName;

    RunningMode(RunResultsExporter runResultsExporter, Set<Pattern> classifiedPatterns, MetricsManager metricsManager, String filesRegex, boolean includeTests) {
        if (runResultsExporter == null) {
            throw new IllegalArgumentException("The writer to an output file must not be null.");
        }
        this.classifiedPatterns = classifiedPatterns;
        this.metricsManager = metricsManager;
        this.runResultsExporter = runResultsExporter;
        this.filesRegex = filesRegex;
        this.includeTests = includeTests;
        this.runResults = new RunResults();
    }

    public Set<Pattern> getClassifiedPatterns() {
        return classifiedPatterns;
    }

    public MetricsManager getMetricsManager() {
        return metricsManager;
    }

    public RunResultsExporter getFormatter() {
        return runResultsExporter;
    }

    public String getFilesRegex() {
        return filesRegex;
    }

    public boolean isIncludeTests() {
        return includeTests;
    }

    public String getCodeName() {
        return codeName;
    }

    public void setCodeName(String codeName) {
        this.codeName = codeName;
    }

    public RunResults getRunResults() {
        return runResults;
    }

    public void addAnalysisResults(String project, FormattableAnalysisResults results) {
        runResults.addAnalysisResults(project, results);
    }

    public abstract void run() throws Exception;

    void exportResults() throws IOException {
        LOGGER.debug("* Exporting results to {}", runResultsExporter.getOutFile());
        try {
            String exportString = runResultsExporter.export(runResults);
            LOGGER.trace("Results exported: {}", exportString);
            LOGGER.debug("* Exporting completed to {}", runResultsExporter.getOutFile());
        } catch (IOException e) {
            throw new IOException("Failed to export to " + runResultsExporter.getOutFile(), e);
        }
    }
}
