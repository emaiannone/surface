package org.surface.surface.core.engine.metrics.api;

import org.apache.commons.lang3.exception.ExceptionUtils;
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
import org.surface.surface.core.engine.metrics.classlevel.ucam.UCAM;
import org.surface.surface.core.engine.metrics.classlevel.ucam.UCAMCached;
import org.surface.surface.core.engine.metrics.projectlevel.acai.ACAI;
import org.surface.surface.core.engine.metrics.projectlevel.acai.ACAICached;
import org.surface.surface.core.engine.metrics.projectlevel.acmi.ACMI;
import org.surface.surface.core.engine.metrics.projectlevel.acmi.ACMICached;
import org.surface.surface.core.engine.metrics.projectlevel.acsi.ACSI;
import org.surface.surface.core.engine.metrics.projectlevel.acsi.ACSICached;
import org.surface.surface.core.engine.metrics.projectlevel.acsp.ACSP;
import org.surface.surface.core.engine.metrics.projectlevel.acsp.ACSPCached;
import org.surface.surface.core.engine.metrics.projectlevel.ccc.CCC;
import org.surface.surface.core.engine.metrics.projectlevel.ccc.CCCCached;
import org.surface.surface.core.engine.metrics.projectlevel.cce.CCE;
import org.surface.surface.core.engine.metrics.projectlevel.cce.CCECached;
import org.surface.surface.core.engine.metrics.projectlevel.cct.CCT;
import org.surface.surface.core.engine.metrics.projectlevel.cct.CCTCached;
import org.surface.surface.core.engine.metrics.projectlevel.cdp.CDP;
import org.surface.surface.core.engine.metrics.projectlevel.cdp.CDPCached;
import org.surface.surface.core.engine.metrics.projectlevel.cme.CME;
import org.surface.surface.core.engine.metrics.projectlevel.cme.CMECached;
import org.surface.surface.core.engine.metrics.projectlevel.cpcc.CPCC;
import org.surface.surface.core.engine.metrics.projectlevel.cpcc.CPCCCached;
import org.surface.surface.core.engine.metrics.projectlevel.cscp.CSCP;
import org.surface.surface.core.engine.metrics.projectlevel.cscp.CSCPCached;
import org.surface.surface.core.engine.metrics.projectlevel.ucac.UCAC;
import org.surface.surface.core.engine.metrics.projectlevel.ucac.UCACCached;

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
        PROJECT_METRICS.put(CCC.CODE, "getCcc");
        PROJECT_METRICS.put(CPCC.CODE, "getCpcc");
        PROJECT_METRICS.put(CCE.CODE, "getCce");
        PROJECT_METRICS.put(CME.CODE, "getCme");
        PROJECT_METRICS.put(UCAC.CODE, "getUcac");
        PROJECT_METRICS.put(CDP.CODE, "getCdp");
        PROJECT_METRICS.put(CSCP.CODE, "getCscp");
        PROJECT_METRICS.put(ACSP.CODE, "getAcsp");
        PROJECT_METRICS.put(ACSI.CODE, "getAcsi");
        PROJECT_METRICS.put(ACMI.CODE, "getAcmi");
        PROJECT_METRICS.put(ACAI.CODE, "getAcai");
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
        CLASS_METRICS.put(UCAM.CODE, "getUcam");
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
                LOGGER.error("* There have been errors while computing metric {}. Details:", metricsCode);
                LOGGER.error("\t* {}", ExceptionUtils.getStackTrace(e));
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

    // NOTE Create here all existing metrics and compose them
    private static class ProjectMetricsStructure {
        private final CCT cct;
        private final CCC ccc;
        private final CPCC cpcc;
        private final CCE cce;
        private final CME cme;
        private final UCAC ucac;
        private final CDP cdp;
        private final CSCP cscp;
        private final ACSP acsp;
        private final ACSI acsi;
        private final ACMI acmi;
        private final ACAI acai;

        ProjectMetricsStructure() {
            this.cct = new CCTCached();
            this.ccc = new CCCCached();
            this.cpcc = new CPCCCached();
            this.cce = new CCECached();
            this.cme = new CMECached();
            this.ucac = new UCACCached();
            this.cdp = new CDPCached();
            this.cscp = new CSCPCached();
            this.acsp = new ACSPCached();
            this.acsi = new ACSICached();
            this.acmi = new ACMICached();
            this.acai = new ACAICached();
        }

        public CCT getCct() {
            return cct;
        }

        public CCC getCcc() {
            return ccc;
        }

        public CPCC getCpcc() {
            return cpcc;
        }

        public CCE getCce() {
            return cce;
        }

        public CME getCme() {
            return cme;
        }

        public UCAC getUcac() {
            return ucac;
        }

        public CDP getCdp() {
            return cdp;
        }

        public CSCP getCscp() {
            return cscp;
        }

        public ACSP getAcsp() {
            return acsp;
        }

        public ACSI getAcsi() {
            return acsi;
        }

        public ACMI getAcmi() {
            return acmi;
        }

        public ACAI getAcai() {
            return acai;
        }
    }

    // NOTE Create here all existing metrics and compose them
    private static class ClassMetricsStructure {
        private final CAT cat;
        private final CMT cmt;
        private final CIDA cida;
        private final CCDA ccda;
        private final COA coa;
        private final RPB rpb;
        private final CMAI cmai;
        private final CAAI caai;
        private final CAIW caiw;
        private final CMW cmw;
        private final CWMP cwmp;
        private final UACA uaca;
        private final UCAM ucam;

        ClassMetricsStructure() {
            this.cat = new CATCached();
            this.cmt = new CMTCached();
            this.cida = new CIDACached();
            this.ccda = new CCDACached();
            this.coa = new COACached();
            this.rpb = new RPBCached();
            this.cmai = new CMAICached();
            this.caai = new CAAICached();
            this.caiw = new CAIWCached();
            this.cmw = new CMWCached();
            this.cwmp = new CWMPCached();
            this.uaca = new UACACached();
            this.ucam = new UCAMCached();
        }

        public CAT getCat() {
            return cat;
        }

        public CMT getCmt() {
            return cmt;
        }

        public CIDA getCida() {
            return cida;
        }

        public CCDA getCcda() {
            return ccda;
        }

        public COA getCoa() {
            return coa;
        }

        public RPB getRpb() {
            return rpb;
        }

        public CMAI getCmai() {
            return cmai;
        }

        public CAAI getCaai() {
            return caai;
        }

        public CAIW getCaiw() {
            return caiw;
        }

        public CMW getCmw() {
            return cmw;
        }

        public CWMP getCwmp() {
            return cwmp;
        }

        public UACA getUaca() {
            return uaca;
        }

        public UCAM getUcam() {
            return ucam;
        }
    }

}
