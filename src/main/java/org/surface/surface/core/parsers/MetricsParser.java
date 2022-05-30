package org.surface.surface.core.parsers;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.surface.surface.core.metrics.classlevel.ca.CA;
import org.surface.surface.core.metrics.classlevel.cai.CAI;
import org.surface.surface.core.metrics.classlevel.civa.CIVA;
import org.surface.surface.core.metrics.classlevel.cm.CM;
import org.surface.surface.core.metrics.classlevel.cma.CMA;
import org.surface.surface.core.metrics.classlevel.cmr.CMR;
import org.surface.surface.core.metrics.classlevel.rp.RP;
import org.surface.surface.core.metrics.projectlevel.cc.CC;
import org.surface.surface.core.metrics.projectlevel.cce.CCE;
import org.surface.surface.core.metrics.projectlevel.ccr.CCR;
import org.surface.surface.core.metrics.projectlevel.cme.CME;
import org.surface.surface.core.metrics.projectlevel.cscr.CSCR;
import org.surface.surface.core.metrics.projectlevel.sccr.SCCR;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class MetricsParser {

    private static final String ALL = "ALL";
    private static final List<Pair<String, String>> supportedMetrics;

    static {
        // NOTE Any new metric must be added here to be recognized by the CLI parser
        supportedMetrics = new ArrayList<>();
        supportedMetrics.add(new ImmutablePair<>(CA.CODE, CA.NAME));
        supportedMetrics.add(new ImmutablePair<>(CM.CODE, CM.NAME));
        supportedMetrics.add(new ImmutablePair<>(CC.CODE, CC.NAME));
        supportedMetrics.add(new ImmutablePair<>(CIVA.CODE, CIVA.NAME));
        supportedMetrics.add(new ImmutablePair<>(CMA.CODE, CMA.NAME));
        supportedMetrics.add(new ImmutablePair<>(RP.CODE, RP.NAME));
        supportedMetrics.add(new ImmutablePair<>(CAI.CODE, CAI.NAME));
        supportedMetrics.add(new ImmutablePair<>(CMR.CODE, CMR.NAME));
        supportedMetrics.add(new ImmutablePair<>(CCR.CODE, CCR.NAME));
        supportedMetrics.add(new ImmutablePair<>(CSCR.CODE, CSCR.NAME));
        supportedMetrics.add(new ImmutablePair<>(CCE.CODE, CCE.NAME));
        supportedMetrics.add(new ImmutablePair<>(CME.CODE, CME.NAME));
        supportedMetrics.add(new ImmutablePair<>(SCCR.CODE, SCCR.NAME));
    }

    public static List<String> parseMetricsString(String[] metricsString) {
        if (metricsString == null || metricsString.length == 0 || metricsString[0].equals("")) {
            throw new IllegalArgumentException("The input metrics formula must not be null, empty, or with an empty string as its only element.");
        }
        Set<String> selectedMetrics = new LinkedHashSet<>();
        List<String> supportedCodes = supportedMetrics.stream().map(Pair::getKey).collect(Collectors.toList());
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
        return new ArrayList<>(selectedMetrics);
    }
}
