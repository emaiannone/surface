package org.surface.surface.core.parsers;

import org.apache.commons.validator.routines.UrlValidator;
import org.surface.surface.common.RunMode;
import org.surface.surface.common.Utils;

import java.io.File;
import java.nio.file.Paths;

public class TargetParser {
    private static final String GITHUB = "github.com";

    // NOTE Any new run mode must be added here to be recognized by the CLI parser
    public static RunMode parseTargetString(String target) {
        if (!TargetParser.validateTargetString(target)) {
            throw new IllegalArgumentException("The supplied target is not attributable to a supported run mode.");
        }
        if (TargetParser.isLocalDirectory(target)) {
            return RunMode.LOCAL_DIR;
        }
        if (TargetParser.isGitDirectory(target)) {
            return RunMode.LOCAL_GIT;
        }
        if (TargetParser.isGitHubUrl(target)) {
            return RunMode.REMOTE_GIT;
        }
        if (TargetParser.isYamlFile(target)) {
            return RunMode.FLEXIBLE;
        }
        return null;
    }

    public static boolean isLocalDirectory(String target) {
        File file = Paths.get(target).toAbsolutePath().toFile();
        return (file.isDirectory() && !Utils.isGitDirectory(file));
    }

    public static boolean isGitDirectory(String target) {
        File file = Paths.get(target).toAbsolutePath().toFile();
        return (file.isDirectory() && Utils.isGitDirectory(file));
    }

    public static boolean isGitHubUrl(String target) {
        return UrlValidator.getInstance().isValid(target) && target.contains(GITHUB);
    }

    public static boolean isYamlFile(String target) {
        File file = Paths.get(target).toAbsolutePath().toFile();
        return Utils.isYamlFile(file);
    }

    public static boolean validateTargetString(String target) {
        return isLocalDirectory(target) ||
                isGitDirectory(target) ||
                isGitHubUrl(target) ||
                isYamlFile(target);
    }
}
