package org.surface.surface.core.metrics.api;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MetricsManager {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final Map<String, String> PROJECT_METRICS;
    private static final Map<String, String> CLASS_METRICS;

    static {
        // NOTE Any new metric must be added here to be recognized by the CLI parser
        PROJECT_METRICS = new LinkedHashMap<>();
        PROJECT_METRICS.put(CC.CODE, "getCc");
        PROJECT_METRICS.put(CCR.CODE, "getCcr");
        PROJECT_METRICS.put(CSCR.CODE, "getCscr");
        PROJECT_METRICS.put(CCE.CODE, "getCce");
        PROJECT_METRICS.put(CME.CODE, "getCme");
        PROJECT_METRICS.put(SCCR.CODE, "getSccr");
        CLASS_METRICS = new LinkedHashMap<>();
        CLASS_METRICS.put(CA.CODE, "getCa");
        CLASS_METRICS.put(CM.CODE, "getCm");
        CLASS_METRICS.put(CIVA.CODE, "getCiva");
        CLASS_METRICS.put(CMA.CODE, "getCma");
        CLASS_METRICS.put(CCVA.CODE, "getCcva");
        CLASS_METRICS.put(RP.CODE, "getRp");
        CLASS_METRICS.put(CAI.CODE, "getCai");
        CLASS_METRICS.put(CMR.CODE, "getCmr");
    }

    private final List<String> metricsCodes;

    public MetricsManager(List<String> metricsCodes) {
        this.metricsCodes = metricsCodes;
    }

    public static List<String> getManagedMetricsCodes() {
        return Stream.concat(PROJECT_METRICS.keySet().stream(), CLASS_METRICS.keySet().stream())
                .collect(Collectors.toList());
    }

    public List<String> getMetricsCodes() {
        return metricsCodes;
    }

    public List<ProjectMetric<?>> prepareProjectMetrics() {
        ProjectMetricsStructure projectMetricsStructure = new ProjectMetricsStructure();
        List<ProjectMetric<?>> projectMetrics = new ArrayList<>();
        for (String metricsCode : metricsCodes) {
            try {
                if (PROJECT_METRICS.containsKey(metricsCode)) {
                    Method declaredMethod = ProjectMetricsStructure.class
                            .getDeclaredMethod(PROJECT_METRICS.get(metricsCode));
                    ProjectMetric<?> invoke = (ProjectMetric<?>) declaredMethod.invoke(projectMetricsStructure);
                    projectMetrics.add(invoke);
                }
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                // If something goes wrong, ignore this metric and continue
            }
        }
        return projectMetrics;
    }

    public List<ClassMetric<?>> prepareClassMetrics() {
        ClassMetricsStructure classMetricsStructure = new ClassMetricsStructure();
        List<ClassMetric<?>> classMetrics = new ArrayList<>();
        for (String metricsCode : metricsCodes) {
            try {
                if (CLASS_METRICS.containsKey(metricsCode)) {
                    Method declaredMethod = ClassMetricsStructure.class
                            .getDeclaredMethod(CLASS_METRICS.get(metricsCode));
                    ClassMetric<?> invoke = (ClassMetric<?>) declaredMethod.invoke(classMetricsStructure);
                    classMetrics.add(invoke);
                }
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                // If something goes wrong, ignore this metric and continue
            }
        }
        return classMetrics;
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
