package org.surface.surface.core.configuration.runners;

import org.surface.surface.core.configuration.runners.results.GitRunResults;
import org.surface.surface.core.engine.analysis.selectors.RevisionSelector;
import org.surface.surface.core.engine.analysis.setup.CloneSetupEnvironmentAction;
import org.surface.surface.core.engine.metrics.api.MetricsManager;
import org.surface.surface.core.engine.writers.FileWriter;

import java.net.URI;
import java.nio.file.Path;

public class RemoteGitRunningMode extends GitRunningMode {
    private static final String CODE_NAME = "REMOTE_GIT";

    private final URI repoUrl;

    public RemoteGitRunningMode(URI repoUrl, MetricsManager metricsManager, FileWriter writer, String filesRegex, boolean includeTests, RevisionSelector revisionSelector, Path workDirPath) {
        super(metricsManager, writer, filesRegex, includeTests, revisionSelector);
        this.repoUrl = repoUrl;
        setProjectName(repoUrl.getPath().substring(repoUrl.getPath().lastIndexOf('/')+1));
        setRepoLocation(repoUrl.toString());
        setCodeName(CODE_NAME);
        setRunResults(new GitRunResults(getRepoLocation()));
        setSetupEnvironmentAction(new CloneSetupEnvironmentAction(getProjectName(), workDirPath, repoUrl));
    }
}
