package org.surface.surface.core.metrics.projectlevel.cscr;

import org.surface.surface.results.ProjectAnalyzerResults;
import org.surface.surface.results.values.DoubleMetricValue;

public class CSCRCached extends CSCR {
    private final CSCRImpl cscr;
    private DoubleMetricValue cachedResult;

    public CSCRCached() {
        this.cscr = new CSCRImpl();
        this.cachedResult = null;
    }

    @Override
    public DoubleMetricValue compute(ProjectAnalyzerResults projectResults) {
        if (cachedResult == null) {
            cachedResult = cscr.compute(projectResults);
        }
        return cachedResult;
    }
}
