package org.surface.surface.out.writers;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;

public abstract class Writer {
    public abstract void write(Map<String, Object> content) throws IOException;

    public static Writer newWriter(Path outFilePath) {
        return WriterFactory.newWriter(outFilePath);
    }

}
