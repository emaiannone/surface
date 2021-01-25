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
    public SecurityMetricsFactory() {
    }

    public List<SecurityMetric> getSecurityMetrics(String[] metricsCodes) {
        List<SecurityMetric> securityMetrics = new ArrayList<>();
        ClassifiedAttributes ca = new ClassifiedAttributesCached();
        ClassifiedMethods cm = new ClassifiedMethodsCached();
        for (String metricCode : metricsCodes) {
            // TODO IMPORTANT: Is there a smarted way to compose metrics? Maybe it is better to
            //  prebuild the whole metrics structure and then, depending on the input, return the proper objects
            SecurityMetric securityMetric = null;
            switch (metricCode) {
                case ClassifiedAttributes.CODE:
                    securityMetric = ca;
                    break;
                case ClassifiedMethods.CODE:
                    securityMetric = cm;
                    break;
                case ClassifiedInstanceVariablesAccessibility.CODE:
                    securityMetric = new ClassifiedInstanceVariablesAccessibilityCached(ca);
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
