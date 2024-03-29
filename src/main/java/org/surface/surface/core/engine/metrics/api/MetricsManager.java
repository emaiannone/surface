package org.surface.surface.core.engine.metrics.api;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.surface.surface.core.engine.metrics.clazz.caai.CAAI;
import org.surface.surface.core.engine.metrics.clazz.caai.CAAICached;
import org.surface.surface.core.engine.metrics.clazz.caiw.CAIW;
import org.surface.surface.core.engine.metrics.clazz.caiw.CAIWCached;
import org.surface.surface.core.engine.metrics.clazz.cat.CAT;
import org.surface.surface.core.engine.metrics.clazz.cat.CATCached;
import org.surface.surface.core.engine.metrics.clazz.ccda.CCDA;
import org.surface.surface.core.engine.metrics.clazz.ccda.CCDACached;
import org.surface.surface.core.engine.metrics.clazz.cida.CIDA;
import org.surface.surface.core.engine.metrics.clazz.cida.CIDACached;
import org.surface.surface.core.engine.metrics.clazz.cmai.CMAI;
import org.surface.surface.core.engine.metrics.clazz.cmai.CMAICached;
import org.surface.surface.core.engine.metrics.clazz.cmt.CMT;
import org.surface.surface.core.engine.metrics.clazz.cmt.CMTCached;
import org.surface.surface.core.engine.metrics.clazz.cmw.CMW;
import org.surface.surface.core.engine.metrics.clazz.cmw.CMWCached;
import org.surface.surface.core.engine.metrics.clazz.coa.COA;
import org.surface.surface.core.engine.metrics.clazz.coa.COACached;
import org.surface.surface.core.engine.metrics.clazz.cwmp.CWMP;
import org.surface.surface.core.engine.metrics.clazz.cwmp.CWMPCached;
import org.surface.surface.core.engine.metrics.clazz.rpb.RPB;
import org.surface.surface.core.engine.metrics.clazz.rpb.RPBCached;
import org.surface.surface.core.engine.metrics.clazz.uaca.UACA;
import org.surface.surface.core.engine.metrics.clazz.uaca.UACACached;
import org.surface.surface.core.engine.metrics.clazz.ucam.UCAM;
import org.surface.surface.core.engine.metrics.clazz.ucam.UCAMCached;
import org.surface.surface.core.engine.metrics.project.cai.CAI;
import org.surface.surface.core.engine.metrics.project.cai.CAICached;
import org.surface.surface.core.engine.metrics.project.ccc.CCC;
import org.surface.surface.core.engine.metrics.project.ccc.CCCCached;
import org.surface.surface.core.engine.metrics.project.cce.CCE;
import org.surface.surface.core.engine.metrics.project.cce.CCECached;
import org.surface.surface.core.engine.metrics.project.cct.CCT;
import org.surface.surface.core.engine.metrics.project.cct.CCTCached;
import org.surface.surface.core.engine.metrics.project.cdp.CDP;
import org.surface.surface.core.engine.metrics.project.cdp.CDPCached;
import org.surface.surface.core.engine.metrics.project.cme.CME;
import org.surface.surface.core.engine.metrics.project.cme.CMECached;
import org.surface.surface.core.engine.metrics.project.cmi.CMI;
import org.surface.surface.core.engine.metrics.project.cmi.CMICached;
import org.surface.surface.core.engine.metrics.project.cpcc.CPCC;
import org.surface.surface.core.engine.metrics.project.cpcc.CPCCCached;
import org.surface.surface.core.engine.metrics.project.cscp.CSCP;
import org.surface.surface.core.engine.metrics.project.cscp.CSCPCached;
import org.surface.surface.core.engine.metrics.project.csi.CSI;
import org.surface.surface.core.engine.metrics.project.csi.CSICached;
import org.surface.surface.core.engine.metrics.project.csp.CSP;
import org.surface.surface.core.engine.metrics.project.csp.CSPCached;
import org.surface.surface.core.engine.metrics.project.pem.PEM;
import org.surface.surface.core.engine.metrics.project.pem.PEMCached;
import org.surface.surface.core.engine.metrics.project.pfsd.PFSD;
import org.surface.surface.core.engine.metrics.project.pfsd.PFSDCached;
import org.surface.surface.core.engine.metrics.project.pi.PI;
import org.surface.surface.core.engine.metrics.project.pi.PICached;
import org.surface.surface.core.engine.metrics.project.plcm.PLCM;
import org.surface.surface.core.engine.metrics.project.plcm.PLCMCached;
import org.surface.surface.core.engine.metrics.project.plp.PLP;
import org.surface.surface.core.engine.metrics.project.plp.PLPCached;
import org.surface.surface.core.engine.metrics.project.pras.PRAS;
import org.surface.surface.core.engine.metrics.project.pras.PRASCached;
import org.surface.surface.core.engine.metrics.project.pswl.PSWL;
import org.surface.surface.core.engine.metrics.project.pswl.PSWLCached;
import org.surface.surface.core.engine.metrics.project.rca.RCA;
import org.surface.surface.core.engine.metrics.project.rca.RCACached;
import org.surface.surface.core.engine.metrics.project.rcc.RCC;
import org.surface.surface.core.engine.metrics.project.rcc.RCCCached;
import org.surface.surface.core.engine.metrics.project.rcm.RCM;
import org.surface.surface.core.engine.metrics.project.rcm.RCMCached;
import org.surface.surface.core.engine.metrics.project.sam.SAM;
import org.surface.surface.core.engine.metrics.project.sam.SAMCached;
import org.surface.surface.core.engine.metrics.project.tsi.TSI;
import org.surface.surface.core.engine.metrics.project.tsi.TSICached;
import org.surface.surface.core.engine.metrics.project.ucac.UCAC;
import org.surface.surface.core.engine.metrics.project.ucac.UCACCached;
import org.surface.surface.core.engine.metrics.project.wca.WCA;
import org.surface.surface.core.engine.metrics.project.wca.WCACached;
import org.surface.surface.core.engine.metrics.project.wcc.WCC;
import org.surface.surface.core.engine.metrics.project.wcc.WCCCached;
import org.surface.surface.core.engine.metrics.project.wcm.WCM;
import org.surface.surface.core.engine.metrics.project.wcm.WCMCached;

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
        PROJECT_METRICS.put(org.surface.surface.core.engine.metrics.project.cat.CAT.CODE, "getCat");
        PROJECT_METRICS.put(org.surface.surface.core.engine.metrics.project.cmt.CMT.CODE, "getCmt");
        PROJECT_METRICS.put(CCT.CODE, "getCct");
        PROJECT_METRICS.put(org.surface.surface.core.engine.metrics.project.cida.CIDA.CODE, "getCida");
        PROJECT_METRICS.put(org.surface.surface.core.engine.metrics.project.ccda.CCDA.CODE, "getCcda");
        PROJECT_METRICS.put(org.surface.surface.core.engine.metrics.project.coa.COA.CODE, "getCoa");
        PROJECT_METRICS.put(org.surface.surface.core.engine.metrics.project.rpb.RPB.CODE, "getRpb");
        PROJECT_METRICS.put(org.surface.surface.core.engine.metrics.project.cmai.CMAI.CODE, "getCmai");
        PROJECT_METRICS.put(org.surface.surface.core.engine.metrics.project.caai.CAAI.CODE, "getCaai");
        PROJECT_METRICS.put(org.surface.surface.core.engine.metrics.project.caiw.CAIW.CODE, "getCaiw");
        PROJECT_METRICS.put(org.surface.surface.core.engine.metrics.project.cmw.CMW.CODE, "getCmw");
        PROJECT_METRICS.put(org.surface.surface.core.engine.metrics.project.cwmp.CWMP.CODE, "getCwmp");
        PROJECT_METRICS.put(CCC.CODE, "getCcc");
        PROJECT_METRICS.put(CPCC.CODE, "getCpcc");
        PROJECT_METRICS.put(CCE.CODE, "getCce");
        PROJECT_METRICS.put(CME.CODE, "getCme");
        PROJECT_METRICS.put(org.surface.surface.core.engine.metrics.project.uaca.UACA.CODE, "getUaca");
        PROJECT_METRICS.put(org.surface.surface.core.engine.metrics.project.ucam.UCAM.CODE, "getUcam");
        PROJECT_METRICS.put(UCAC.CODE, "getUcac");
        PROJECT_METRICS.put(CDP.CODE, "getCdp");
        PROJECT_METRICS.put(CSCP.CODE, "getCscp");
        PROJECT_METRICS.put(CSP.CODE, "getCsp");
        PROJECT_METRICS.put(CSI.CODE, "getCsi");
        PROJECT_METRICS.put(CMI.CODE, "getCmi");
        PROJECT_METRICS.put(CAI.CODE, "getCai");
        PROJECT_METRICS.put(RCA.CODE, "getRca");
        PROJECT_METRICS.put(WCA.CODE, "getWca");
        PROJECT_METRICS.put(RCM.CODE, "getRcm");
        PROJECT_METRICS.put(WCM.CODE, "getWcm");
        PROJECT_METRICS.put(RCC.CODE, "getRcc");
        PROJECT_METRICS.put(WCC.CODE, "getWcc");
        PROJECT_METRICS.put(PLP.CODE, "getSam");
        PROJECT_METRICS.put(PRAS.CODE, "getPras");
        PROJECT_METRICS.put(PSWL.CODE, "getPswl");
        PROJECT_METRICS.put(PFSD.CODE, "getPfsd");
        PROJECT_METRICS.put(PLCM.CODE, "getPlcm");
        PROJECT_METRICS.put(PI.CODE, "getPi");
        PROJECT_METRICS.put(PEM.CODE, "getPem");
        PROJECT_METRICS.put(TSI.CODE, "getTsi");
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
        private final org.surface.surface.core.engine.metrics.project.cat.CAT cat;
        private final org.surface.surface.core.engine.metrics.project.cmt.CMT cmt;
        private final CCT cct;
        private final org.surface.surface.core.engine.metrics.project.cida.CIDA cida;
        private final org.surface.surface.core.engine.metrics.project.ccda.CCDA ccda;
        private final org.surface.surface.core.engine.metrics.project.coa.COA coa;
        private final org.surface.surface.core.engine.metrics.project.rpb.RPB rpb;
        private final org.surface.surface.core.engine.metrics.project.cmai.CMAI cmai;
        private final org.surface.surface.core.engine.metrics.project.caai.CAAI caai;
        private final org.surface.surface.core.engine.metrics.project.caiw.CAIW caiw;
        private final org.surface.surface.core.engine.metrics.project.cmw.CMW cmw;
        private final org.surface.surface.core.engine.metrics.project.cwmp.CWMP cwmp;
        private final CCC ccc;
        private final CPCC cpcc;
        private final CCE cce;
        private final CME cme;
        private final org.surface.surface.core.engine.metrics.project.uaca.UACA uaca;
        private final org.surface.surface.core.engine.metrics.project.ucam.UCAM ucam;
        private final UCAC ucac;
        private final CDP cdp;
        private final CSCP cscp;
        private final CSP csp;
        private final CSI csi;
        private final CMI cmi;
        private final CAI cai;
        private final RCA rca;
        private final WCA wca;
        private final RCM rcm;
        private final WCM wcm;
        private final RCC rcc;
        private final WCC wcc;
        private final SAM sam;
        private final PLP plp;
        private final PRAS pras;
        private final PSWL pswl;
        private final PFSD pfsd;
        private final PLCM plcm;
        private final PI pi;
        private final PEM pem;
        private final TSI tsi;

        ProjectMetricsStructure() {
            this.cat = new org.surface.surface.core.engine.metrics.project.cat.CATCached();
            this.cmt = new org.surface.surface.core.engine.metrics.project.cmt.CMTCached();
            this.cct = new CCTCached();
            this.cida = new org.surface.surface.core.engine.metrics.project.cida.CIDACached();
            this.ccda = new org.surface.surface.core.engine.metrics.project.ccda.CCDACached();
            this.coa = new org.surface.surface.core.engine.metrics.project.coa.COACached();
            this.rpb = new org.surface.surface.core.engine.metrics.project.rpb.RPBCached();
            this.cmai = new org.surface.surface.core.engine.metrics.project.cmai.CMAICached();
            this.caai = new org.surface.surface.core.engine.metrics.project.caai.CAAICached();
            this.caiw = new org.surface.surface.core.engine.metrics.project.caiw.CAIWCached();
            this.cmw = new org.surface.surface.core.engine.metrics.project.cmw.CMWCached();
            this.cwmp = new org.surface.surface.core.engine.metrics.project.cwmp.CWMPCached();
            this.ccc = new CCCCached();
            this.cpcc = new CPCCCached();
            this.cce = new CCECached();
            this.cme = new CMECached();
            this.uaca = new org.surface.surface.core.engine.metrics.project.uaca.UACACached();
            this.ucam = new org.surface.surface.core.engine.metrics.project.ucam.UCAMCached();
            this.ucac = new UCACCached();
            this.cdp = new CDPCached();
            this.cscp = new CSCPCached();
            this.csp = new CSPCached();
            this.csi = new CSICached();
            this.cmi = new CMICached();
            this.cai = new CAICached();
            this.rca = new RCACached(cida, ccda, cai);
            this.wca = new WCACached(cmai, caai, uaca);
            this.rcm = new RCMCached(coa, cme, cmi);
            this.wcm = new WCMCached(caiw, cmw, cwmp, ucam);
            this.rcc = new RCCCached(rpb, cpcc, cce, cdp, csp);
            this.wcc = new WCCCached(ccc, ucac, cscp, csi);
            this.sam = new SAMCached(cat, cmt, cct);
            this.plp = new PLPCached(wca, wcm, wcc);
            this.pras = new PRASCached(rca, rcm, rcc);
            this.pswl = new PSWLCached(wca, wcm);
            this.pfsd = new PFSDCached(rca, rcm);
            this.plcm = new PLCMCached(rcc, wcc);
            this.pi = new PICached(wcc);
            this.pem = new PEMCached(sam);
            this.tsi = new TSICached(plp, pras, pswl, pfsd, plcm, pi, pem);
        }

        public org.surface.surface.core.engine.metrics.project.cat.CAT getCat() {
            return cat;
        }

        public org.surface.surface.core.engine.metrics.project.cmt.CMT getCmt() {
            return cmt;
        }

        public CCT getCct() {
            return cct;
        }

        public org.surface.surface.core.engine.metrics.project.cida.CIDA getCida() {
            return cida;
        }

        public org.surface.surface.core.engine.metrics.project.ccda.CCDA getCcda() {
            return ccda;
        }

        public org.surface.surface.core.engine.metrics.project.coa.COA getCoa() {
            return coa;
        }

        public org.surface.surface.core.engine.metrics.project.rpb.RPB getRpb() {
            return rpb;
        }

        public org.surface.surface.core.engine.metrics.project.cmai.CMAI getCmai() {
            return cmai;
        }

        public org.surface.surface.core.engine.metrics.project.caai.CAAI getCaai() {
            return caai;
        }

        public org.surface.surface.core.engine.metrics.project.caiw.CAIW getCaiw() {
            return caiw;
        }

        public org.surface.surface.core.engine.metrics.project.cmw.CMW getCmw() {
            return cmw;
        }

        public org.surface.surface.core.engine.metrics.project.cwmp.CWMP getCwmp() {
            return cwmp;
        }

        public org.surface.surface.core.engine.metrics.project.uaca.UACA getUaca() {
            return uaca;
        }

        public org.surface.surface.core.engine.metrics.project.ucam.UCAM getUcam() {
            return ucam;
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

        public CSP getCsp() {
            return csp;
        }

        public CSI getCsi() {
            return csi;
        }

        public CMI getCmi() {
            return cmi;
        }

        public CAI getCai() {
            return cai;
        }

        public RCA getRca() {
            return rca;
        }

        public WCA getWca() {
            return wca;
        }

        public RCM getRcm() {
            return rcm;
        }

        public WCM getWcm() {
            return wcm;
        }

        public RCC getRcc() {
            return rcc;
        }

        public WCC getWcc() {
            return wcc;
        }

        public SAM getSam() {
            return sam;
        }

        public PLP getPlp() {
            return plp;
        }

        public PRAS getPras() {
            return pras;
        }

        public PSWL getPswl() {
            return pswl;
        }

        public PFSD getPfsd() {
            return pfsd;
        }

        public PLCM getPlcm() {
            return plcm;
        }

        public PI getPi() {
            return pi;
        }

        public PEM getPem() {
            return pem;
        }

        public TSI getTsi() {
            return tsi;
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
