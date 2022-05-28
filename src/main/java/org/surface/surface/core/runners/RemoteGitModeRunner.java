package org.surface.surface.core.runners;

import org.apache.commons.lang3.tuple.Pair;
import org.surface.surface.common.RevisionMode;
import org.surface.surface.core.analysis.setup.CloneSetupEnvironmentAction;
import org.surface.surface.out.exporters.GitProjectResultsExporter;
import org.surface.surface.out.writers.Writer;
import org.surface.surface.out.writers.WriterFactory;

import java.nio.file.Path;
import java.util.List;

class RemoteGitModeRunner extends GitModeRunner {

    RemoteGitModeRunner(List<String> metrics, String target, Path outFilePath, String filesRegex, Pair<RevisionMode, String> revision, Path workDirPath) {
        super(metrics, target, outFilePath, filesRegex, revision);
        Writer writer = new WriterFactory().getWriter(getOutFilePath());
        setResultsExporter(new GitProjectResultsExporter(writer, target));
        setSetupEnvironmentAction(new CloneSetupEnvironmentAction(getProjectName(), target, workDirPath));
    }
}
