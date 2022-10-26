package org.surface.surface.core.engine.metrics.project.pswl;

import org.surface.surface.core.engine.inspection.results.ProjectInspectorResults;
import org.surface.surface.core.engine.metrics.project.wca.WCA;
import org.surface.surface.core.engine.metrics.project.wcm.WCM;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;
import org.surface.surface.core.engine.metrics.results.values.MetricValue;

import java.util.stream.Stream;

public class PSWLImpl extends PSWL {
    private final WCA wca;
    private final WCM wcm;

    public PSWLImpl(WCA wca, WCM wcm) {
        this.wca = wca;
        this.wcm = wcm;
    }

    @Override
    public DoubleMetricValue compute(ProjectInspectorResults projectResults) {
        double value = Stream.of(wca.compute(projectResults), wcm.compute(projectResults))
                .mapToDouble(MetricValue::getValue)
                .sum();
        return new DoubleMetricValue(getName(), getCode(), value);
    }
}
