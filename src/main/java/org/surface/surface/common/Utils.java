package org.surface.surface.common;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.validator.routines.UrlValidator;
import org.eclipse.jgit.util.FileUtils;

import java.io.File;
import java.io.IOException;
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

    public static boolean isYamlFile(File file) {
        String extension = FilenameUtils.getExtension(file.getName());
        return isFile(file) && (extension.equalsIgnoreCase(YML) || extension.equalsIgnoreCase(YAML));
    }

    public static boolean isJsonFile(File file) {
        return isFile(file) && FilenameUtils.getExtension(file.getName()).equalsIgnoreCase(JSON);
    }

    public static boolean isJavaFile(File file) {
        return isFile(file) && FilenameUtils.getExtension(file.getName()).equalsIgnoreCase(JAVA);
    }

    public static boolean isGitHubUrl(String urlString) {
        return UrlValidator.getInstance().isValid(urlString) && urlString.contains(GITHUB);
    }

    public static boolean isAlphaNumeric(String string) {
        String revisionRegex = "[a-zA-Z\\d]+";
        return string.matches(revisionRegex);
    }

    private void deleteFilesInDir(File dir) {
        if (isDirectory(dir)) {
            try {
                FileUtils.delete(dir, FileUtils.RECURSIVE);
            } catch (IOException ignored) {
            }
        }
    }
}
