package org.surface.surface.common;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.UrlValidator;

import java.io.File;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Objects;

public class Utils {
    public static final String DOT_GIT = ".git";
    public static final String YML = "yml";
    public static final String YAML = "yaml";
    public static final String JAVA = "java";
    public static final String JSON = "json";
    public static final String GITHUB = "github.com";

    public static boolean isFile(File file) {
        return file.exists() && file.isFile();
    }

    public static boolean isDirectory(File file) {
        return file.exists() && file.isDirectory();
    }

    public static boolean isGitDirectory(File file) {
        return isDirectory(file) &&
                Arrays.stream(Objects.requireNonNull(file.listFiles())).anyMatch(dir -> dir.getName().equals(DOT_GIT));
    }

    public static boolean hasYamlExtension(Path path) {
        String ext = FilenameUtils.getExtension(path.toString());
        return ext.equalsIgnoreCase(YAML) || ext.equalsIgnoreCase(YML);
    }

    public static boolean isYamlFile(File file) {
        return isFile(file) && hasYamlExtension(file.toPath());
    }

    public static boolean hasJsonExtension(Path path) {
        return FilenameUtils.getExtension(path.toString()).equalsIgnoreCase(JSON);
    }

    public static boolean isJsonFile(File file) {
        return isFile(file) && hasJsonExtension(file.toPath());
    }

    public static boolean isJavaFile(File file) {
        return isFile(file) && FilenameUtils.getExtension(file.getName()).equalsIgnoreCase(JAVA);
    }

    public static boolean isGitHubUrl(String urlString) {
        return UrlValidator.getInstance().isValid(urlString) && urlString.contains(GITHUB);
    }

    public static String[] getRevisionsFromRange(String revisionRange) {
        String[] parts = revisionRange.split("\\.\\.");
        if (StringUtils.countMatches(revisionRange, '.') != 2 || parts.length != 2) {
            throw new IllegalArgumentException("The revision range must fulfill the expected format (\"<START-SHA>..<END-SHA>\").");
        }
        if (Utils.isAlphaNumeric(parts[0]) && Utils.isAlphaNumeric(parts[1])) {
            return parts;
        } else {
            throw new IllegalArgumentException("The revisions must be alphanumeric strings.");
        }
    }

    public static boolean isAlphaNumeric(String string) {
        String revisionRegex = "[a-zA-Z\\d]+";
        return string.matches(revisionRegex);
    }
}
