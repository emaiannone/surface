package org.surface.surface.core.parsers;

import org.apache.commons.io.FilenameUtils;
import org.surface.surface.core.out.writers.JsonFileWriter;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OutFileParser {

    private static final List<String> supportedFileTypes;

    static {
        // NOTE Any new file type must be added here to be recognized by the CLI parser
        supportedFileTypes = new ArrayList<>();
        supportedFileTypes.add(JsonFileWriter.CODE);
    }

    public static String parseOutFilePath(String outFileString) {
        Path outFilePath = Paths.get(outFileString).toAbsolutePath();
        File file = outFilePath.toFile();
        String extension = FilenameUtils.getExtension(file.toString());
        if (extension == null || extension.equals("")) {
            throw new IllegalArgumentException("The input file has no extension.");
        }
        List<String> fileTypes = supportedFileTypes.stream().map(String::toLowerCase).collect(Collectors.toList());
        if (!fileTypes.contains(extension.toLowerCase())) {
            throw new IllegalArgumentException("The file's extensions is not supported.");
        }
        return extension;
    }
}
