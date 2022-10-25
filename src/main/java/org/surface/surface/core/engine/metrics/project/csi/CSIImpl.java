package org.surface.surface.core.engine.metrics.project.csi;

import org.surface.surface.core.engine.inspection.results.InheritanceInspectorResults;
import org.surface.surface.core.engine.inspection.results.ProjectInspectorResults;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;

import static org.surface.surface.core.engine.inspection.results.InheritanceInspectorResults.InheritanceTreeNode;

public class CSIImpl extends CSI {

    @Override
    public DoubleMetricValue compute(ProjectInspectorResults projectResult) {
        InheritanceInspectorResults inheritanceResult = projectResult.getInheritanceResult();
        int totNumInheritorsFromCriticalClasses = 0;
        int totNumPotentialInheritances = 0;
        for (InheritanceTreeNode root : inheritanceResult.getInheritanceTreeRoots()) {
            totNumInheritorsFromCriticalClasses += root.getNumberInheritorsFromCriticalClasses();
            totNumPotentialInheritances += (projectResult.getNumberClasses() - 1) * projectResult.getNumberCriticalClasses();
        }
        double value = totNumPotentialInheritances != 0.0 ? (double) totNumInheritorsFromCriticalClasses / totNumPotentialInheritances : 0.0;
        return new DoubleMetricValue(getName(), getCode(), value);
    }
}
