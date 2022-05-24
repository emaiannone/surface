package org.surface.surface.cli;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.validator.routines.UrlValidator;
import org.surface.surface.core.RunMode;

import java.io.File;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Objects;

public class RunModeSelector {

    public static RunMode inferMode(String target) {
        if (UrlValidator.getInstance().isValid(target) && target.contains("github.com")) {
            return RunMode.REMOTE_GIT;
        }
        try {
            File file = Paths.get(target).toAbsolutePath().toFile();
            if (file.exists()) {
                if (file.isDirectory()) {
                    boolean isGit = Arrays.stream(Objects.requireNonNull(file.listFiles())).anyMatch(dir -> dir.getName().equals(".git"));
                    if (isGit) {
                        return RunMode.LOCAL_GIT;
                    }
                    return RunMode.LOCAL_DIR;
                } else if (file.isFile()) {
                    String extension = FilenameUtils.getExtension(file.getName());
                    if (extension.equalsIgnoreCase("yml")
                            || extension.equalsIgnoreCase("yaml")) {
                        return RunMode.FLEXIBLE;
                    }
                }
            }
        } catch (InvalidPathException ignored) {
        }
        throw new IllegalArgumentException("The supplied target must be (i) a local non-git directory (LOCAL), (ii) a local git directory (LOCAL_GIT), (iii) a remote URL to a GitHub repository (REMOTE_GIT), or (iv) a local path to a YAML file (FLEXIBLE).");
    }
}
