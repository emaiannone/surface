package org.surface.surface.core.engine.metrics.project.wca;

import org.surface.surface.core.engine.inspection.results.ProjectInspectorResults;
import org.surface.surface.core.engine.metrics.project.caai.CAAI;
import org.surface.surface.core.engine.metrics.project.cmai.CMAI;
import org.surface.surface.core.engine.metrics.project.uaca.UACA;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;
import org.surface.surface.core.engine.metrics.results.values.MetricValue;

import java.util.stream.Stream;

public class WCAImpl extends WCA {
    private final CMAI cmai;
    private final CAAI caai;
    private final UACA uaca;

    public WCAImpl(CMAI cmai, CAAI caai, UACA uaca) {
        this.cmai = cmai;
        this.caai = caai;
        this.uaca = uaca;
    }

    @Override
    public DoubleMetricValue compute(ProjectInspectorResults projectResults) {
        double value = Stream.of(cmai.compute(projectResults), caai.compute(projectResults), uaca.compute(projectResults))
                .mapToDouble(MetricValue::getValue)
                .sum();
        return new DoubleMetricValue(getName(), getCode(), value);
    }
}
