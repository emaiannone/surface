package org.surface.surface.core.inspection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.surface.surface.core.Utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JavaFilesExplorer {
    private static final Logger LOGGER = LogManager.getLogger();

    public static List<Path> selectFiles(Path startDir, String regex) throws IOException {
        final Pattern compiledRegex;
        Pattern compiledRegexTmp;
        try {
            compiledRegexTmp = Pattern.compile(regex);
        } catch (Exception e) {
            compiledRegexTmp = Pattern.compile(".*", Pattern.DOTALL);
        }
        compiledRegex = compiledRegexTmp;
        try (Stream<Path> fileStream = Files.walk(startDir)) {
            List<Path> files = fileStream
                    .filter(f -> Utils.isJavaFile(f.toFile()))
                    .filter(f -> compiledRegex.matcher(f.toFile().toString()).find())
                    .collect(Collectors.toList());
            return files;
        }
    }
}
