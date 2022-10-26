package org.surface.surface.core.engine.metrics.project.plp;

import org.surface.surface.core.engine.inspection.results.ProjectInspectorResults;
import org.surface.surface.core.engine.metrics.project.wca.WCA;
import org.surface.surface.core.engine.metrics.project.wcc.WCC;
import org.surface.surface.core.engine.metrics.project.wcm.WCM;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;
import org.surface.surface.core.engine.metrics.results.values.MetricValue;

import java.util.stream.Stream;

public class PLPImpl extends PLP {
    private final WCA wca;
    private final WCM wcm;
    private final WCC wcc;

    public PLPImpl(WCA wca, WCM wcm, WCC wcc) {
        this.wca = wca;
        this.wcm = wcm;
        this.wcc = wcc;
    }

    @Override
    public DoubleMetricValue compute(ProjectInspectorResults projectResults) {
        double value = Stream.of(wca.compute(projectResults), wcm.compute(projectResults), wcc.compute(projectResults))
                .mapToDouble(MetricValue::getValue)
                .sum();
        return new DoubleMetricValue(getName(), getCode(), value);
    }
}
