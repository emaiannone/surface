package org.surface.surface.core.analysis.setup;

import org.apache.commons.io.FileUtils;
import org.surface.surface.core.Utils;

import java.io.IOException;
import java.nio.file.Path;

public class CopySetupEnvironmentAction extends SetupEnvironmentAction {

    private final Path srcDirPath;

    public CopySetupEnvironmentAction(String projectName, Path destDirPath, Path srcDirPath) {
        super(projectName, destDirPath);
        this.srcDirPath = srcDirPath;
    }

    @Override
    public Path setupEnvironment() {
        // Ensure the target is a git repository, and then copy it into the temporary directory
        if (!Utils.isGitDirectory(srcDirPath.toFile())) {
            throw new IllegalStateException("The target directory " + srcDirPath + " does not exist or is not a git directory.");
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
            FileUtils.copyDirectory(srcDirPath.toFile(), repoDirPath.toFile());
        } catch (IOException e) {
            throw new RuntimeException("Failed to copy the git repository into " + repoDirPath +
                    ". Please, try again or select a new destination.", e);
        }
        return tmpDirPath;
    }
}
