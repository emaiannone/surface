package org.surface.surface.core.engine.metrics.project.rcc;

import org.surface.surface.core.engine.inspection.results.ProjectInspectorResults;
import org.surface.surface.core.engine.metrics.project.cce.CCE;
import org.surface.surface.core.engine.metrics.project.cdp.CDP;
import org.surface.surface.core.engine.metrics.project.cpcc.CPCC;
import org.surface.surface.core.engine.metrics.project.csp.CSP;
import org.surface.surface.core.engine.metrics.project.rpb.RPB;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;
import org.surface.surface.core.engine.metrics.results.values.MetricValue;

import java.util.stream.DoubleStream;
import java.util.stream.Stream;

public class RCCImpl extends RCC {
    private final RPB rpb;
    private final CPCC cpcc;
    private final CCE cce;
    private final CDP cdp;
    private final CSP csp;

    public RCCImpl(RPB rpb, CPCC cpcc, CCE cce, CDP cdp, CSP csp) {
        this.rpb = rpb;
        this.cpcc = cpcc;
        this.cce = cce;
        this.cdp = cdp;
        this.csp = csp;
    }

    @Override
    public DoubleMetricValue compute(ProjectInspectorResults projectResults) {
        double value = DoubleStream.concat(
                DoubleStream.of(rpb.compute(projectResults).getValue() ? 1.0 : 0.0),
                Stream.of(cpcc.compute(projectResults), cce.compute(projectResults), cdp.compute(projectResults), csp.compute(projectResults))
                        .mapToDouble(MetricValue::getValue)).sum();
        return new DoubleMetricValue(getName(), getCode(), value);
    }
}
