package org.surface.surface.core.out.writers;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public abstract class FileWriter {
    private final File outFile;

    public FileWriter(File outFile) {
        this.outFile = outFile;
    }

    public File getOutFile() {
        return outFile;
    }
    public abstract void write(Map<String, Object> content) throws IOException;
}
