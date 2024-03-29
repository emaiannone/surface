package org.surface.surface.core.engine.metrics.clazz.rpb;

import org.surface.surface.core.engine.inspection.results.ClassInspectorResults;
import org.surface.surface.core.engine.metrics.results.values.BooleanMetricValue;

public class RPBImpl extends RPB {

    @Override
    public BooleanMetricValue compute(ClassInspectorResults classResult) {
        return new BooleanMetricValue(getName(), getCode(),
                classResult.isImportingReflection());
    }
}
