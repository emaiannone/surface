package org.name.tool.core.metrics;

import org.name.tool.core.metrics.api.ClassMetric;
import org.name.tool.core.metrics.classlevel.ca.CA;
import org.name.tool.core.metrics.classlevel.ca.CACached;
import org.name.tool.core.metrics.classlevel.cai.CAI;
import org.name.tool.core.metrics.classlevel.cai.CAICached;
import org.name.tool.core.metrics.classlevel.ccva.CCVA;
import org.name.tool.core.metrics.classlevel.ccva.CCVACached;
import org.name.tool.core.metrics.classlevel.civa.CIVA;
import org.name.tool.core.metrics.classlevel.civa.CIVACached;
import org.name.tool.core.metrics.classlevel.cm.CM;
import org.name.tool.core.metrics.classlevel.cm.CMCached;
import org.name.tool.core.metrics.classlevel.cma.CMA;
import org.name.tool.core.metrics.classlevel.cma.CMACached;
import org.name.tool.core.metrics.classlevel.cmr.CMR;
import org.name.tool.core.metrics.classlevel.cmr.CMRCached;
import org.name.tool.core.metrics.classlevel.rp.RP;
import org.name.tool.core.metrics.classlevel.rp.RPCached;

import java.util.ArrayList;
import java.util.List;

public class ClassMetricsFactory {
    private final MetricsStructure metricsStructure;

    public ClassMetricsFactory() {
        this.metricsStructure = new MetricsStructure();
    }

    public List<ClassMetric<?>> getMetrics(String[] metricsCodes) {
        List<ClassMetric<?>> classMetrics = new ArrayList<>();
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
                // Add other metrics here
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

        public MetricsStructure() {
            // Create here all existing metrics and compose them
            this.ca = new CACached();
            this.cm = new CMCached();
            this.rp = new RPCached();
            this.civa = new CIVACached(ca);
            this.ccva = new CCVACached(ca);
            this.cma = new CMACached(cm);
            this.cmr = new CMRCached(cm);
            this.cai = new CAICached(ca, cm);
        }

        public CA getCa() {
            return ca;
        }

        public CM getCm() {
            return cm;
        }

        public RP getRp() {
            return rp;
        }

        public CIVA getCiva() {
            return civa;
        }

        public CCVA getCcva() {
            return ccva;
        }

        public CMA getCma() {
            return cma;
        }

        public CMR getCmr() {
            return cmr;
        }

        public CAI getCai() {
            return cai;
        }
    }
}
