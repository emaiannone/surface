package org.surface.surface.core.runners;

import org.surface.surface.core.filters.RevisionFilter;

import java.nio.file.Path;
import java.util.List;

public class FlexibleAnalysisRunner extends AnalysisRunner {
    private final Path cloneDirPath;
    private final RevisionFilter revisionFilter;

    public FlexibleAnalysisRunner(List<String> metrics, String target, Path outFilePath, String filesRegex, Path cloneDirPath, RevisionFilter revisionFilter) {
        super(metrics, target, outFilePath, filesRegex);
        this.cloneDirPath = cloneDirPath;
        this.revisionFilter = revisionFilter;
    }

    public Path getCloneDirPath() {
        return cloneDirPath;
    }

    public RevisionFilter getRevisionFilter() {
        return revisionFilter;
    }

    @Override
    public void run() {
        // TODO implement: read the YAML file (target) and understand what to do
    }
}
