package org.surface.surface.core.interpreters;

import org.apache.commons.io.FilenameUtils;
import org.surface.surface.core.writers.FileWriter;
import org.surface.surface.core.writers.JsonFileWriter;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class OutFileInterpreter {
    private static final Map<String, Class<? extends FileWriter>> SUPPORTED_FILE_TYPES;
    static {
        // NOTE Any new file type must be added here to be recognized by the CLI parser
        SUPPORTED_FILE_TYPES = new HashMap<>();
        SUPPORTED_FILE_TYPES.put(JsonFileWriter.CODE.toLowerCase(), JsonFileWriter.class);
    }

    public static FileWriter interpretOutString(String outString) {
        Path outFilePath = Paths.get(outString).toAbsolutePath();
        File file = outFilePath.toFile();
        String extension = FilenameUtils.getExtension(file.toString());
        if (extension == null || extension.equals("")) {
            throw new IllegalArgumentException("The input file has no extension.");
        }
        FileWriter fileWriter;
        try {
            Class<? extends FileWriter> aClass = SUPPORTED_FILE_TYPES.get(extension.toLowerCase());
            if (aClass == null) {
                throw new IllegalArgumentException("The file's extension supplied is not supported.");
            }
            Constructor<? extends FileWriter> constructor = aClass.getConstructor(File.class);
            fileWriter = constructor.newInstance(file);
        } catch (InvocationTargetException | IllegalAccessException | InstantiationException |
                 NoSuchMethodException e) {
            throw new IllegalArgumentException("The file's extension supplied is not supported.");
        }
        return fileWriter;
    }
}
