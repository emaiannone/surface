package org.surface.surface.core.engine.metrics.project.pem;

import org.surface.surface.core.engine.inspection.results.ProjectInspectorResults;
import org.surface.surface.core.engine.metrics.project.sam.SAM;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;

public class PEMImpl extends PEM {
    private final SAM sam;

    public PEMImpl(SAM sam) {
        this.sam = sam;
    }

    @Override
    public DoubleMetricValue compute(ProjectInspectorResults projectResults) {
        return new DoubleMetricValue(getName(), getCode(), sam.compute(projectResults).getValue());
    }
}
