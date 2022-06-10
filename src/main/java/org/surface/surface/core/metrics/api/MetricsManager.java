package org.surface.surface.core.metrics.api;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.surface.surface.core.metrics.classlevel.caiw.CAIW;
import org.surface.surface.core.metrics.classlevel.caiw.CAIWCached;
import org.surface.surface.core.metrics.classlevel.cat.CAT;
import org.surface.surface.core.metrics.classlevel.cat.CATCached;
import org.surface.surface.core.metrics.classlevel.ccda.CCDA;
import org.surface.surface.core.metrics.classlevel.ccda.CCDACached;
import org.surface.surface.core.metrics.classlevel.cida.CIDA;
import org.surface.surface.core.metrics.classlevel.cida.CIDACached;
import org.surface.surface.core.metrics.classlevel.cmt.CMT;
import org.surface.surface.core.metrics.classlevel.cmt.CMTCached;
import org.surface.surface.core.metrics.classlevel.cmw.CMW;
import org.surface.surface.core.metrics.classlevel.cmw.CMWCached;
import org.surface.surface.core.metrics.classlevel.coa.COA;
import org.surface.surface.core.metrics.classlevel.coa.COACached;
import org.surface.surface.core.metrics.classlevel.rpb.RPB;
import org.surface.surface.core.metrics.classlevel.rpb.RPBCached;
import org.surface.surface.core.metrics.projectlevel.cce.CCE;
import org.surface.surface.core.metrics.projectlevel.cce.CCECached;
import org.surface.surface.core.metrics.projectlevel.cct.CCT;
import org.surface.surface.core.metrics.projectlevel.cct.CCTCached;
import org.surface.surface.core.metrics.projectlevel.cdp.CDP;
import org.surface.surface.core.metrics.projectlevel.cdp.CDPCached;
import org.surface.surface.core.metrics.projectlevel.cme.CME;
import org.surface.surface.core.metrics.projectlevel.cme.CMECached;
import org.surface.surface.core.metrics.projectlevel.cscp.CSCP;
import org.surface.surface.core.metrics.projectlevel.cscp.CSCPCached;
import org.surface.surface.core.metrics.projectlevel.csp.CSP;
import org.surface.surface.core.metrics.projectlevel.csp.CSPCached;

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
        PROJECT_METRICS.put(CCT.CODE, "getCct");
        PROJECT_METRICS.put(CCE.CODE, "getCce");
        PROJECT_METRICS.put(CME.CODE, "getCme");
        PROJECT_METRICS.put(CDP.CODE, "getCdp");
        PROJECT_METRICS.put(CSCP.CODE, "getCscp");
        PROJECT_METRICS.put(CSP.CODE, "getCsp");
        CLASS_METRICS = new LinkedHashMap<>();
        CLASS_METRICS.put(CAT.CODE, "getCat");
        CLASS_METRICS.put(CMT.CODE, "getCmt");
        CLASS_METRICS.put(CIDA.CODE, "getCida");
        CLASS_METRICS.put(CCDA.CODE, "getCcda");
        CLASS_METRICS.put(COA.CODE, "getCoa");
        CLASS_METRICS.put(RPB.CODE, "getRpb");
        CLASS_METRICS.put(CAIW.CODE, "getCaiw");
        CLASS_METRICS.put(CMW.CODE, "getCmw");
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
        private final CCT CCT;
        private final CDP CDP;
        private final CSCP CSCP;
        private final CCE CCE;
        private final CME CME;
        private final CSP CSP;

        ProjectMetricsStructure() {
            // NOTE Create here all existing metrics and compose them
            this.CCT = new CCTCached();
            this.CDP = new CDPCached(CCT);
            this.CSCP = new CSCPCached(CCT);
            this.CCE = new CCECached(CCT);
            this.CME = new CMECached();
            this.CSP = new CSPCached();
        }

        CCT getCct() {
            return CCT;
        }

        CDP getCdp() {
            return CDP;
        }

        CSCP getCscp() {
            return CSCP;
        }

        CCE getCCE() {
            return CCE;
        }

        CME getCME() {
            return CME;
        }

        CSP getCsp() {
            return CSP;
        }
    }

    private static class ClassMetricsStructure {
        private final CAT CAT;
        private final CMT CMT;
        private final RPB RPB;
        private final CIDA CIDA;
        private final CCDA CCDA;
        private final COA COA;
        private final CMW CMW;
        private final CAIW CAIW;

        ClassMetricsStructure() {
            // NOTE Create here all existing metrics and compose them
            this.CAT = new CATCached();
            this.CMT = new CMTCached();
            this.RPB = new RPBCached();
            this.CIDA = new CIDACached(CAT);
            this.CCDA = new CCDACached(CAT);
            this.COA = new COACached(CMT);
            this.CMW = new CMWCached(CMT);
            this.CAIW = new CAIWCached(CAT);
        }

        CAT getCat() {
            return CAT;
        }

        CMT getCmt() {
            return CMT;
        }

        RPB getRpb() {
            return RPB;
        }

        CIDA getCida() {
            return CIDA;
        }

        CCDA getCcda() {
            return CCDA;
        }

        COA getCoa() {
            return COA;
        }

        CMW getCmw() {
            return CMW;
        }

        CAIW getCaiw() {
            return CAIW;
        }
    }

}
