package org.name.tool.core.metrics;

import org.name.tool.core.metrics.api.ProjectSecurityMetric;
import org.name.tool.core.metrics.projectlevel.cc.CC;
import org.name.tool.core.metrics.projectlevel.cc.CCCached;
import org.name.tool.core.metrics.projectlevel.cce.CCE;
import org.name.tool.core.metrics.projectlevel.cce.CCECached;
import org.name.tool.core.metrics.projectlevel.cme.CME;
import org.name.tool.core.metrics.projectlevel.cme.CMECached;
import org.name.tool.core.metrics.projectlevel.cscr.CSCR;
import org.name.tool.core.metrics.projectlevel.cscr.CSCRCached;

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
                case CME.CODE:
                    securityMetric = metricsStructure.getCme();
                    break;
                case CSCR.CODE:
                    securityMetric = metricsStructure.getCscr();
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
        private final CME cme;
        private final CSCR cscr;

        public MetricsStructure() {
            // Create here all existing metrics and compose them
            this.cc = new CCCached();
            this.cce = new CCECached(cc);
            this.cme = new CMECached();
            this.cscr = new CSCRCached();
        }

        public CC getCc() {
            return cc;
        }

        public CCE getCce() {
            return cce;
        }

        public CME getCme() {
            return cme;
        }

        public CSCR getCscr() {
            return cscr;
        }
    }
}
