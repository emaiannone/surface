package org.surface.surface;

import org.apache.commons.cli.ParseException;
import org.surface.surface.cli.CLIArgumentsParser;
import org.surface.surface.core.Surface;
import org.surface.surface.core.RunSetting;

public class CLIStarter {
    public static void main(String[] args) {
        CLIArgumentsParser cliArgumentsParser = new CLIArgumentsParser();
        RunSetting runSetting = null;
        try {
            runSetting = cliArgumentsParser.parse(args);
        } catch (ParseException e) {
            e.printStackTrace();
            System.err.println("Exiting...");
            System.exit(1);
        }
        Surface surface = new Surface(runSetting);
        surface.run();
    }
}
