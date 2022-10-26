package org.surface.surface.core.engine.metrics.project.rca;

import org.surface.surface.core.engine.inspection.results.ProjectInspectorResults;
import org.surface.surface.core.engine.metrics.project.cai.CAI;
import org.surface.surface.core.engine.metrics.project.ccda.CCDA;
import org.surface.surface.core.engine.metrics.project.cida.CIDA;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;
import org.surface.surface.core.engine.metrics.results.values.MetricValue;

import java.util.stream.Stream;

public class RCAImpl extends RCA {
    private final CIDA cida;
    private final CCDA ccda;
    private final CAI cai;

    public RCAImpl(CIDA cida, CCDA ccda, CAI cai) {
        this.cida = cida;
        this.ccda = ccda;
        this.cai = cai;
    }

    @Override
    public DoubleMetricValue compute(ProjectInspectorResults projectResults) {
        double value = Stream.of(cida.compute(projectResults), ccda.compute(projectResults), cai.compute(projectResults))
                .mapToDouble(MetricValue::getValue)
                .sum();
        return new DoubleMetricValue(getName(), getCode(), value);
    }
}
