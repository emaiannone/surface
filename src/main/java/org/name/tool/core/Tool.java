package org.name.tool.core;

import org.name.tool.core.analysis.ProjectAnalyzer;
import org.name.tool.core.analysis.results.ClassifiedAnalyzerResults;
import org.name.tool.core.analysis.results.ProjectAnalyzerResults;
import org.name.tool.core.metrics.api.SecurityMetric;
import org.name.tool.core.metrics.SecurityMetricsFactory;
import org.name.tool.core.metrics.api.SecurityMetricResult;

import java.util.ArrayList;
import java.util.List;

public class Tool {
    private final ToolInput toolInput;

    public Tool(ToolInput toolInput) {
        this.toolInput = toolInput;
    }

    public void run() {
        ProjectAnalyzer projectAnalyzer = new ProjectAnalyzer(toolInput.getProjectAbsolutePath());
        System.out.println("* Project Analysis starting");
        ProjectAnalyzerResults projectResults = projectAnalyzer.analyze();
        System.out.println("* Project Analysis finished");
        System.out.println("* Printing Project results");
        System.out.println(projectResults);

        System.out.println("* Project Metrics Computation starting");
        List<SecurityMetricResult> metricsResults = new ArrayList<>();
        for (ClassifiedAnalyzerResults classResults : projectResults) {
            List<SecurityMetric> metrics = new SecurityMetricsFactory().getSecurityMetrics(toolInput.getMetricsCodes());
            for (SecurityMetric metric : metrics) {
                SecurityMetricResult metricResult = metric.compute(classResults);
                metricsResults.add(metricResult);
            }
        }
        System.out.println("* Project Metrics Computation finished");
        System.out.println("* Printing Project Metrics");
        metricsResults.forEach(System.out::println);

        // TODO Inter-class metrics should be computed here

        // TODO Export
    }
}
