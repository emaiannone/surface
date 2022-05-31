package org.surface.surface.core.runners;

import org.surface.surface.core.analysis.selectors.RevisionSelector;
import org.surface.surface.core.analysis.setup.CopySetupEnvironmentAction;
import org.surface.surface.core.metrics.api.Metric;
import org.surface.surface.core.out.exporters.GitProjectResultsExporter;
import org.surface.surface.core.out.writers.FileWriter;

import java.nio.file.Path;
import java.util.List;

public class LocalGitModeRunner extends GitModeRunner {
    private static final String CODE_NAME = "LOCAL_GIT";

    private final Path repoPath;

    public LocalGitModeRunner(Path repoPath, List<Metric<?, ?>> metrics, FileWriter writer, String filesRegex, RevisionSelector revisionSelector, Path workDirPath) {
        super(metrics, writer, filesRegex, revisionSelector);
        this.repoPath = repoPath;
        setCodeName(CODE_NAME);
        setResultsExporter(new GitProjectResultsExporter(null));
        setSetupEnvironmentAction(new CopySetupEnvironmentAction(getProjectName(), workDirPath, repoPath));
    }


    @Override
    String getProjectName() {
        return repoPath.getFileName().toString();
    }
}
