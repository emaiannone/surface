package org.surface.surface.core.engine.inspection;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

public class ClassifiedPatterns {
    private static ClassifiedPatterns INSTANCE;
    private List<Pattern> patterns;

    private ClassifiedPatterns() {
        try {
            try (InputStream patternsIS = getClass().getClassLoader().getResourceAsStream("patterns")) {
                if (patternsIS == null) {
                    throw new IOException();
                }
                Scanner scanner = new Scanner(patternsIS);
                patterns = new ArrayList<>();
                while (scanner.hasNext()) {
                    String line = scanner.nextLine();
                    String regex = ".*" + line + "[^\\s]*";
                    Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
                    patterns.add(pattern);
                }
            }
        } catch (IOException ignored) {
        }
    }

    public static ClassifiedPatterns getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ClassifiedPatterns();
        }
        return INSTANCE;
    }

    public List<Pattern> getPatterns() {
        return patterns;
    }
}
