package org.surface.surface.core.runners;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.surface.surface.common.selectors.RevisionSelector;
import org.surface.surface.core.analysis.HistoryAnalyzer;
import org.surface.surface.core.metrics.results.ProjectMetricsResults;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

public abstract class GitModeRunner extends ModeRunner<Map<String, ProjectMetricsResults>> {
    private static final Logger LOGGER = LogManager.getLogger();

    private static final String SURFACE_TMP = "SURFACE_TMP";

    private final Path workDirPath;
    private final RevisionSelector revisionSelector;

    GitModeRunner(List<String> metrics, String target, Path outFilePath, String filesRegex, Path workDirPath, RevisionSelector revisionSelector) {
        super(metrics, target, outFilePath, filesRegex);
        this.workDirPath = workDirPath;
        this.revisionSelector = revisionSelector;
    }

    private String getProjectName() {
        return getTargetPath().getFileName().toString();
    }

    public Path getWorkDirPath() {
        return workDirPath;
    }

    Path getTmpDirPath() {
        return Paths.get(workDirPath.toString(), SURFACE_TMP);
    }

    Path getRepoDirPath() {
        return Paths.get(getTmpDirPath().toString(), getProjectName());
    }

    protected abstract Path prepareTmpDir();

    @Override
    public void run() throws Exception {
        // Depends on the specific thing to do
        Path tmpDirPath = prepareTmpDir();
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

    void deleteTmpDirectory(Path tmpDirPath) throws IOException {
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
