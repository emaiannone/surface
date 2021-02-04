package org.name.tool.core.metrics.projectlevel.cscr;

import org.name.tool.results.MetricValue;
import org.name.tool.results.ProjectAnalyzerResults;

public class CSCRCached extends CSCR {
    private final CSCRImpl cscr;
    private MetricValue<Double> cachedResult;

    public CSCRCached() {
        this.cscr = new CSCRImpl();
        this.cachedResult = null;
    }

    @Override
    public MetricValue<Double> compute(ProjectAnalyzerResults projectResults) {
        if (cachedResult == null) {
            cachedResult = cscr.compute(projectResults);
        }
        return cachedResult;
    }
}
