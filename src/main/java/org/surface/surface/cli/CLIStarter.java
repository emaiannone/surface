package org.surface.surface.cli;

import org.apache.commons.cli.ParseException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.surface.surface.common.RunSetting;
import org.surface.surface.core.Surface;

public class CLIStarter {
    private static final Logger LOGGER = LogManager.getLogger();

    public static void main(String[] args) {
        RunSetting runSetting = null;
        try {
            runSetting = CLIArgumentsParser.parse(args);
        } catch (ParseException e) {
            LOGGER.error("* Failed to parse some command line arguments", e);
            LOGGER.info("* Exiting...");
            System.exit(1);
        }
        Surface surface = new Surface(runSetting);
        surface.run();
    }
}
