package org.surface.surface.core.configuration.runners;

import org.surface.surface.core.configuration.runners.results.GitRunResults;
import org.surface.surface.core.engine.analysis.selectors.RevisionSelector;
import org.surface.surface.core.engine.analysis.setup.CloneSetupEnvironmentAction;
import org.surface.surface.core.engine.metrics.api.MetricsManager;
import org.surface.surface.core.engine.writers.FileWriter;

import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;

public class RemoteGitRunningMode extends GitRunningMode {
    private static final String CODE_NAME = "REMOTE_GIT";

    public RemoteGitRunningMode(URI repoUrl, Path workDirPath, FileWriter writer, MetricsManager metricsManager, RevisionSelector revisionSelector, String filesRegex, boolean includeTests) {
        super(writer, metricsManager, revisionSelector, filesRegex, includeTests, true);
        if (repoUrl == null) {
            throw new IllegalArgumentException("The URL to the target repository must not be null.");
        }
        if (workDirPath == null) {
            throw new IllegalArgumentException("The working directory must not be null.");
        }
        if (metricsManager == null || metricsManager.getLoadedMetrics().size() == 0) {
            throw new IllegalArgumentException("The list of metrics to compute must not be null.");
        }
        setProjectName(Paths.get(repoUrl.getPath()).getFileName().toString());
        setRepoLocation(repoUrl.toString());
        setCodeName(CODE_NAME);
        setRunResults(new GitRunResults(getRepoLocation()));
        setSetupEnvironmentAction(new CloneSetupEnvironmentAction(getProjectName(), workDirPath, repoUrl));
    }
}
