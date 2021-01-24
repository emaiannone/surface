package org.name.tool.core;

import org.name.tool.core.analysis.analyzers.ProjectAnalyzer;
import org.name.tool.core.analysis.results.ClassifiedAnalyzerResults;
import org.name.tool.core.analysis.results.ProjectAnalyzerResults;
import org.name.tool.core.metrics.SecurityMetric;
import org.name.tool.core.metrics.SecurityMetricsFactory;

import java.util.List;

public class Tool {
    private final ToolInput toolInput;

    public Tool(ToolInput toolInput) {
        this.toolInput = toolInput;
    }

    public void run() {
        ProjectAnalyzer projectAnalyzer = new ProjectAnalyzer(toolInput.getProjectAbsolutePath());
        ProjectAnalyzerResults projectResults = projectAnalyzer.analyze();

        List<SecurityMetric> metrics = new SecurityMetricsFactory().getSecurityMetrics(toolInput.getMetricsCodes());

        for (ClassifiedAnalyzerResults classResults : projectResults) {
            for (SecurityMetric metric : metrics) {
                metric.compute(classResults);
                //TODO Compose a list of MetricsResult (name, class, value)
            }
        }

        // TODO Inter-class metrics should be computed here

        // TODO Export
    }
}
