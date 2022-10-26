package org.surface.surface.cli;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.surface.surface.core.Utils;
import org.surface.surface.core.configuration.Surface;
import org.surface.surface.core.configuration.runners.RunningMode;

import java.util.List;

class CLIStarter {
    private static final Logger LOGGER = LogManager.getLogger();

    public static void main(String[] args) {
        RunningMode runner = null;
        try {
            runner = CLIArgumentsParser.parse(args);
        } catch (Exception e) {
            List<String> messages = Utils.getExceptionMessageChain(e);
            LOGGER.error("* Failed to parse some command line arguments");
            for (int i = 0, messagesSize = messages.size(); i < messagesSize; i++) {
                String tabs = new String(new char[i + 1]).replace("\0", "  ");
                LOGGER.error("{}* {}", tabs, messages.get(i));
            }
            LOGGER.debug("\t* {}", ExceptionUtils.getStackTrace(e));
            System.exit(1);
        }
        if (runner == null) {
            System.exit(1);
        }
        try {
            Surface surface = new Surface(runner);
            surface.run();
        } catch (Exception e) {
            List<String> messages = Utils.getExceptionMessageChain(e);
            LOGGER.error("* Failed to parse some command line arguments");
            for (int i = 0, messagesSize = messages.size(); i < messagesSize; i++) {
                String tabs = new String(new char[i + 1]).replace("\0", "  ");
                LOGGER.error("{}* {}", tabs, messages.get(i));
            }
            LOGGER.debug("\t* {}", ExceptionUtils.getStackTrace(e));
            System.exit(1);
        }
    }
}
