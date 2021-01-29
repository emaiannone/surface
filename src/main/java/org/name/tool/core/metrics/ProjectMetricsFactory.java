package org.name.tool.core.metrics;

import org.name.tool.core.metrics.api.ProjectMetric;
import org.name.tool.core.metrics.projectlevel.cc.CC;
import org.name.tool.core.metrics.projectlevel.cc.CCCached;
import org.name.tool.core.metrics.projectlevel.cce.CCE;
import org.name.tool.core.metrics.projectlevel.cce.CCECached;
import org.name.tool.core.metrics.projectlevel.ccr.CCR;
import org.name.tool.core.metrics.projectlevel.ccr.CCRCached;
import org.name.tool.core.metrics.projectlevel.cme.CME;
import org.name.tool.core.metrics.projectlevel.cme.CMECached;
import org.name.tool.core.metrics.projectlevel.cscr.CSCR;
import org.name.tool.core.metrics.projectlevel.cscr.CSCRCached;

import java.util.ArrayList;
import java.util.List;

public class ProjectMetricsFactory {
    private final ProjectMetricsFactory.MetricsStructure metricsStructure;

    public ProjectMetricsFactory() {
        this.metricsStructure = new ProjectMetricsFactory.MetricsStructure();
    }

    public List<ProjectMetric<?>> getMetrics(String[] metricsCodes) {
        List<ProjectMetric<?>> projectMetrics = new ArrayList<>();
        for (String metricCode : metricsCodes) {
            ProjectMetric<?> projectMetric = null;
            switch (metricCode) {
                case CC.CODE:
                    projectMetric = metricsStructure.getCc();
                    break;
                case CCE.CODE:
                    projectMetric = metricsStructure.getCce();
                    break;
                case CCR.CODE:
                    projectMetric = metricsStructure.getCcr();
                    break;
                case CME.CODE:
                    projectMetric = metricsStructure.getCme();
                    break;
                case CSCR.CODE:
                    projectMetric = metricsStructure.getCscr();
                    break;
                // Add other metrics here
            }
            if (projectMetric != null) {
                projectMetrics.add(projectMetric);
            }
        }
        return projectMetrics;
    }

    private static class MetricsStructure {
        private final CC cc;
        private final CCE cce;
        private final CCR ccr;
        private final CME cme;
        private final CSCR cscr;

        public MetricsStructure() {
            // Create here all existing metrics and compose them
            this.cc = new CCCached();
            this.cce = new CCECached(cc);
            this.ccr = new CCRCached(cc);
            this.cme = new CMECached();
            this.cscr = new CSCRCached();
        }

        public CC getCc() {
            return cc;
        }

        public CCE getCce() {
            return cce;
        }

        public CCR getCcr() {
            return ccr;
        }

        public CME getCme() {
            return cme;
        }

        public CSCR getCscr() {
            return cscr;
        }
    }
}
