package org.surface.surface.core.engine.metrics.project.pras;

import org.surface.surface.core.engine.inspection.results.ProjectInspectorResults;
import org.surface.surface.core.engine.metrics.project.rca.RCA;
import org.surface.surface.core.engine.metrics.project.rcc.RCC;
import org.surface.surface.core.engine.metrics.project.rcm.RCM;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;
import org.surface.surface.core.engine.metrics.results.values.MetricValue;

import java.util.stream.Stream;

public class PRASImpl extends PRAS {
    private final RCA rca;
    private final RCM rcm;
    private final RCC rcc;

    public PRASImpl(RCA rca, RCM rcm, RCC rcc) {
        this.rca = rca;
        this.rcm = rcm;
        this.rcc = rcc;
    }

    @Override
    public DoubleMetricValue compute(ProjectInspectorResults projectResults) {
        double value = Stream.of(rca.compute(projectResults), rcm.compute(projectResults), rcc.compute(projectResults))
                .mapToDouble(MetricValue::getValue)
                .sum();
        return new DoubleMetricValue(getName(), getCode(), value);
    }
}
