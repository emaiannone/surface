package org.surface.surface.core.metrics.projectlevel.cscr;

import org.surface.surface.core.inspection.results.ProjectInspectorResults;
import org.surface.surface.core.metrics.results.values.DoubleMetricValue;

public class CSCRCached extends CSCR {
    private final CSCRImpl cscr;
    private DoubleMetricValue cachedResult;

    public CSCRCached() {
        this.cscr = new CSCRImpl();
        this.cachedResult = null;
    }

    @Override
    public DoubleMetricValue compute(ProjectInspectorResults projectResults) {
        if (cachedResult == null) {
            cachedResult = cscr.compute(projectResults);
        }
        return cachedResult;
    }
}
