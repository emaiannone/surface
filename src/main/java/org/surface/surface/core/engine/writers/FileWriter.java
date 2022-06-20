package org.surface.surface.core.engine.writers;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public abstract class FileWriter {
    private final File outFile;

    public FileWriter(File outFile) {
        this.outFile = outFile;
    }

    public File getOutFile() {
        return outFile;
    }
    public abstract void write(List<Map<String, Object>> content) throws IOException;
}
