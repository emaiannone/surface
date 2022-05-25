package org.surface.surface.core.explorers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.surface.surface.common.Utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JavaFilesExplorer {
    private static final Logger LOGGER = LogManager.getLogger();

    public static List<Path> selectFiles(Path startDir) throws IOException {
        try (Stream<Path> fileStream = Files.walk(startDir)) {
            List<Path> files = fileStream.filter(f -> Utils.isJavaFile(f.toFile()))
                    .collect(Collectors.toList());
            LOGGER.debug("All Java files found: {}", files);
            return files;
        }
    }

    public static List<Path> selectFiles(Path startDir, String regex) throws IOException {
        List<Path> files = selectFiles(startDir)
                .stream()
                .filter(p -> p.getFileName().toString().matches(regex))
                .collect(Collectors.toList());
        LOGGER.debug("Java files post regex filter: {}", files);
        return files;
    }
}
