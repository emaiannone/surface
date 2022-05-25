package org.surface.surface.core.runners;

import org.surface.surface.common.filters.RevisionFilter;
import org.surface.surface.core.metrics.results.ProjectMetricsResults;
import org.surface.surface.out.exporters.GitProjectResultsExporter;
import org.surface.surface.out.writers.Writer;
import org.surface.surface.out.writers.WriterFactory;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public class RemoteGitAnalysisRunner extends AnalysisRunner<Map<String, ProjectMetricsResults>> {
    private final Path cloneDirPath;
    private final RevisionFilter revisionFilter;

    public RemoteGitAnalysisRunner(List<String> metrics, String target, Path outFilePath, String filesRegex, Path cloneDirPath, RevisionFilter revisionFilter) {
        super(metrics, target, outFilePath, filesRegex);
        this.cloneDirPath = cloneDirPath;
        this.revisionFilter = revisionFilter;
        Writer writer = new WriterFactory().getWriter(getOutFilePath());
        setResultsExporter(new GitProjectResultsExporter(writer));
    }

    public Path getCloneDirPath() {
        return cloneDirPath;
    }

    public RevisionFilter getRevisionFilter() {
        return revisionFilter;
    }

    @Override
    public void run() {
        // Analogous to LocalGit, but with clone and deletion
    }
}
