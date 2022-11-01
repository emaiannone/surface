package org.surface.surface.core.exporters;

import org.surface.surface.core.configuration.runners.results.RunResults;

import java.io.File;
import java.io.IOException;

public abstract class RunResultsExporter {
    private final File outFile;

    public RunResultsExporter(File outFile) {
        this.outFile = outFile;
    }

    public File getOutFile() {
        return outFile;
    }

    public abstract String export(RunResults results) throws IOException;
}
