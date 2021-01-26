package org.name.tool.core.metrics;

import org.name.tool.core.metrics.api.SecurityMetric;
import org.name.tool.core.metrics.ca.ClassifiedAttributes;
import org.name.tool.core.metrics.ca.ClassifiedAttributesCached;
import org.name.tool.core.metrics.civa.ClassifiedInstanceVariablesAccessibility;
import org.name.tool.core.metrics.civa.ClassifiedInstanceVariablesAccessibilityCached;
import org.name.tool.core.metrics.cm.ClassifiedMethods;
import org.name.tool.core.metrics.cm.ClassifiedMethodsCached;

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
                case ClassifiedAttributes.CODE:
                    securityMetric = metricsStructure.getCa();
                    break;
                case ClassifiedMethods.CODE:
                    securityMetric = metricsStructure.getCm();
                    break;
                case ClassifiedInstanceVariablesAccessibility.CODE:
                    securityMetric = metricsStructure.getCiva();
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
        private final ClassifiedAttributes ca;
        private final ClassifiedMethods cm;
        private final ClassifiedInstanceVariablesAccessibilityCached civa;

        public MetricsStructure() {
            // Create here all existing metrics and compose them
            this.ca = new ClassifiedAttributesCached();
            this.cm = new ClassifiedMethodsCached();
            this.civa = new ClassifiedInstanceVariablesAccessibilityCached(ca);
        }

        public ClassifiedAttributes getCa() {
            return ca;
        }

        public ClassifiedMethods getCm() {
            return cm;
        }

        public ClassifiedInstanceVariablesAccessibilityCached getCiva() {
            return civa;
        }
    }
}
