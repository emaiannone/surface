package org.surface.surface.core.configuration.interpreters;

import org.apache.commons.io.FilenameUtils;
import org.surface.surface.core.exporters.JsonRunResultsExporter;
import org.surface.surface.core.exporters.PlainRunResultsExporter;
import org.surface.surface.core.exporters.RunResultsExporter;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class OutFileInterpreter implements InputStringInterpreter<RunResultsExporter> {
    private static final Map<String, Class<? extends RunResultsExporter>> SUPPORTED_FILE_TYPES;

    static {
        // NOTE Any new file type must be added here to be recognized by the CLI parser
        SUPPORTED_FILE_TYPES = new HashMap<>();
        SUPPORTED_FILE_TYPES.put(JsonRunResultsExporter.CODE.toLowerCase(), JsonRunResultsExporter.class);
        SUPPORTED_FILE_TYPES.put(PlainRunResultsExporter.CODE.toLowerCase(), PlainRunResultsExporter.class);
    }

    public RunResultsExporter interpret(String inputString) {
        Path outFilePath = Paths.get(inputString).toAbsolutePath();
        File file = outFilePath.toFile();
        String extension = FilenameUtils.getExtension(file.toString());
        if (extension == null || extension.equals("")) {
            throw new IllegalArgumentException("The input file has no extension.");
        }
        RunResultsExporter runResultsExporter;
        try {
            Class<? extends RunResultsExporter> aClass = SUPPORTED_FILE_TYPES.get(extension.toLowerCase());
            if (aClass == null) {
                throw new IllegalArgumentException("The file's extension supplied is not supported.");
            }
            Constructor<? extends RunResultsExporter> constructor = aClass.getConstructor(File.class);
            runResultsExporter = constructor.newInstance(file);
        } catch (InvocationTargetException | IllegalAccessException | InstantiationException |
                 NoSuchMethodException e) {
            throw new IllegalArgumentException("The file's extension supplied is not supported.");
        }
        return runResultsExporter;
    }
}
