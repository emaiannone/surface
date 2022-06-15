package org.surface.surface.core.configuration.runners;

import org.surface.surface.core.configuration.runners.results.GitRunResults;
import org.surface.surface.core.engine.analysis.selectors.RevisionSelector;
import org.surface.surface.core.engine.analysis.setup.CopySetupEnvironmentAction;
import org.surface.surface.core.engine.metrics.api.MetricsManager;
import org.surface.surface.core.engine.writers.FileWriter;

import java.nio.file.Path;

public class LocalGitRunningMode extends GitRunningMode {
    private static final String CODE_NAME = "LOCAL_GIT";

    public LocalGitRunningMode(Path repoPath, MetricsManager metricsManager, FileWriter writer, String filesRegex, boolean includeTests, boolean excludeWorkTree, RevisionSelector revisionSelector, Path workDirPath) {
        super(metricsManager, writer, filesRegex, includeTests, excludeWorkTree, revisionSelector);
        setProjectName(repoPath.getFileName().toString());
        setRepoLocation(repoPath.toString());
        setCodeName(CODE_NAME);
        setRunResults(new GitRunResults(getRepoLocation()));
        setSetupEnvironmentAction(new CopySetupEnvironmentAction(getProjectName(), workDirPath, repoPath));
    }

}
