package org.surface.surface.core.analysis.setup;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Path;

public class CloneSetupEnvironmentAction extends SetupEnvironmentAction {
    private static final Logger LOGGER = LogManager.getLogger();

    private final URI cloneUrl;

    public CloneSetupEnvironmentAction(String projectName, Path destDirPath, URI cloneUrl) {
        super(projectName, destDirPath);
        this.cloneUrl = cloneUrl;
    }

    @Override
    public Path setupEnvironment() {
        // Clone the repository into the temporary directory
        Path tmpDirPath = getTmpDirPath();
        try {
            FileUtils.deleteDirectory(tmpDirPath.toFile());
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete the working directory " + tmpDirPath +
                    ". Please, try again or select a new destination.", e);
        }
        Path repoDirPath = getRepoDirPath();
        repoDirPath.toFile().mkdirs();
        LOGGER.info("* Cloning {} into {}", cloneUrl, repoDirPath);
        try (Git git = Git.cloneRepository()
                .setURI(cloneUrl.toString())
                .setDirectory(repoDirPath.toFile())
                .call()) {
            LOGGER.debug("* Clone successful");
        } catch (GitAPIException e) {
            throw new RuntimeException("Failed to clone the remote git repository from " + cloneUrl +
                    " into " + tmpDirPath +
                    ". Please, check if URL is spelled correctly or try a new remote URL.", e);
        }
        return tmpDirPath;
    }
}
