package org.surface.surface.core.analysis.setup;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.IOException;
import java.nio.file.Path;

public class CloneSetupEnvironmentAction extends SetupEnvironmentAction {
    private static final Logger LOGGER = LogManager.getLogger();

    public CloneSetupEnvironmentAction(String projectName, String target, Path workDirPath) {
        super(projectName, target, workDirPath);
    }

    @Override
    public Path setupEnvironment() {
        // Clone the repository into the temporary directory
        Path tmpDirPath = getTmpDirPath();
        try {
            deleteTmpDirectory(tmpDirPath);
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete the working directory " + tmpDirPath +
                    ". Please, try again or select a new destination.", e);
        }
        Path repoDirPath = getRepoDirPath();
        repoDirPath.toFile().mkdirs();
        LOGGER.info("* Cloning {} into {}", getTarget(), repoDirPath);
        try (Git git = Git.cloneRepository()
                .setURI(getTarget())
                .setDirectory(repoDirPath.toFile())
                .call()) {
            LOGGER.info("* Clone successful");
        } catch (GitAPIException e) {
            throw new RuntimeException("Failed to clone the remote git repository from " + getTarget() +
                    " into " + tmpDirPath +
                    ". Please, try again or select a new destination.", e);
        }
        return tmpDirPath;
    }
}
