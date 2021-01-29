package org.name.tool.core.metrics.projectlevel.cscr;

import org.name.tool.core.results.MetricResult;
import org.name.tool.core.results.ProjectAnalyzerResults;

import java.util.Map;

public class CSCRCached extends CSCR {
    private final CSCRImpl cscr;
    private MetricResult<Map<String, Double>> cachedResult;

    public CSCRCached() {
        this.cscr = new CSCRImpl();
        this.cachedResult = null;
    }

    @Override
    public MetricResult<Map<String, Double>> compute(ProjectAnalyzerResults projectResults) {
        if (cachedResult == null) {
            cachedResult = cscr.compute(projectResults);
        }
        return cachedResult;
    }
}
