package org.surface.surface.core.engine.metrics.project.cmi;

import org.surface.surface.core.engine.inspection.results.InheritanceInspectorResults;
import org.surface.surface.core.engine.inspection.results.InheritanceInspectorResults.InheritanceTreeNode;
import org.surface.surface.core.engine.inspection.results.ProjectInspectorResults;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;

public class CMIImpl extends CMI {

    @Override
    public DoubleMetricValue compute(ProjectInspectorResults projectResults) {
        InheritanceInspectorResults inheritanceResult = projectResults.getInheritanceResult();
        int totNumClassifiedInheritableMethods = 0;
        int totNumClassifiedMethods = 0;
        for (InheritanceTreeNode root : inheritanceResult.getInheritanceTreeRoots()) {
            totNumClassifiedInheritableMethods += root.getNumberInheritableClassifiedMethods();
            totNumClassifiedMethods += root.getNumberClassifiedMethods();
        }
        double value = totNumClassifiedMethods != 0.0 ? (double) totNumClassifiedInheritableMethods / totNumClassifiedMethods : 0.0;
        return new DoubleMetricValue(getName(), getCode(), value);
    }
}
