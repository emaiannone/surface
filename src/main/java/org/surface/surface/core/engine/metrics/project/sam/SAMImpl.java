package org.surface.surface.core.engine.metrics.project.sam;

import org.surface.surface.core.engine.inspection.results.ProjectInspectorResults;
import org.surface.surface.core.engine.metrics.project.cat.CAT;
import org.surface.surface.core.engine.metrics.project.cct.CCT;
import org.surface.surface.core.engine.metrics.project.cmt.CMT;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;
import org.surface.surface.core.engine.metrics.results.values.MetricValue;

import java.util.stream.Stream;

public class SAMImpl extends SAM {
    private final CAT cat;
    private final CMT cmt;
    private final CCT cct;

    public SAMImpl(CAT cat, CMT cmt, CCT cct) {
        this.cat = cat;
        this.cmt = cmt;
        this.cct = cct;
    }

    @Override
    public DoubleMetricValue compute(ProjectInspectorResults projectResults) {
        double value = Stream.of(cat.compute(projectResults), cmt.compute(projectResults), cct.compute(projectResults))
                .mapToDouble(MetricValue::getValue)
                .sum();
        return new DoubleMetricValue(getName(), getCode(), value);
    }
}
