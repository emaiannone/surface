package org.surface.surface.core.runners;

import org.surface.surface.common.filters.RevisionFilter;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class LocalGitAnalysisRunner extends AnalysisRunner {
    private final RevisionFilter revisionFilter;

    public LocalGitAnalysisRunner(List<String> metrics, String target, Path outFilePath, String filesRegex, RevisionFilter revisionFilter) {
        super(metrics, target, outFilePath, filesRegex);
        this.revisionFilter = revisionFilter;
    }

    public RevisionFilter getRevisionFilter() {
        return revisionFilter;
    }

    @Override
    public void run() {
        Path targetDirPath = Paths.get(getTarget()).toAbsolutePath();
        File targetDir = targetDirPath.toFile();
        if (!targetDir.exists() && !targetDir.isDirectory()) {
            throw new IllegalStateException("The target directory does not exist or is not a directory.");
        }
        // TODO Implement: same as localDirectory but apply the commits filter, use JGit for checkouts, and store all results in a Map<Commit, ProjectMetricsResults>
        // NOTE If you find a shared code with other run(), bring them to the superclass
    }
}
