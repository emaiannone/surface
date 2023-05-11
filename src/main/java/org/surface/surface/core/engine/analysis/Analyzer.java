package org.surface.surface.core.engine.analysis;

import org.surface.surface.core.engine.analysis.results.FormattableAnalysisResults;
import org.surface.surface.core.engine.metrics.api.MetricsManager;

import java.util.Set;
import java.util.regex.Pattern;

public abstract class Analyzer {
    private final String filesRegex;
    private final Set<Pattern> classifiedPatterns;
    private final MetricsManager metricsManager;
    private final boolean includeTests;

    public Analyzer(String filesRegex, Set<Pattern> classifiedPatterns, MetricsManager metricsManager, boolean includeTests) {
        this.filesRegex = filesRegex;
        this.classifiedPatterns = classifiedPatterns;
        this.metricsManager = metricsManager;
        this.includeTests = includeTests;
    }

    public String getFilesRegex() {
        return filesRegex;
    }

    public Set<Pattern> getClassifiedPatterns() {
        return classifiedPatterns;
    }

    public MetricsManager getMetricsManager() {
        return metricsManager;
    }

    public boolean isIncludeTests() {
        return includeTests;
    }

    public abstract FormattableAnalysisResults analyze() throws Exception;
}
