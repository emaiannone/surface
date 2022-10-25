package org.surface.surface.core.engine.metrics.project.acsp;

import org.surface.surface.core.engine.inspection.results.InheritanceInspectorResults;
import org.surface.surface.core.engine.inspection.results.InheritanceInspectorResults.InheritanceTreeNode;
import org.surface.surface.core.engine.inspection.results.ProjectInspectorResults;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class ACSPImpl extends ACSP {

    @Override
    public DoubleMetricValue compute(ProjectInspectorResults projectResults) {
        InheritanceInspectorResults inheritanceResult = projectResults.getInheritanceResult();
        Map<String, Double> cspValues = new LinkedHashMap<>();
        Set<InheritanceTreeNode> roots = inheritanceResult.getInheritanceTreeRoots();
        for (InheritanceTreeNode root : roots) {
            int numCriticalClasses = root.getNumberCriticalClasses();
            int numCriticalSuperclasses = root.getNumberCriticalSuperclasses();
            double metricValue = numCriticalClasses != 0.0 ? (double) numCriticalSuperclasses / numCriticalClasses : 0.0;
            cspValues.put(root.getRootFullyQualifiedName(), metricValue);
        }
        double value = cspValues.values().stream().mapToDouble(x -> x).average().orElse(0.0);
        return new DoubleMetricValue(getName(), getCode(), value);
    }
}
