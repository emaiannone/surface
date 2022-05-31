package org.surface.surface.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.surface.surface.core.runners.ModeRunner;

import java.util.List;

public class Surface {
    private static final Logger LOGGER = LogManager.getLogger();

    private final ModeRunner<?> modeRunner;

    public Surface(ModeRunner<?> modeRunner) {
        this.modeRunner = modeRunner;
    }

    public void run() {
        LOGGER.info("* Launching SURFACE");
        try {
            modeRunner.run();
        } catch (Exception e) {
            List<String> messages = Utils.getExceptionMessageChain(e);
            LOGGER.error("* Exiting SURFACE due to an unrecoverable error");
            for (int i = 0, messagesSize = messages.size(); i < messagesSize; i++) {
                String tabs = new String(new char[i + 1]).replace("\0", "  ");
                LOGGER.error("{}* {}", tabs, messages.get(i));
            }
            LOGGER.debug(e);
            System.exit(1);
        } finally {
            LOGGER.info("* Exiting SURFACE");
        }
    }
}
