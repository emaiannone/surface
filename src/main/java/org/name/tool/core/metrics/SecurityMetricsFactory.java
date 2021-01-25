package org.name.tool.core.metrics;

import org.name.tool.core.metrics.api.SecurityMetric;
import org.name.tool.core.metrics.ca.ClassifiedAttributes;
import org.name.tool.core.metrics.ca.ClassifiedAttributesCached;
import org.name.tool.core.metrics.cm.ClassifiedMethods;
import org.name.tool.core.metrics.cm.ClassifiedMethodsCached;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class SecurityMetricsFactory {
    public SecurityMetricsFactory() {
    }

    public List<SecurityMetric> getSecurityMetrics(String[] metricsCodes) {
        return Arrays
                .stream(metricsCodes)
                .map(this::getSecurityMetric)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public SecurityMetric getSecurityMetric(String metricCode) {
        // TODO How to properly compose Indirect Metrics? (if any)
        switch (metricCode) {
            case ClassifiedAttributes.CODE:
                return new ClassifiedAttributesCached();
            case ClassifiedMethods.CODE:
                return new ClassifiedMethodsCached();
            // Add other metrics here
            default:
                return null;
        }
    }

}
