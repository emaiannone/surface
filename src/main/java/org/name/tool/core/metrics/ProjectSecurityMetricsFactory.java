package org.name.tool.core.metrics;

import org.name.tool.core.metrics.api.ProjectSecurityMetric;
import org.name.tool.core.metrics.cc.CC;
import org.name.tool.core.metrics.cc.CCCached;

import java.util.ArrayList;
import java.util.List;

public class ProjectSecurityMetricsFactory {
    private final ProjectSecurityMetricsFactory.MetricsStructure metricsStructure;

    public ProjectSecurityMetricsFactory() {
        this.metricsStructure = new ProjectSecurityMetricsFactory.MetricsStructure();
    }

    public List<ProjectSecurityMetric<?>> getSecurityMetrics(String[] metricsCodes) {
        List<ProjectSecurityMetric<?>> securityMetrics = new ArrayList<>();
        for (String metricCode : metricsCodes) {
            ProjectSecurityMetric<?> securityMetric = null;
            switch (metricCode) {
                case CC.CODE:
                    securityMetric = metricsStructure.getCc();
                    break;
                // Add other metrics here
            }
            if (securityMetric != null) {
                securityMetrics.add(securityMetric);
            }
        }
        return securityMetrics;
    }

    private static class MetricsStructure {
        private final CC cc;

        public MetricsStructure() {
            // Create here all existing metrics and compose them
            this.cc = new CCCached();
        }

        public CC getCc() {
            return cc;
        }
    }
}
