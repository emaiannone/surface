package org.surface.surface.out.writers;

import org.apache.commons.io.FilenameUtils;

import java.nio.file.Path;

public class WriterFactory {

    static Writer newWriter(Path outFilePath) {
        String extension = FilenameUtils.getExtension(outFilePath.toFile().getName());
        switch (extension.toLowerCase()) {
            case JsonFileWriter.CODE:
                return new JsonFileWriter(outFilePath);
            default:
                return new NullWriter();
        }
    }
}
