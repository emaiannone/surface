package org.surface.surface.core.metrics;

import org.surface.surface.core.metrics.api.ClassMetric;
import org.surface.surface.core.metrics.classlevel.ca.CA;
import org.surface.surface.core.metrics.classlevel.ca.CACached;
import org.surface.surface.core.metrics.classlevel.cai.CAI;
import org.surface.surface.core.metrics.classlevel.cai.CAICached;
import org.surface.surface.core.metrics.classlevel.ccva.CCVA;
import org.surface.surface.core.metrics.classlevel.ccva.CCVACached;
import org.surface.surface.core.metrics.classlevel.civa.CIVA;
import org.surface.surface.core.metrics.classlevel.civa.CIVACached;
import org.surface.surface.core.metrics.classlevel.cm.CM;
import org.surface.surface.core.metrics.classlevel.cm.CMCached;
import org.surface.surface.core.metrics.classlevel.cma.CMA;
import org.surface.surface.core.metrics.classlevel.cma.CMACached;
import org.surface.surface.core.metrics.classlevel.cmr.CMR;
import org.surface.surface.core.metrics.classlevel.cmr.CMRCached;
import org.surface.surface.core.metrics.classlevel.rp.RP;
import org.surface.surface.core.metrics.classlevel.rp.RPCached;

import java.util.ArrayList;
import java.util.List;

public class ClassMetricsFactory implements MetricsFactory<ClassMetric<?>> {
    private final MetricsStructure metricsStructure;

    public ClassMetricsFactory() {
        this.metricsStructure = new MetricsStructure();
    }

    public List<ClassMetric<?>> getMetrics(List<String> metricsCodes) {
        List<ClassMetric<?>> classMetrics = new ArrayList<>();
        // NOTE Any new metric must be added here to be supported
        for (String metricCode : metricsCodes) {
            ClassMetric<?> classMetric = null;
            switch (metricCode) {
                case CA.CODE:
                    classMetric = metricsStructure.getCa();
                    break;
                case CM.CODE:
                    classMetric = metricsStructure.getCm();
                    break;
                case RP.CODE:
                    classMetric = metricsStructure.getRp();
                    break;
                case CIVA.CODE:
                    classMetric = metricsStructure.getCiva();
                    break;
                case CCVA.CODE:
                    classMetric = metricsStructure.getCcva();
                    break;
                case CMA.CODE:
                    classMetric = metricsStructure.getCma();
                    break;
                case CMR.CODE:
                    classMetric = metricsStructure.getCmr();
                    break;
                case CAI.CODE:
                    classMetric = metricsStructure.getCai();
                    break;
            }
            if (classMetric != null) {
                classMetrics.add(classMetric);
            }
        }
        return classMetrics;
    }

    private static class MetricsStructure {
        private final CA ca;
        private final CM cm;
        private final RP rp;
        private final CIVA civa;
        private final CCVA ccva;
        private final CMA cma;
        private final CMR cmr;
        private final CAI cai;

        MetricsStructure() {
            // Create here all existing metrics and compose them
            this.ca = new CACached();
            this.cm = new CMCached();
            this.rp = new RPCached();
            this.civa = new CIVACached(ca);
            this.ccva = new CCVACached(ca);
            this.cma = new CMACached(cm);
            this.cmr = new CMRCached(cm);
            this.cai = new CAICached(ca);
        }

        CA getCa() {
            return ca;
        }

        CM getCm() {
            return cm;
        }

        RP getRp() {
            return rp;
        }

        CIVA getCiva() {
            return civa;
        }

        CCVA getCcva() {
            return ccva;
        }

        CMA getCma() {
            return cma;
        }

        CMR getCmr() {
            return cmr;
        }

        CAI getCai() {
            return cai;
        }
    }
}
