package org.surface.surface.core.configuration.runners;

import org.surface.surface.core.engine.analysis.selectors.RevisionSelector;
import org.surface.surface.core.engine.analysis.setup.CloneSetupEnvironmentAction;
import org.surface.surface.core.engine.metrics.api.MetricsManager;
import org.surface.surface.core.exporters.RunResultsExporter;

import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.regex.Pattern;

public class RemoteGitRunningMode extends GitRunningMode {
    private static final String CODE_NAME = "REMOTE_GIT";

    public RemoteGitRunningMode(URI repoUrl, Path workDirPath, RunResultsExporter runResultsExporter, Set<Pattern> classifiedPatterns, MetricsManager metricsManager, RevisionSelector revisionSelector, String filesRegex, boolean includeTests) {
        super(workDirPath, runResultsExporter, classifiedPatterns, metricsManager, filesRegex, includeTests, revisionSelector, true);
        if (repoUrl == null) {
            throw new IllegalArgumentException("The URL to the target repository must not be null.");
        }
        if (metricsManager == null || metricsManager.getLoadedMetrics().size() == 0) {
            throw new IllegalArgumentException("The list of metrics to compute must not be null or empty.");
        }
        setProjectName(Paths.get(repoUrl.getPath()).getFileName().toString());
        setRepoLocation(repoUrl.toString());
        setCodeName(CODE_NAME);
        setSetupEnvironmentAction(new CloneSetupEnvironmentAction(getProjectName(), workDirPath, repoUrl));
    }
}
