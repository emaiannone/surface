package org.surface.surface.core.engine.metrics.projectlevel.acsi;

import org.surface.surface.core.engine.inspection.results.InheritanceInspectorResults;
import org.surface.surface.core.engine.inspection.results.ProjectInspectorResults;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import static org.surface.surface.core.engine.inspection.results.InheritanceInspectorResults.InheritanceTreeNode;

public class ACSIImpl extends ACSI {

    @Override
    public DoubleMetricValue compute(ProjectInspectorResults projectResults) {
        InheritanceInspectorResults inheritanceResult = projectResults.getInheritanceResult();
        Map<String, Double> csiValues = new LinkedHashMap<>();
        Set<InheritanceTreeNode> roots = inheritanceResult.getInheritanceTreeRoots();
        for (InheritanceTreeNode root : roots) {
            int numInheritorsFromCriticalClasses = root.getNumberInheritorsFromCriticalClasses();
            int numPotentialInheritances = (projectResults.getNumberClasses() - 1) * projectResults.getNumberCriticalClasses();
            double metricValue = numPotentialInheritances != 0.0 ? (double) numInheritorsFromCriticalClasses / numPotentialInheritances : 0.0;
            csiValues.put(root.getRootFullyQualifiedName(), metricValue);
        }
        double value = csiValues.values().stream().mapToDouble(x -> x).average().orElse(0.0);
        return new DoubleMetricValue(getName(), getCode(), value);
    }
}
