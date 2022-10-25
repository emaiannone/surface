package org.surface.surface.core.engine.metrics.project.cmt;

import org.surface.surface.core.engine.inspection.results.ProjectInspectorResults;
import org.surface.surface.core.engine.metrics.results.values.IntMetricValue;

public class CMTImpl extends CMT {

    @Override
    public IntMetricValue compute(ProjectInspectorResults projectResult) {
        return new IntMetricValue(getName(), getCode(),
                projectResult.getNumberAllClassifiedMethods());
    }
}
