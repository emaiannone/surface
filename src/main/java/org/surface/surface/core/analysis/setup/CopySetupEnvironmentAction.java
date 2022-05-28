package org.surface.surface.core.analysis.setup;

import org.apache.commons.io.FileUtils;
import org.surface.surface.common.Utils;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CopySetupEnvironmentAction extends SetupEnvironmentAction {

    public CopySetupEnvironmentAction(String projectName, String target, Path workDirPath) {
        super(projectName, target, workDirPath);
    }

    @Override
    public Path setupEnvironment() {
        // Ensure the target is a git repository, and then copy it into the temporary directory
        Path targetDirPath = Paths.get(getTarget()).toAbsolutePath();
        if (!Utils.isGitDirectory(targetDirPath.toFile())) {
            throw new IllegalStateException("The target directory " + targetDirPath + " does not exist or is not a git directory.");
        }

        Path tmpDirPath = getTmpDirPath();
        try {
            FileUtils.deleteDirectory(tmpDirPath.toFile());
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete the working directory " + tmpDirPath +
                    ". Please, try again or select a new destination.", e);
        }
        Path repoDirPath = getRepoDirPath();
        repoDirPath.toFile().mkdirs();
        try {
            FileUtils.copyDirectory(targetDirPath.toFile(), repoDirPath.toFile());
        } catch (IOException e) {
            throw new RuntimeException("Failed to copy the git repository into " + repoDirPath +
                    ". Please, try again or select a new destination.", e);
        }
        return tmpDirPath;
    }
}
