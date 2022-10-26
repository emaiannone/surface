package org.surface.surface.core.engine.metrics.project.wcm;

import org.surface.surface.core.engine.inspection.results.ProjectInspectorResults;
import org.surface.surface.core.engine.metrics.project.caiw.CAIW;
import org.surface.surface.core.engine.metrics.project.cmw.CMW;
import org.surface.surface.core.engine.metrics.project.cwmp.CWMP;
import org.surface.surface.core.engine.metrics.project.ucam.UCAM;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;
import org.surface.surface.core.engine.metrics.results.values.MetricValue;

import java.util.stream.Stream;

public class WCMImpl extends WCM {
    private final CAIW caiw;
    private final CMW cmw;
    private final CWMP cwmp;
    private final UCAM ucam;

    public WCMImpl(CAIW caiw, CMW cmw, CWMP cwmp, UCAM ucam) {
        this.caiw = caiw;
        this.cmw = cmw;
        this.cwmp = cwmp;
        this.ucam = ucam;
    }

    @Override
    public DoubleMetricValue compute(ProjectInspectorResults projectResults) {
        double value = Stream.of(caiw.compute(projectResults), cmw.compute(projectResults), cwmp.compute(projectResults), ucam.compute(projectResults))
                .mapToDouble(MetricValue::getValue)
                .sum();
        return new DoubleMetricValue(getName(), getCode(), value);
    }
}
