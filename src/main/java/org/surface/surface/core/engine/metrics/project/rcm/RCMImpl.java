package org.surface.surface.core.engine.metrics.project.rcm;

import org.surface.surface.core.engine.inspection.results.ProjectInspectorResults;
import org.surface.surface.core.engine.metrics.project.cme.CME;
import org.surface.surface.core.engine.metrics.project.cmi.CMI;
import org.surface.surface.core.engine.metrics.project.coa.COA;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;
import org.surface.surface.core.engine.metrics.results.values.MetricValue;

import java.util.stream.Stream;

public class RCMImpl extends RCM {
    private final COA coa;
    private final CME cme;
    private final CMI cmi;

    public RCMImpl(COA coa, CME cme, CMI cmi) {
        this.coa = coa;
        this.cme = cme;
        this.cmi = cmi;
    }

    @Override
    public DoubleMetricValue compute(ProjectInspectorResults projectResults) {
        double value = Stream.of(coa.compute(projectResults), cme.compute(projectResults), cmi.compute(projectResults))
                .mapToDouble(MetricValue::getValue)
                .sum();
        return new DoubleMetricValue(getName(), getCode(), value);
    }
}
