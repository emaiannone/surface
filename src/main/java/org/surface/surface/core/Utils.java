package org.surface.surface.core;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.validator.routines.UrlValidator;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Utils {
    private static final String DOT_GIT = ".git";
    private static final String YML = "yml";
    private static final String YAML = "yaml";
    private static final String JAVA = "java";
    private static final String GITHUB = "github.com";

    public static boolean isGitDirectory(File file) {
        return file.isDirectory() &&
                Arrays.stream(Objects.requireNonNull(file.listFiles())).anyMatch(dir -> dir.getName().equals(DOT_GIT));
    }

    public static boolean isPathToLocalDirectory(Path target) {
        File file = target.toAbsolutePath().toFile();
        return (file.isDirectory() && !Utils.isGitDirectory(file));
    }

    public static boolean isPathToGitDirectory(Path target) {
        File file = target.toAbsolutePath().toFile();
        return (file.isDirectory() && Utils.isGitDirectory(file));
    }

    public static boolean isYamlFile(File file) {
        String ext = FilenameUtils.getExtension(file.toPath().toString());
        return file.isFile() && ext.equalsIgnoreCase(YAML) || ext.equalsIgnoreCase(YML);
    }

    public static boolean isPathToYamlFile(Path target) {
        File file = target.toAbsolutePath().toFile();
        return Utils.isYamlFile(file);
    }

    public static boolean isJavaFile(File file) {
        return file.isFile() && FilenameUtils.getExtension(file.getName()).equalsIgnoreCase(JAVA);
    }

    public static List<String> getExceptionMessageChain(Throwable throwable) {
        List<String> result = new ArrayList<>();
        while (throwable != null) {
            result.add(throwable.getMessage());
            throwable = throwable.getCause();
        }
        return result;
    }

    public static boolean isGitHubUrl(String target) {
        return UrlValidator.getInstance().isValid(target) && target.contains(GITHUB);
    }
}
