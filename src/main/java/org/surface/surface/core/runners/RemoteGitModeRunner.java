package org.surface.surface.core.runners;

import org.surface.surface.core.analysis.selectors.RevisionSelector;
import org.surface.surface.core.analysis.setup.CloneSetupEnvironmentAction;
import org.surface.surface.core.metrics.api.MetricsManager;
import org.surface.surface.core.out.exporters.GitProjectResultsExporter;
import org.surface.surface.core.out.writers.FileWriter;

import java.net.URI;
import java.nio.file.Path;

public class RemoteGitModeRunner extends GitModeRunner {
    private static final String CODE_NAME = "REMOTE_GIT";

    private final URI repoUrl;

    public RemoteGitModeRunner(URI repoUrl, MetricsManager metricsManager, FileWriter writer, String filesRegex, boolean includeTests, RevisionSelector revisionSelector, Path workDirPath) {
        super(metricsManager, writer, filesRegex, includeTests, revisionSelector);
        this.repoUrl = repoUrl;
        setCodeName(CODE_NAME);
        setResultsExporter(new GitProjectResultsExporter(repoUrl.toString()));
        setSetupEnvironmentAction(new CloneSetupEnvironmentAction(getProjectName(), workDirPath, repoUrl));
    }

    @Override
    String getProjectName() {
        String path = repoUrl.getPath();
        return path.substring(path.lastIndexOf('/') + 1);
    }
}
