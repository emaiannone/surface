package org.surface.surface.core.runners;

import org.surface.surface.core.filters.RevisionFilter;

import java.nio.file.Path;
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
        // TODO Implement: same as localDirectory but apply the commits filter, use JGit for checkouts, and store all results in a Map<Commit, ProjectMetricsResults>
        // NOTE If you find a shared code with other run(), bring them to the superclass
    }
}
