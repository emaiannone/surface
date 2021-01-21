package org.name.tool.core;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import org.name.tool.core.analysis.ProjectAnalyzer;
import org.name.tool.core.metrics.Metric;
import org.name.tool.core.metrics.ca.ClassifiedAttributes;
import org.name.tool.core.metrics.ca.ClassifiedAttributesCached;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Tool {
    private final ToolInput toolInput;

    public Tool(ToolInput toolInput) {
        this.toolInput = toolInput;
    }

    public void run() {
        ProjectAnalyzer projectAnalyzer = new ProjectAnalyzer(toolInput.getProjectAbsolutePath());
        HashMap<ClassOrInterfaceDeclaration, HashMap<FieldDeclaration, MethodDeclaration>> projectMap = projectAnalyzer.analyze();

        // TODO How to properly compose Indirect Metrics? (if any)
        List<Metric> metrics = new ArrayList<>();
        for (String metricCode : toolInput.getMetricsCodes()) {
            Metric metric = null;
            switch (metricCode) {
                case ClassifiedAttributes.CODE:
                    metric = new ClassifiedAttributesCached();
                    break;
                //TODO Add other Metrics
            }
            if (metric != null) {
                metrics.add(metric);
            }
        }

        // TODO Iterate over the classes of "big map": "little map"
        for (Metric metric : metrics) {
            // TODO Pass the "little map"
            metric.compute();
            //TODO Compose the list of results (map of class-metric-value?)
        }

        // TODO Inter-class metrics should be computed here

        // TODO If enabled:  export
    }
}
