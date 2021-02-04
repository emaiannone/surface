package org.name.tool.core.metrics.projectlevel.cscr;

import org.name.tool.results.ProjectAnalyzerResults;
import org.name.tool.results.values.DoubleMetricValue;

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
