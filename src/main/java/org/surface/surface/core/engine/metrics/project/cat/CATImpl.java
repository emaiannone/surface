package org.surface.surface.core.engine.metrics.project.cat;

import org.surface.surface.core.engine.inspection.results.ProjectInspectorResults;
import org.surface.surface.core.engine.metrics.results.values.IntMetricValue;

public class CATImpl extends CAT {

    @Override
    public IntMetricValue compute(ProjectInspectorResults projectResult) {
        return new IntMetricValue(getName(), getCode(),
                projectResult.getNumberAllClassifiedAttributes());
    }
}
