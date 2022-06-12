package org.surface.surface.core.runners;

import org.surface.surface.core.analysis.selectors.RevisionSelector;
import org.surface.surface.core.analysis.setup.CloneSetupEnvironmentAction;
import org.surface.surface.core.metrics.api.MetricsManager;
import org.surface.surface.core.runners.results.GitRunResults;
import org.surface.surface.core.writers.FileWriter;

import java.net.URI;
import java.nio.file.Path;

public class RemoteGitModeRunner extends GitModeRunner {
    private static final String CODE_NAME = "REMOTE_GIT";

    private final URI repoUrl;

    public RemoteGitModeRunner(URI repoUrl, MetricsManager metricsManager, FileWriter writer, String filesRegex, boolean includeTests, RevisionSelector revisionSelector, Path workDirPath) {
        super(metricsManager, writer, filesRegex, includeTests, revisionSelector);
        this.repoUrl = repoUrl;
        setProjectName(repoUrl.getPath().substring(repoUrl.getPath().lastIndexOf('/')+1));
        setRepoLocation(repoUrl.toString());
        setCodeName(CODE_NAME);
        setRunResults(new GitRunResults(getRepoLocation()));
        setSetupEnvironmentAction(new CloneSetupEnvironmentAction(getProjectName(), workDirPath, repoUrl));
    }
}
