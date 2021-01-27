package org.name.tool.core.metrics;

import org.name.tool.core.metrics.api.SecurityMetric;
import org.name.tool.core.metrics.ca.CA;
import org.name.tool.core.metrics.ca.CACached;
import org.name.tool.core.metrics.ccva.CCVA;
import org.name.tool.core.metrics.ccva.CCVACached;
import org.name.tool.core.metrics.civa.CIVA;
import org.name.tool.core.metrics.civa.CIVACached;
import org.name.tool.core.metrics.cm.CM;
import org.name.tool.core.metrics.cm.CMCached;
import org.name.tool.core.metrics.cma.CMA;
import org.name.tool.core.metrics.cma.CMACached;
import org.name.tool.core.metrics.rp.RP;
import org.name.tool.core.metrics.rp.RPCached;

import java.util.ArrayList;
import java.util.List;

public class SecurityMetricsFactory {
    private final MetricsStructure metricsStructure;

    public SecurityMetricsFactory() {
        this.metricsStructure = new MetricsStructure();
    }

    public List<SecurityMetric> getSecurityMetrics(String[] metricsCodes) {
        List<SecurityMetric> securityMetrics = new ArrayList<>();
        for (String metricCode : metricsCodes) {
            SecurityMetric securityMetric = null;
            switch (metricCode) {
                case CA.CODE:
                    securityMetric = metricsStructure.getCa();
                    break;
                case CM.CODE:
                    securityMetric = metricsStructure.getCm();
                    break;
                case RP.CODE:
                    securityMetric = metricsStructure.getRp();
                    break;
                case CIVA.CODE:
                    securityMetric = metricsStructure.getCiva();
                    break;
                case CCVA.CODE:
                    securityMetric = metricsStructure.getCcva();
                    break;
                case CMA.CODE:
                    securityMetric = metricsStructure.getCma();
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
        private final CA ca;
        private final CM cm;
        private final RP rp;
        private final CIVA civa;
        private final CCVA ccva;
        private final CMA cma;

        public MetricsStructure() {
            // Create here all existing metrics and compose them
            this.ca = new CACached();
            this.cm = new CMCached();
            this.rp = new RPCached();
            this.civa = new CIVACached(ca);
            this.ccva = new CCVACached(ca);
            this.cma = new CMACached(cm);
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
    }
}
