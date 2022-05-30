package org.surface.surface.core.runners;

import org.apache.commons.lang3.tuple.Pair;
import org.surface.surface.common.RevisionMode;
import org.surface.surface.core.analysis.setup.CloneSetupEnvironmentAction;
import org.surface.surface.core.out.exporters.GitProjectResultsExporter;
import org.surface.surface.core.out.writers.Writer;

import java.nio.file.Path;
import java.util.List;

class RemoteGitModeRunner extends GitModeRunner {

    RemoteGitModeRunner(List<String> metrics, String target, Path outFilePath, String filesRegex, Pair<RevisionMode, String> revision, Path workDirPath) {
        super(metrics, target, outFilePath, filesRegex, revision);
        Writer writer = Writer.newWriter(getOutFilePath());
        setResultsExporter(new GitProjectResultsExporter(writer, target));
        setSetupEnvironmentAction(new CloneSetupEnvironmentAction(getProjectName(), target, workDirPath));
    }
}
