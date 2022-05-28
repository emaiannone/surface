package org.surface.surface.core.runners;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.surface.surface.common.RevisionMode;
import org.surface.surface.core.analysis.HistoryAnalyzer;
import org.surface.surface.core.analysis.selectors.RevisionSelector;
import org.surface.surface.core.analysis.selectors.RevisionSelectorFactory;
import org.surface.surface.core.analysis.setup.SetupEnvironmentAction;
import org.surface.surface.core.metrics.results.ProjectMetricsResults;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

public abstract class GitModeRunner extends ModeRunner<Map<String, ProjectMetricsResults>> {
    private static final Logger LOGGER = LogManager.getLogger();

    private final RevisionSelector revisionSelector;
    private SetupEnvironmentAction setupEnvironmentAction;

    GitModeRunner(List<String> metrics, String target, Path outFilePath, String filesRegex, Pair<RevisionMode, String> revision) {
        super(metrics, target, outFilePath, filesRegex);
        this.revisionSelector = new RevisionSelectorFactory().selectRevisionSelector(revision);
    }

    void setSetupEnvironmentAction(SetupEnvironmentAction setupEnvironmentAction) {
        this.setupEnvironmentAction = setupEnvironmentAction;
    }

    @Override
    public void run() throws Exception {
        // TODO Move this logic into HistoryAnalyzer
        // Depends on the specific thing to do
        Path tmpDirPath = setupEnvironmentAction.setupEnvironment();
        Path repoDirPath = Paths.get(tmpDirPath.toString(), getProjectName());

        // In case of interrupts, clear the temporary directory
        SigIntHandler sigIntHandler = new SigIntHandler(tmpDirPath);
        Runtime.getRuntime().addShutdownHook(sigIntHandler);

        HistoryAnalyzer historyAnalyzer = new HistoryAnalyzer(repoDirPath, getFilesRegex(), getMetrics(), revisionSelector);
        Map<String, ProjectMetricsResults> allResults;
        try {
            allResults = historyAnalyzer.analyze();
        }
        finally {
            deleteTmpDirectory(tmpDirPath);
            Runtime.getRuntime().removeShutdownHook(sigIntHandler);
        }
        exportResults(allResults);
    }

    private void deleteTmpDirectory(Path tmpDirPath) throws IOException {
        if (tmpDirPath.toFile().exists()) {
            FileUtils.deleteDirectory(tmpDirPath.toFile());
        }
    }

    private class SigIntHandler extends Thread {
        private final Path tmpDirPath;

        private SigIntHandler(Path tmpDirPath) {
            this.tmpDirPath = tmpDirPath;
        }

        @Override
        public void run() {
            try {
                deleteTmpDirectory(tmpDirPath);
            } catch (IOException e) {
                LOGGER.warn("* Could not delete the working directory");
            }
        }
    }
}
