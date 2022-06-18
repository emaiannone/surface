package org.surface.surface.core.configuration.runners;

import org.surface.surface.core.configuration.runners.results.GitRunResults;
import org.surface.surface.core.engine.analysis.selectors.RevisionSelector;
import org.surface.surface.core.engine.analysis.setup.CopySetupEnvironmentAction;
import org.surface.surface.core.engine.metrics.api.MetricsManager;
import org.surface.surface.core.engine.writers.FileWriter;

import java.nio.file.Path;

public class LocalGitRunningMode extends GitRunningMode {
    private static final String CODE_NAME = "LOCAL_GIT";

    public LocalGitRunningMode(Path repoPath, Path workDirPath, FileWriter writer, MetricsManager metricsManager, RevisionSelector revisionSelector, String filesRegex, boolean excludeWorkTree, boolean includeTests) {
        super(writer, metricsManager, revisionSelector, filesRegex, includeTests, excludeWorkTree);
        // TODO Duplicated with other runners: find a way to avoid this
        if (repoPath == null) {
            throw new IllegalArgumentException("The path to the target repository must not be null.");
        }
        if (workDirPath == null) {
            throw new IllegalArgumentException("The working directory must not be null.");
        }
        if (metricsManager == null || metricsManager.getLoadedMetrics().size() == 0) {
            throw new IllegalArgumentException("The list of metrics to compute must not be null.");
        }
        setProjectName(repoPath.getFileName().toString());
        setRepoLocation(repoPath.toString());
        setCodeName(CODE_NAME);
        setRunResults(new GitRunResults(getRepoLocation()));
        setSetupEnvironmentAction(new CopySetupEnvironmentAction(getProjectName(), workDirPath, repoPath));
    }

}
