package org.surface.surface.core.engine.metrics.project.csp;

import org.surface.surface.core.engine.inspection.results.InheritanceInspectorResults;
import org.surface.surface.core.engine.inspection.results.InheritanceInspectorResults.InheritanceTreeNode;
import org.surface.surface.core.engine.inspection.results.ProjectInspectorResults;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;

public class CSPImpl extends CSP {

    @Override
    public DoubleMetricValue compute(ProjectInspectorResults projectResults) {
        InheritanceInspectorResults inheritanceResult = projectResults.getInheritanceResult();
        int totNumCriticalSuperclasses = 0;
        int totNumCriticalClasses = 0;
        for (InheritanceTreeNode root : inheritanceResult.getInheritanceTreeRoots()) {
            totNumCriticalSuperclasses += root.getNumberCriticalSuperclasses();
            totNumCriticalClasses += root.getNumberCriticalClasses();
        }
        double value = totNumCriticalClasses != 0.0 ? (double) totNumCriticalSuperclasses / totNumCriticalClasses : 0.0;
        return new DoubleMetricValue(getName(), getCode(), value);
    }
}
