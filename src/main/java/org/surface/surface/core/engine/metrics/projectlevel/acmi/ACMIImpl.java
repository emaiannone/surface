package org.surface.surface.core.engine.metrics.projectlevel.acmi;

import org.surface.surface.core.engine.inspection.results.InheritanceInspectorResults;
import org.surface.surface.core.engine.inspection.results.InheritanceInspectorResults.InheritanceTreeNode;
import org.surface.surface.core.engine.inspection.results.ProjectInspectorResults;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class ACMIImpl extends ACMI {

    @Override
    public DoubleMetricValue compute(ProjectInspectorResults projectResults) {
        InheritanceInspectorResults inheritanceResult = projectResults.getInheritanceResult();
        Map<String, Double> cmiValues = new LinkedHashMap<>();
        Set<InheritanceTreeNode> roots = inheritanceResult.getInheritanceTreeRoots();
        for (InheritanceTreeNode root : roots) {
            int numClassifiedInheritableMethods = root.getNumberInheritableClassifiedMethods();
            int numClassifiedMethods = root.getNumberClassifiedMethods();
            double metricValue = numClassifiedMethods != 0.0 ? (double) numClassifiedInheritableMethods / numClassifiedMethods : 0.0;
            cmiValues.put(root.getRootFullyQualifiedName(), metricValue);
        }
        double value = cmiValues.values().stream().mapToDouble(x -> x).average().orElse(0.0);
        return new DoubleMetricValue(getName(), getCode(), value);
    }
}
