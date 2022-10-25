package org.surface.surface.core.engine.metrics.project.acai;

import org.surface.surface.core.engine.inspection.results.InheritanceInspectorResults;
import org.surface.surface.core.engine.inspection.results.ProjectInspectorResults;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import static org.surface.surface.core.engine.inspection.results.InheritanceInspectorResults.InheritanceTreeNode;

public class ACAIImpl extends ACAI {

    @Override
    public DoubleMetricValue compute(ProjectInspectorResults projectResults) {
        InheritanceInspectorResults inheritanceResult = projectResults.getInheritanceResult();
        Map<String, Double> caiValues = new LinkedHashMap<>();
        Set<InheritanceTreeNode> roots = inheritanceResult.getInheritanceTreeRoots();
        for (InheritanceTreeNode root : roots) {
            int numClassifiedInheritableAttributes = root.getNumberInheritableClassifiedAttributes();
            int numClassifiedAttributes = root.getNumberClassifiedAttributes();
            double metricValue = numClassifiedAttributes != 0.0 ? (double) numClassifiedInheritableAttributes / numClassifiedAttributes : 0.0;
            caiValues.put(root.getRootFullyQualifiedName(), metricValue);
        }
        double value = caiValues.values().stream().mapToDouble(x -> x).average().orElse(0.0);
        return new DoubleMetricValue(getName(), getCode(), value);
    }
}
