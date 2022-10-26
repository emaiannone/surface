package org.surface.surface.core.engine.metrics.project.pfsd;

import org.surface.surface.core.engine.inspection.results.ProjectInspectorResults;
import org.surface.surface.core.engine.metrics.project.rca.RCA;
import org.surface.surface.core.engine.metrics.project.rcm.RCM;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;
import org.surface.surface.core.engine.metrics.results.values.MetricValue;

import java.util.stream.Stream;

public class PFSDImpl extends PFSD {
    private final RCA rca;
    private final RCM rcm;

    public PFSDImpl(RCA rca, RCM rcm) {
        this.rca = rca;
        this.rcm = rcm;
    }

    @Override
    public DoubleMetricValue compute(ProjectInspectorResults projectResults) {
        double value = Stream.of(rca.compute(projectResults), rcm.compute(projectResults))
                .mapToDouble(MetricValue::getValue)
                .sum();
        return new DoubleMetricValue(getName(), getCode(), value);
    }
}
