package org.surface.surface.core.engine.metrics.project.tsi;

import org.surface.surface.core.engine.inspection.results.ProjectInspectorResults;
import org.surface.surface.core.engine.metrics.project.pem.PEM;
import org.surface.surface.core.engine.metrics.project.pfsd.PFSD;
import org.surface.surface.core.engine.metrics.project.pi.PI;
import org.surface.surface.core.engine.metrics.project.plcm.PLCM;
import org.surface.surface.core.engine.metrics.project.plp.PLP;
import org.surface.surface.core.engine.metrics.project.pras.PRAS;
import org.surface.surface.core.engine.metrics.project.pswl.PSWL;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;
import org.surface.surface.core.engine.metrics.results.values.MetricValue;

import java.util.stream.Stream;

public class TSIImpl extends TSI {
    private final PLP plp;
    private final PRAS pras;
    private final PSWL pswl;
    private final PFSD pfsd;
    private final PLCM plcm;
    private final PI pi;
    private final PEM pem;

    public TSIImpl(PLP plp, PRAS pras, PSWL pswl, PFSD pfsd, PLCM plcm, PI pi, PEM pem) {
        this.plp = plp;
        this.pras = pras;
        this.pswl = pswl;
        this.pfsd = pfsd;
        this.plcm = plcm;
        this.pi = pi;
        this.pem = pem;
    }

    @Override
    public DoubleMetricValue compute(ProjectInspectorResults projectResults) {
        double value = Stream.of(plp.compute(projectResults), pras.compute(projectResults),
                        pswl.compute(projectResults), pfsd.compute(projectResults),
                        plcm.compute(projectResults), pi.compute(projectResults), pem.compute(projectResults))
                .mapToDouble(MetricValue::getValue)
                .sum();
        return new DoubleMetricValue(getName(), getCode(), value);
    }
}
