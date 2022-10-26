package org.surface.surface.core.engine.metrics.project.pi;

import org.surface.surface.core.engine.inspection.results.ProjectInspectorResults;
import org.surface.surface.core.engine.metrics.project.wcc.WCC;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;

public class PIImpl extends PI {
    private final WCC wcc;

    public PIImpl(WCC wcc) {
        this.wcc = wcc;
    }

    @Override
    public DoubleMetricValue compute(ProjectInspectorResults projectResults) {
        return new DoubleMetricValue(getName(), getCode(), wcc.compute(projectResults).getValue());
    }
}
