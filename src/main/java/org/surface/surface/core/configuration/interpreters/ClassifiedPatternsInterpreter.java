package org.surface.surface.core.configuration.interpreters;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class ClassifiedPatternsInterpreter implements InputStringInterpreter<Set<Pattern>> {

    @Override
    public Set<Pattern> interpret(String inputString) {
        if (inputString == null || inputString.isEmpty()) {
            throw new IllegalArgumentException("The classified pattern file path is null or empty.");
        }
        Path patternPath = Paths.get(inputString).toAbsolutePath();
        File patternFile = patternPath.toFile();
        if (!patternFile.exists()) {
            throw new IllegalArgumentException("The classified pattern file does not exist.");
        }
        if (patternFile.length() == 0) {
            throw new IllegalArgumentException("The classified pattern file is empty.");
        }
        try (InputStream fileStream = Files.newInputStream(patternFile.toPath())) {
            Scanner scanner = new Scanner(fileStream);
            Set<Pattern> patterns = new LinkedHashSet<>();
            while (scanner.hasNext()) {
                String line = scanner.nextLine();
                if (line.trim().isEmpty()) {
                    continue;
                }
                try {
                    Pattern pattern = Pattern.compile(line, Pattern.CASE_INSENSITIVE);
                    patterns.add(pattern);
                } catch (PatternSyntaxException ignored) {
                }
            }
            return patterns;
        } catch (IOException e) {
            throw new IllegalArgumentException("The classified pattern file cannot be read.");
        }
    }
}
