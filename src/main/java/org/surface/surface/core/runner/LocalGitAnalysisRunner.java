package org.surface.surface.core.runner;

import org.surface.surface.core.filter.RevisionFilter;

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
        // TODO Implement
    }
}
