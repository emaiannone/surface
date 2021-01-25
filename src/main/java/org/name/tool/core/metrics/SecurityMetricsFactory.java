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
    //TODO Move this metrics structure in a separate class
    private final ClassifiedAttributes ca;
    private final ClassifiedMethods cm;
    private final ClassifiedInstanceVariablesAccessibilityCached civa;

    public SecurityMetricsFactory() {
        // Create here all existing metrics and compose them
        this.ca = new ClassifiedAttributesCached();
        this.cm = new ClassifiedMethodsCached();
        this.civa = new ClassifiedInstanceVariablesAccessibilityCached(ca);
    }

    public List<SecurityMetric> getSecurityMetrics(String[] metricsCodes) {
        List<SecurityMetric> securityMetrics = new ArrayList<>();
        for (String metricCode : metricsCodes) {
            SecurityMetric securityMetric = null;
            switch (metricCode) {
                case ClassifiedAttributes.CODE:
                    securityMetric = ca;
                    break;
                case ClassifiedMethods.CODE:
                    securityMetric = cm;
                    break;
                case ClassifiedInstanceVariablesAccessibility.CODE:
                    securityMetric = civa;
                    break;
                // Add other metrics here
            }
            if (securityMetric != null) {
                securityMetrics.add(securityMetric);
            }
        }
        return securityMetrics;
    }
}
