package org.surface.surface.core.out.writers;

import java.nio.file.Path;

public class WriterFactory {

    static Writer newWriter(Path outFilePath, String outFileExtension) {
        // NOTE Any new file type must be added here to be supported
        switch (outFileExtension.toLowerCase()) {
            case JsonFileWriter.CODE:
                return new JsonFileWriter(outFilePath);
            default:
                throw new IllegalArgumentException("The given extension is not supported.");
        }
    }
}
