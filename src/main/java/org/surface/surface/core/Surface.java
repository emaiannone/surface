package org.surface.surface.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.surface.surface.core.runners.AnalysisRunner;
import org.surface.surface.core.runners.AnalysisRunnerFactory;

import java.io.IOException;


public class Surface {
    private static final Logger LOGGER = LogManager.getLogger();

    private final RunSetting runSetting;

    public Surface(RunSetting runSetting) {
        this.runSetting = runSetting;
    }

    public void run() {
        LOGGER.info("* Setting up SURFACE");
        AnalysisRunnerFactory runnerFactory = new AnalysisRunnerFactory();
        AnalysisRunner analysisRunner = runnerFactory.getAnalysisRunner(runSetting);
        try {
            LOGGER.info("* Launching SURFACE");
            analysisRunner.run();
        } catch (IOException e) {
            LOGGER.error(e);
        }
    }
}
