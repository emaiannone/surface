package org.surface.surface.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.surface.surface.common.RunSetting;
import org.surface.surface.core.runners.AnalysisRunner;
import org.surface.surface.core.runners.AnalysisRunnerFactory;

public class Surface {
    private static final Logger LOGGER = LogManager.getLogger();

    private final RunSetting runSetting;

    public Surface(RunSetting runSetting) {
        this.runSetting = runSetting;
    }

    public void run() {
        LOGGER.info("* Setting up SURFACE");
        AnalysisRunnerFactory runnerFactory = new AnalysisRunnerFactory();
        AnalysisRunner<?> analysisRunner = runnerFactory.getAnalysisRunner(runSetting);
        try {
            LOGGER.info("* Launching SURFACE");
            analysisRunner.run();
        } catch (Exception e) {
            LOGGER.fatal("Aborting SURFACE", e);
        } finally {
            LOGGER.info("* Exiting SURFACE");
        }
    }
}
