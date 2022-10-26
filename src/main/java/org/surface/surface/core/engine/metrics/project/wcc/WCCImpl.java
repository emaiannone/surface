package org.surface.surface.core.engine.metrics.project.wcc;

import org.surface.surface.core.engine.inspection.results.ProjectInspectorResults;
import org.surface.surface.core.engine.metrics.project.ccc.CCC;
import org.surface.surface.core.engine.metrics.project.cscp.CSCP;
import org.surface.surface.core.engine.metrics.project.csi.CSI;
import org.surface.surface.core.engine.metrics.project.ucac.UCAC;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;
import org.surface.surface.core.engine.metrics.results.values.MetricValue;

import java.util.stream.Stream;

public class WCCImpl extends WCC {
    private final CCC ccc;
    private final UCAC ucac;
    private final CSCP cscp;
    private final CSI csi;

    public WCCImpl(CCC ccc, UCAC ucac, CSCP cscp, CSI csi) {
        this.ccc = ccc;
        this.ucac = ucac;
        this.cscp = cscp;
        this.csi = csi;
    }

    @Override
    public DoubleMetricValue compute(ProjectInspectorResults projectResults) {
        double value = Stream.of(ccc.compute(projectResults), ucac.compute(projectResults), cscp.compute(projectResults), csi.compute(projectResults))
                .mapToDouble(MetricValue::getValue)
                .sum();
        return new DoubleMetricValue(getName(), getCode(), value);
    }
}
