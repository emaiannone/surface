package org.surface.surface.core.engine.metrics.project.sam;

import org.surface.surface.core.engine.inspection.results.ProjectInspectorResults;
import org.surface.surface.core.engine.metrics.project.cat.CAT;
import org.surface.surface.core.engine.metrics.project.cct.CCT;
import org.surface.surface.core.engine.metrics.project.cmt.CMT;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;

public class SAMCached extends SAM {
    private final SAMImpl sam;
    private DoubleMetricValue cachedResult;

    public SAMCached(CAT cat, CMT cmt, CCT cct) {
        this.sam = new SAMImpl(cat, cmt, cct);
        this.cachedResult = null;
    }

    @Override
    public DoubleMetricValue compute(ProjectInspectorResults projectResult) {
        if (cachedResult == null) {
            cachedResult = sam.compute(projectResult);
        }
        return cachedResult;
    }
}
