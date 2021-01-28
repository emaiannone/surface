package org.name.tool.core;

import org.name.tool.core.analysis.ProjectAnalyzer;
import org.name.tool.core.metrics.api.ProjectMetricsCalculator;
import org.name.tool.core.results.ProjectAnalyzerResults;
import org.name.tool.core.results.ProjectMetricsResults;

public class Tool {
    private final ToolInput toolInput;

    public Tool(ToolInput toolInput) {
        this.toolInput = toolInput;
    }

    public void run() {
        ProjectAnalyzer projectAnalyzer = new ProjectAnalyzer(toolInput.getProjectAbsolutePath());
        System.out.println("* Project Analysis starting");
        ProjectAnalyzerResults projectAnalyzerResults = projectAnalyzer.analyze();
        System.out.println("* Project Analysis finished");
        System.out.println("* Printing Project results");
        System.out.println(projectAnalyzerResults);

        System.out.println("* Project Metrics Computation starting");
        ProjectMetricsCalculator projectMetricsCalculator = new ProjectMetricsCalculator(projectAnalyzerResults);
        ProjectMetricsResults projectMetricsResults = projectMetricsCalculator.calculate(toolInput.getMetricsCodes());
        System.out.println("* Project Metrics Computation finished");
        System.out.println("* Printing Project Metrics");
        System.out.println(projectMetricsResults);

        // TODO Export
    }
}
