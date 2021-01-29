package org.name.tool.core.metrics.projectlevel.cscr;

import org.name.tool.core.results.ProjectAnalyzerResults;
import org.name.tool.core.results.SecurityMetricResult;

import java.util.Map;

public class CSCRCached extends CSCR {
    private final CSCRImpl cscr;
    private SecurityMetricResult<Map<String, Double>> cachedResult;

    public CSCRCached() {
        this.cscr = new CSCRImpl();
        this.cachedResult = null;
    }

    @Override
    public SecurityMetricResult<Map<String, Double>> compute(ProjectAnalyzerResults projectResults) {
        if (cachedResult == null) {
            cachedResult = cscr.compute(projectResults);
        }
        return cachedResult;
    }
}
