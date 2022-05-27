package org.surface.surface.cli;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.surface.surface.common.RunSetting;
import org.surface.surface.common.Utils;
import org.surface.surface.core.Surface;

import java.util.List;

class CLIStarter {
    private static final Logger LOGGER = LogManager.getLogger();

    public static void main(String[] args) {
        RunSetting runSetting = null;
        try {
            runSetting = CLIArgumentsParser.parse(args);
        } catch (Exception e) {
            List<String> messages = Utils.getExceptionMessageChain(e);
            LOGGER.error("* Failed to parse some command line arguments");
            for (int i = 0, messagesSize = messages.size(); i < messagesSize; i++) {
                String tabs = new String(new char[i + 1]).replace("\0", "  ");
                LOGGER.error("{}* {}", tabs, messages.get(i));
            }
            LOGGER.debug(e);
            System.exit(1);
        }
        Surface surface = new Surface(runSetting);
        surface.run();
    }
}
