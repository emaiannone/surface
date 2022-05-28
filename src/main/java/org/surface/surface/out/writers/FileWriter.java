package org.surface.surface.out.writers;

import java.nio.file.Path;

abstract class FileWriter extends Writer {
    private Path outFilePath;

    FileWriter(Path outFilePath) {
        this.outFilePath = outFilePath;
    }

    Path getOutFilePath() {
        return outFilePath;
    }

    public void setOutFilePath(Path outFilePath) {
        this.outFilePath = outFilePath;
    }
}
