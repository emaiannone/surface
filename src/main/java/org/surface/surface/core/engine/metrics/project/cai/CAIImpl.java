package org.surface.surface.core.engine.metrics.project.cai;

import org.surface.surface.core.engine.inspection.results.InheritanceInspectorResults;
import org.surface.surface.core.engine.inspection.results.ProjectInspectorResults;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;

import static org.surface.surface.core.engine.inspection.results.InheritanceInspectorResults.InheritanceTreeNode;

public class CAIImpl extends CAI {

    @Override
    public DoubleMetricValue compute(ProjectInspectorResults projectResult) {
        InheritanceInspectorResults inheritanceResult = projectResult.getInheritanceResult();
        int totNumClassifiedInheritableAttributes = 0;
        int totNumClassifiedAttributes = 0;
        for (InheritanceTreeNode root : inheritanceResult.getInheritanceTreeRoots()) {
            totNumClassifiedInheritableAttributes += root.getNumberInheritableClassifiedAttributes();
            totNumClassifiedAttributes += root.getNumberClassifiedAttributes();
        }
        double value = totNumClassifiedAttributes != 0.0 ? (double) totNumClassifiedInheritableAttributes / totNumClassifiedAttributes : 0.0;
        return new DoubleMetricValue(getName(), getCode(), value);
    }
}
