package org.surface.surface.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.surface.surface.core.runner.AnalysisRunner;
import org.surface.surface.core.runner.AnalysisRunnerFactory;

import java.io.IOException;


public class Surface {
    private static final Logger LOGGER = LogManager.getLogger();

    private final RunSetting runSetting;

    public Surface(RunSetting runSetting) {
        this.runSetting = runSetting;
    }

    public void run() {
        AnalysisRunnerFactory runnerFactory = new AnalysisRunnerFactory();
        AnalysisRunner analysisRunner = runnerFactory.getAnalysisRunner(runSetting);
        LOGGER.debug(analysisRunner);
        try {
            LOGGER.info("* Launching SURFACE");
            analysisRunner.run();
        } catch (IOException e) {
            LOGGER.error(e);
        }
    }
}
