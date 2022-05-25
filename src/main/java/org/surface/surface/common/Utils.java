package org.surface.surface.common;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.validator.routines.UrlValidator;

import java.io.File;
import java.util.Arrays;
import java.util.Objects;

public class Utils {

    public static boolean isGitDirectory(File file) {
        return Arrays.stream(Objects.requireNonNull(file.listFiles())).anyMatch(dir -> dir.getName().equals(".git"));
    }

    public static boolean isYamlFile(File file) {
        String extension = FilenameUtils.getExtension(file.getName());
        return extension.equalsIgnoreCase("yml") || extension.equalsIgnoreCase("yaml");
    }

    public static boolean isJsonFile(File file) {
        return FilenameUtils.getExtension(file.getName()).equalsIgnoreCase("json");
    }

    public static boolean isGitHubUrl(String urlString) {
        return UrlValidator.getInstance().isValid(urlString) && urlString.contains("github.com");
    }

    public static boolean isAlphaNumeric(String string) {
        String revisionRegex = "[a-zA-Z\\d]+";
        return string.matches(revisionRegex);
    }
}
