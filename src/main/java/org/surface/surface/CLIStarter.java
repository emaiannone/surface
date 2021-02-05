package org.surface.surface;

import org.apache.commons.cli.ParseException;
import org.surface.surface.cli.CLIParser;
import org.surface.surface.core.Surface;
import org.surface.surface.core.SurfaceInput;

public class CLIStarter {
    public static void main(String[] args) {
        CLIParser cliParser = new CLIParser();
        SurfaceInput surfaceInput = null;
        try {
            surfaceInput = cliParser.parse(args);
        } catch (ParseException e) {
            e.printStackTrace();
            System.exit(1);
        }
        Surface surface = new Surface(surfaceInput);
        surface.run();
    }
}
