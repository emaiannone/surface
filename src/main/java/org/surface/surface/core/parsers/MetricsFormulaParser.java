package org.surface.surface.core.parsers;

import org.surface.surface.core.metrics.api.Metric;
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
import org.surface.surface.core.metrics.projectlevel.cc.CC;
import org.surface.surface.core.metrics.projectlevel.cc.CCCached;
import org.surface.surface.core.metrics.projectlevel.cce.CCE;
import org.surface.surface.core.metrics.projectlevel.cce.CCECached;
import org.surface.surface.core.metrics.projectlevel.ccr.CCR;
import org.surface.surface.core.metrics.projectlevel.ccr.CCRCached;
import org.surface.surface.core.metrics.projectlevel.cme.CME;
import org.surface.surface.core.metrics.projectlevel.cme.CMECached;
import org.surface.surface.core.metrics.projectlevel.cscr.CSCR;
import org.surface.surface.core.metrics.projectlevel.cscr.CSCRCached;
import org.surface.surface.core.metrics.projectlevel.sccr.SCCR;
import org.surface.surface.core.metrics.projectlevel.sccr.SCCRCached;

import java.util.*;

public class MetricsFormulaParser {
    private static final String ALL = "ALL";
    private static final Map<String, Metric<?, ?>> ALL_METRICS;
    private static final ProjectMetricsStructure PROJECT_METRICS_STRUCTURE;
    private static final ClassMetricsStructure CLASS_METRICS_STRUCTURE;

    static {
        PROJECT_METRICS_STRUCTURE = new ProjectMetricsStructure();
        CLASS_METRICS_STRUCTURE = new ClassMetricsStructure();
        // NOTE Any new metric must be added here to be recognized by the CLI parser
        ALL_METRICS = new HashMap<>();
        ALL_METRICS.put(CA.CODE, CLASS_METRICS_STRUCTURE.getCa());
        ALL_METRICS.put(CM.CODE, CLASS_METRICS_STRUCTURE.getCm());
        ALL_METRICS.put(CC.CODE, PROJECT_METRICS_STRUCTURE.getCc());
        ALL_METRICS.put(CIVA.CODE, CLASS_METRICS_STRUCTURE.getCiva());
        ALL_METRICS.put(CMA.CODE, CLASS_METRICS_STRUCTURE.getCma());
        ALL_METRICS.put(CCVA.CODE, CLASS_METRICS_STRUCTURE.getCcva());
        ALL_METRICS.put(RP.CODE, CLASS_METRICS_STRUCTURE.getRp());
        ALL_METRICS.put(CAI.CODE, CLASS_METRICS_STRUCTURE.getCai());
        ALL_METRICS.put(CMR.CODE, CLASS_METRICS_STRUCTURE.getCmr());
        ALL_METRICS.put(CCR.CODE, PROJECT_METRICS_STRUCTURE.getCcr());
        ALL_METRICS.put(CSCR.CODE, PROJECT_METRICS_STRUCTURE.getCscr());
        ALL_METRICS.put(CCE.CODE, PROJECT_METRICS_STRUCTURE.getCce());
        ALL_METRICS.put(CME.CODE, PROJECT_METRICS_STRUCTURE.getCme());
        ALL_METRICS.put(SCCR.CODE, PROJECT_METRICS_STRUCTURE.getSccr());
    }

    public static List<Metric<?, ?>> parseMetricsFormula(String[] metricsString) {
        if (metricsString == null || metricsString.length == 0 || metricsString[0].equals("")) {
            throw new IllegalArgumentException("The input metrics formula must not be null, empty, or with an empty string as its only element.");
        }
        Set<String> selectedMetrics = new LinkedHashSet<>();
        List<String> supportedCodes = new ArrayList<>(ALL_METRICS.keySet());
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
        List<Metric<?, ?>> metrics = new ArrayList<>();
        for (String metricsCode : metricsCodes) {
            metrics.add(ALL_METRICS.get(metricsCode));
        }
        return metrics;
    }

    private static class ProjectMetricsStructure {
        private final CC cc;
        private final CCR ccr;
        private final SCCR sccr;
        private final CCE cce;
        private final CME cme;
        private final CSCR cscr;

        ProjectMetricsStructure() {
            // NOTE Create here all existing metrics and compose them
            this.cc = new CCCached();
            this.ccr = new CCRCached(cc);
            this.sccr = new SCCRCached(cc);
            this.cce = new CCECached(cc);
            this.cme = new CMECached();
            this.cscr = new CSCRCached();
        }

        CC getCc() {
            return cc;
        }

        CCR getCcr() {
            return ccr;
        }

        SCCR getSccr() {
            return sccr;
        }

        CCE getCce() {
            return cce;
        }

        CME getCme() {
            return cme;
        }

        CSCR getCscr() {
            return cscr;
        }
    }

    private static class ClassMetricsStructure {
        private final CA ca;
        private final CM cm;
        private final RP rp;
        private final CIVA civa;
        private final CCVA ccva;
        private final CMA cma;
        private final CMR cmr;
        private final CAI cai;

        ClassMetricsStructure() {
            // NOTE Create here all existing metrics and compose them
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
