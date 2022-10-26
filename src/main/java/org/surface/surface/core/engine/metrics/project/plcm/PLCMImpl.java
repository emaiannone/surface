package org.surface.surface.core.engine.metrics.project.plcm;

import org.surface.surface.core.engine.inspection.results.ProjectInspectorResults;
import org.surface.surface.core.engine.metrics.project.rcc.RCC;
import org.surface.surface.core.engine.metrics.project.wcc.WCC;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;
import org.surface.surface.core.engine.metrics.results.values.MetricValue;

import java.util.stream.Stream;

public class PLCMImpl extends PLCM {
    private final RCC rcc;
    private final WCC wcc;

    public PLCMImpl(RCC rcc, WCC wcc) {
        this.rcc = rcc;
        this.wcc = wcc;
    }

    @Override
    public DoubleMetricValue compute(ProjectInspectorResults projectResults) {
        double value = Stream.of(rcc.compute(projectResults), wcc.compute(projectResults))
                .mapToDouble(MetricValue::getValue)
                .sum();
        return new DoubleMetricValue(getName(), getCode(), value);
    }
}
