package org.surface.surface.core.analysis;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

public class ClassifiedPatterns {
    private static ClassifiedPatterns INSTANCE;
    private final String keywordsPath = "src/main/resources/keywords_regex";
    private List<Pattern> patterns;

    private ClassifiedPatterns() {
        try {
            Scanner scanner = new Scanner(new File(keywordsPath));
            patterns = new ArrayList<>();
            while (scanner.hasNext()) {
                String line = scanner.nextLine();
                String regex = ".*" + line + "[^\\s]*";
                Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
                patterns.add(pattern);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
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
