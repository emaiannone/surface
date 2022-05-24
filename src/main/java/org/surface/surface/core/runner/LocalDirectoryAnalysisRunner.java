package org.surface.surface.core.runner;

import org.apache.commons.lang3.tuple.Pair;
import org.surface.surface.core.RunMode;
import org.surface.surface.core.explorer.JavaFilesExplorer;
import org.surface.surface.results.ProjectMetricsResults;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class LocalDirectoryAnalysisRunner extends AnalysisRunner {

    public LocalDirectoryAnalysisRunner(List<String> metrics, String target, File outFile, String filesRegex) {
        super(metrics, target, outFile, filesRegex);
    }

    @Override
    public void run() throws IOException {
        File projectDir = new File(getTarget());
        if (!projectDir.exists() && !projectDir.isDirectory()) {
            throw new IllegalStateException("The target directory does not exist or is not a directory.");
        }
        List<Path> files = getFilesRegex() == null ?
                JavaFilesExplorer.selectFiles(projectDir) :
                JavaFilesExplorer.selectFiles(projectDir, getFilesRegex());
        ProjectMetricsResults projectMetricsResults = super.analyzeProject(projectDir, files);
        exportProjectResults(projectMetricsResults);
    }
}
