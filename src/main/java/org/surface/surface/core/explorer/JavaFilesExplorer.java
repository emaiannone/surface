package org.surface.surface.core.explorer;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JavaFilesExplorer {
    public static List<Path> selectFiles(File startDir) throws IOException {
        try (Stream<Path> fileStream = Files.walk(Paths.get(startDir.getAbsolutePath()))) {
            return fileStream.filter(Files::isRegularFile)
                    .filter(f -> FilenameUtils.getExtension(f.getFileName().toString()).equals("java"))
                    .collect(Collectors.toList());
        }
    }

    public static List<Path> selectFiles(File startDir, String regex) throws IOException {
        return selectFiles(startDir)
                .stream()
                .filter(p -> p.getFileName().toString().matches(regex))
                .collect(Collectors.toList());
    }
}
