package org.surface.surface.core.metrics;

import org.surface.surface.core.metrics.api.ProjectMetric;
import org.surface.surface.core.metrics.projectlevel.cc.CC;
import org.surface.surface.core.metrics.projectlevel.cc.CCCached;
import org.surface.surface.core.metrics.projectlevel.cce.CCE;
import org.surface.surface.core.metrics.projectlevel.cce.CCECached;
import org.surface.surface.core.metrics.projectlevel.ccr.CCR;
import org.surface.surface.core.metrics.projectlevel.ccr.CCRCached;
import org.surface.surface.core.metrics.projectlevel.cme.CME;
import org.surface.surface.core.metrics.projectlevel.cme.CMECached;
import org.surface.surface.core.metrics.projectlevel.cscr.CSCR;
import org.surface.surface.core.metrics.projectlevel.cscr.CSCRCached;
import org.surface.surface.core.metrics.projectlevel.sccr.SCCR;
import org.surface.surface.core.metrics.projectlevel.sccr.SCCRCached;

import java.util.ArrayList;
import java.util.List;

public class ProjectMetricsFactory implements MetricsFactory<ProjectMetric<?>> {
    private final MetricsStructure metricsStructure;

    public ProjectMetricsFactory() {
        this.metricsStructure = new MetricsStructure();
    }

    public List<ProjectMetric<?>> getMetrics(List<String> metricsCodes) {
        List<ProjectMetric<?>> projectMetrics = new ArrayList<>();
        for (String metricCode : metricsCodes) {
            ProjectMetric<?> projectMetric = null;
            switch (metricCode) {
                case CC.CODE:
                    projectMetric = metricsStructure.getCc();
                    break;
                case CCR.CODE:
                    projectMetric = metricsStructure.getCcr();
                    break;
                case SCCR.CODE:
                    projectMetric = metricsStructure.getSccr();
                    break;
                case CCE.CODE:
                    projectMetric = metricsStructure.getCce();
                    break;
                case CME.CODE:
                    projectMetric = metricsStructure.getCme();
                    break;
                case CSCR.CODE:
                    projectMetric = metricsStructure.getCscr();
                    break;
                // NOTE Add other metrics here
            }
            if (projectMetric != null) {
                projectMetrics.add(projectMetric);
            }
        }
        return projectMetrics;
    }

    private static class MetricsStructure {
        private final CC cc;
        private final CCR ccr;
        private final SCCR sccr;
        private final CCE cce;
        private final CME cme;
        private final CSCR cscr;

        public MetricsStructure() {
            // Create here all existing metrics and compose them
            this.cc = new CCCached();
            this.ccr = new CCRCached(cc);
            this.sccr = new SCCRCached(cc);
            this.cce = new CCECached(cc);
            this.cme = new CMECached();
            this.cscr = new CSCRCached();
        }

        public CC getCc() {
            return cc;
        }

        public CCR getCcr() {
            return ccr;
        }

        public SCCR getSccr() {
            return sccr;
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
