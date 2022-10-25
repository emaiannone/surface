package org.surface.surface.core.engine.metrics.project.rpb;

import org.surface.surface.core.engine.inspection.results.ProjectInspectorResults;
import org.surface.surface.core.engine.metrics.results.values.BooleanMetricValue;

public class RPBImpl extends RPB {

    @Override
    public BooleanMetricValue compute(ProjectInspectorResults projectResults) {
        return new BooleanMetricValue(getName(), getCode(),
                projectResults.hasClassImportingReflection());
    }
}
