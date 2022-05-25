package org.surface.surface.out.writers;

import java.nio.file.Path;

public abstract class FileWriter implements Writer {
    private Path outFilePath;

    public FileWriter(Path outFilePath) {
        this.outFilePath = outFilePath;
    }

    public Path getOutFilePath() {
        return outFilePath;
    }

    public void setOutFilePath(Path outFilePath) {
        this.outFilePath = outFilePath;
    }
}
