package org.surface.surface.core.runners;

import org.surface.surface.core.analysis.selectors.RevisionSelector;
import org.surface.surface.core.analysis.setup.CopySetupEnvironmentAction;
import org.surface.surface.core.metrics.api.MetricsManager;
import org.surface.surface.core.runners.results.GitRunResults;
import org.surface.surface.core.writers.FileWriter;

import java.nio.file.Path;

public class LocalGitModeRunner extends GitModeRunner {
    private static final String CODE_NAME = "LOCAL_GIT";

    public LocalGitModeRunner(Path repoPath, MetricsManager metricsManager, FileWriter writer, String filesRegex, boolean includeTests, RevisionSelector revisionSelector, Path workDirPath) {
        super(metricsManager, writer, filesRegex, includeTests, revisionSelector);
        setProjectName(repoPath.getFileName().toString());
        setRepoLocation(repoPath.toString());
        setCodeName(CODE_NAME);
        setRunResults(new GitRunResults(getRepoLocation()));
        setSetupEnvironmentAction(new CopySetupEnvironmentAction(getProjectName(), workDirPath, repoPath));
    }

}
