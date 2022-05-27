package org.surface.surface.cli;

import org.surface.surface.common.RunMode;
import org.surface.surface.common.Utils;

import java.io.File;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;

class RunModeSelector {

    public static RunMode inferMode(String target) {
        if (Utils.isGitHubUrl(target)) {
            return RunMode.REMOTE_GIT;
        }
        try {
            File file = Paths.get(target).toAbsolutePath().toFile();
            if (file.exists()) {
                if (file.isDirectory()) {
                    if (Utils.isGitDirectory(file)) {
                        return RunMode.LOCAL_GIT;
                    }
                    return RunMode.LOCAL_DIR;
                } else if (file.isFile()) {
                    if (Utils.isYamlFile(file)) {
                        return RunMode.FLEXIBLE;
                    }
                }
            }
        } catch (InvalidPathException ignored) {
        }
        throw new IllegalArgumentException("The supplied target must be (i) a local non-git directory (LOCAL), (ii) a local git directory (LOCAL_GIT), (iii) a remote URL to a GitHub repository (REMOTE_GIT), or (iv) a local path to a YAML file (FLEXIBLE).");
    }
}
