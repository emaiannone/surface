package org.surface.surface.core.runners;

import org.surface.surface.core.analysis.selectors.RevisionSelector;
import org.surface.surface.core.analysis.setup.CloneSetupEnvironmentAction;
import org.surface.surface.core.metrics.api.Metric;
import org.surface.surface.core.out.exporters.GitProjectResultsExporter;
import org.surface.surface.core.out.writers.FileWriter;

import java.net.URI;
import java.nio.file.Path;
import java.util.List;

public class RemoteGitModeRunner extends GitModeRunner {
    private static final String CODE_NAME = "REMOTE_GIT";

    private final URI repoUrl;

    public RemoteGitModeRunner(URI repoUrl, List<Metric<?, ?>> metrics, FileWriter writer, String filesRegex, RevisionSelector revisionSelector, Path workDirPath) {
        super(metrics, writer, filesRegex, revisionSelector);
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
