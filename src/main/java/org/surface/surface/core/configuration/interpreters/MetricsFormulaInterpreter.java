package org.surface.surface.core.configuration.interpreters;

import org.surface.surface.core.engine.metrics.api.MetricsManager;

import java.util.*;

public class MetricsFormulaInterpreter implements InputStringInterpreter<MetricsManager> {
    private static final String ALL = "ALL";
    private static final String[] SEPS = {","};

    public MetricsManager interpret(String[] inputArray) {
        return interpret(String.join(SEPS[0], inputArray));
    }

    public MetricsManager interpret(String inputString) {
        if (inputString == null || inputString.isEmpty()) {
            throw new IllegalArgumentException("The input metrics formula must not be null or empty.");
        }
        String[] formulaParts = null;
        for (String sep : SEPS) {
            formulaParts = inputString.split(sep);
            // If the splitting failed
            if (formulaParts[0].equals(inputString)) {
                continue;
            }
            break;
        }
        // Drop any empty or null parts
        String[] cleanFormulaParts = Arrays.stream(formulaParts).filter(Objects::nonNull).filter(p -> !p.isEmpty()).toArray(String[]::new);
        Set<String> selectedMetrics = new LinkedHashSet<>();
        List<String> supportedCodes = MetricsManager.getAllSupportedMetrics();
        for (String part : cleanFormulaParts) {
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
        List<String> metricsCodes = new ArrayList<>(selectedMetrics);
        return new MetricsManager(metricsCodes);
    }
}
