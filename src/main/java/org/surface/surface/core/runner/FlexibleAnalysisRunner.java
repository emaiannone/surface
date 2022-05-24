package org.surface.surface.core.runner;

import org.surface.surface.core.filter.RevisionFilter;

import java.io.File;
import java.util.List;

public class FlexibleAnalysisRunner extends AnalysisRunner {
    private final File cloneDir;
    private final RevisionFilter revisionFilter;

    public FlexibleAnalysisRunner(List<String> metrics, String target, File outFile, String filesRegex, File cloneDir, RevisionFilter revisionFilter) {
        super(metrics, target, outFile, filesRegex);
        this.cloneDir = cloneDir;
        this.revisionFilter = revisionFilter;
    }

    public File getCloneDir() {
        return cloneDir;
    }

    public RevisionFilter getRevisionFilter() {
        return revisionFilter;
    }

    @Override
    public void run() {
        // TODO implement
    }
}
