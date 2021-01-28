package org.name.tool.core.metrics;

import org.name.tool.core.metrics.api.ProjectSecurityMetric;
import org.name.tool.core.metrics.projectlevel.cc.CC;
import org.name.tool.core.metrics.projectlevel.cc.CCCached;
import org.name.tool.core.metrics.projectlevel.cce.CCE;
import org.name.tool.core.metrics.projectlevel.cce.CCECached;

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
                case CCE.CODE:
                    securityMetric = metricsStructure.getCce();
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
        private final CCE cce;

        public MetricsStructure() {
            // Create here all existing metrics and compose them
            this.cc = new CCCached();
            this.cce = new CCECached(cc);
        }

        public CC getCc() {
            return cc;
        }

        public CCE getCce() {
            return cce;
        }
    }
}
