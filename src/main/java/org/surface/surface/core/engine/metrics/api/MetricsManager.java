package org.surface.surface.core.engine.metrics.api;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.surface.surface.core.engine.metrics.classlevel.caai.CAAI;
import org.surface.surface.core.engine.metrics.classlevel.caai.CAAICached;
import org.surface.surface.core.engine.metrics.classlevel.caiw.CAIW;
import org.surface.surface.core.engine.metrics.classlevel.caiw.CAIWCached;
import org.surface.surface.core.engine.metrics.classlevel.cat.CAT;
import org.surface.surface.core.engine.metrics.classlevel.cat.CATCached;
import org.surface.surface.core.engine.metrics.classlevel.ccda.CCDA;
import org.surface.surface.core.engine.metrics.classlevel.ccda.CCDACached;
import org.surface.surface.core.engine.metrics.classlevel.cida.CIDA;
import org.surface.surface.core.engine.metrics.classlevel.cida.CIDACached;
import org.surface.surface.core.engine.metrics.classlevel.cmai.CMAI;
import org.surface.surface.core.engine.metrics.classlevel.cmai.CMAICached;
import org.surface.surface.core.engine.metrics.classlevel.cmt.CMT;
import org.surface.surface.core.engine.metrics.classlevel.cmt.CMTCached;
import org.surface.surface.core.engine.metrics.classlevel.cmw.CMW;
import org.surface.surface.core.engine.metrics.classlevel.cmw.CMWCached;
import org.surface.surface.core.engine.metrics.classlevel.coa.COA;
import org.surface.surface.core.engine.metrics.classlevel.coa.COACached;
import org.surface.surface.core.engine.metrics.classlevel.cwmp.CWMP;
import org.surface.surface.core.engine.metrics.classlevel.cwmp.CWMPCached;
import org.surface.surface.core.engine.metrics.classlevel.rpb.RPB;
import org.surface.surface.core.engine.metrics.classlevel.rpb.RPBCached;
import org.surface.surface.core.engine.metrics.classlevel.uaca.UACA;
import org.surface.surface.core.engine.metrics.classlevel.uaca.UACACached;
import org.surface.surface.core.engine.metrics.projectlevel.cce.CCE;
import org.surface.surface.core.engine.metrics.projectlevel.cce.CCECached;
import org.surface.surface.core.engine.metrics.projectlevel.cct.CCT;
import org.surface.surface.core.engine.metrics.projectlevel.cct.CCTCached;
import org.surface.surface.core.engine.metrics.projectlevel.cdp.CDP;
import org.surface.surface.core.engine.metrics.projectlevel.cdp.CDPCached;
import org.surface.surface.core.engine.metrics.projectlevel.cme.CME;
import org.surface.surface.core.engine.metrics.projectlevel.cme.CMECached;
import org.surface.surface.core.engine.metrics.projectlevel.cscp.CSCP;
import org.surface.surface.core.engine.metrics.projectlevel.cscp.CSCPCached;
import org.surface.surface.core.engine.metrics.projectlevel.csp.CSP;
import org.surface.surface.core.engine.metrics.projectlevel.csp.CSPCached;

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
        CLASS_METRICS.put(CMAI.CODE, "getCmai");
        CLASS_METRICS.put(CAAI.CODE, "getCaai");
        CLASS_METRICS.put(CAIW.CODE, "getCaiw");
        CLASS_METRICS.put(CMW.CODE, "getCmw");
        CLASS_METRICS.put(CWMP.CODE, "getCwmp");
        CLASS_METRICS.put(UACA.CODE, "getUaca");
    }

    private final List<String> metricsCodes;

    public MetricsManager(List<String> metricsCodes) {
        this.metricsCodes = metricsCodes;
    }

    public static List<String> getAllSupportedMetrics() {
        return Stream.concat(PROJECT_METRICS.keySet().stream(), CLASS_METRICS.keySet().stream())
                .collect(Collectors.toList());
    }

    public List<String> getLoadedMetrics() {
        return metricsCodes;
    }

    public int getNumberLoadedMetrics() {
        return metricsCodes.size();
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
                LOGGER.error("* There have been errors while computing metric {}\n{}", metricsCode, e.getMessage());
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
        private final CCT cct;
        private final CDP cdp;
        private final CSCP cscp;
        private final CCE cce;
        private final CME cme;
        private final CSP csp;

        ProjectMetricsStructure() {
            // NOTE Create here all existing metrics and compose them
            this.cct = new CCTCached();
            this.cdp = new CDPCached(cct);
            this.cscp = new CSCPCached(cct);
            this.cce = new CCECached(cct);
            this.cme = new CMECached();
            this.csp = new CSPCached();
        }

        CCT getCct() {
            return cct;
        }

        CDP getCdp() {
            return cdp;
        }

        CSCP getCscp() {
            return cscp;
        }

        CCE getCce() {
            return cce;
        }

        CME getCme() {
            return cme;
        }

        CSP getCsp() {
            return csp;
        }
    }

    private static class ClassMetricsStructure {
        private final CAT cat;
        private final CMT cmt;
        private final RPB rpb;
        private final CIDA cida;
        private final CCDA ccda;
        private final COA coa;
        private final CMW cmw;
        private final CWMP cwmp;
        private final UACA uaca;
        private final CMAI cmai;
        private final CAAI caai;
        private final CAIW caiw;

        ClassMetricsStructure() {
            // NOTE Create here all existing metrics and compose them
            this.cat = new CATCached();
            this.cmt = new CMTCached();
            this.rpb = new RPBCached();
            this.cida = new CIDACached(cat);
            this.ccda = new CCDACached(cat);
            this.coa = new COACached(cmt);
            this.cmw = new CMWCached(cmt);
            this.cwmp = new CWMPCached();
            this.uaca = new UACACached();
            this.cmai = new CMAICached(cat);
            this.caai = new CAAICached(cat);
            this.caiw = new CAIWCached(cat);
        }

        CAT getCat() {
            return cat;
        }

        CMT getCmt() {
            return cmt;
        }

        RPB getRpb() {
            return rpb;
        }

        CIDA getCida() {
            return cida;
        }

        CCDA getCcda() {
            return ccda;
        }

        COA getCoa() {
            return coa;
        }

        CMW getCmw() {
            return cmw;
        }

        CWMP getCwmp() {
            return cwmp;
        }

        UACA getUaca() {
            return uaca;
        }

        CMAI getCmai() {
            return cmai;
        }

        CAAI getCaai() {
            return caai;
        }

        CAIW getCaiw() {
            return caiw;
        }
    }

}
