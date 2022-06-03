package org.surface.surface.core.interpreters;

import org.surface.surface.core.metrics.api.MetricsManager;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class MetricsFormulaInterpreter {
    private static final String ALL = "ALL";

    public static MetricsManager interpretMetricsFormula(String metricsString, String sep) {
        return interpretMetricsFormula(metricsString.split(sep));
    }

    public static MetricsManager interpretMetricsFormula(String[] metricsString) {
        if (metricsString == null || metricsString.length == 0 || metricsString[0].equals("")) {
            throw new IllegalArgumentException("The input metrics formula must not be null, empty, or with an empty string as its only element.");
        }
        Set<String> selectedMetrics = new LinkedHashSet<>();

        List<String> supportedCodes = MetricsManager.getManagedMetricsCodes();
        for (String part : metricsString) {
            if (part == null || part.equals("")) {
                throw new IllegalArgumentException("The input metrics formula has an invalid code.");
            }
            if (part.equals(ALL)) {
                selectedMetrics.addAll(supportedCodes);
            } else {
                boolean toRemove;
                String code;
                if (part.charAt(0) == '-') {
                    code = part.substring(1);
                    toRemove = true;
                } else {
                    code = part;
                    toRemove = false;
                }
                // If the code is not supported, it is completely ignored, and no action is taken
                if (supportedCodes.contains(code)) {
                    if (selectedMetrics.contains(code) && toRemove) {
                        selectedMetrics.remove(code);
                    }
                    if (!selectedMetrics.contains(code) && !toRemove) {
                        selectedMetrics.add(code);
                    }
                }
            }
        }
        if (selectedMetrics.size() == 0) {
            throw new IllegalArgumentException("The input metrics formula resulted in an empty set of metrics.");
        }
        List<String> metricsCodes = new ArrayList<>(selectedMetrics);
        return new MetricsManager(metricsCodes);
    }
}
