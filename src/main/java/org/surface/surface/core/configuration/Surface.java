package org.surface.surface.core.configuration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.surface.surface.core.configuration.runners.RunningMode;

public class Surface {
    private static final Logger LOGGER = LogManager.getLogger();

    private final RunningMode runningMode;

    public Surface(RunningMode runningMode) {
        this.runningMode = runningMode;
    }

    public void run() throws Exception {
        LOGGER.info("* Launching SURFACE");
        runningMode.run();
        LOGGER.info("* Exiting SURFACE");
    }
}
