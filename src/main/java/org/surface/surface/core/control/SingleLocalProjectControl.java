package org.surface.surface.core.control;

import org.surface.surface.data.exports.local.LocalProjectResultsExporter;
import org.surface.surface.results.ProjectMetricsResults;

import java.io.IOException;
import java.nio.file.Path;

public class SingleLocalProjectControl extends ProjectsControl {
    private final Path projectAbsolutePath;

    public SingleLocalProjectControl(String[] metricsCodes, String exportFormat, String outFile, Path projectAbsolutePath) {
        super(metricsCodes, exportFormat, outFile);
        this.projectAbsolutePath = projectAbsolutePath;
    }

    public Path getProjectAbsolutePath() {
        return projectAbsolutePath;
    }

    @Override
    public void run() {
        System.out.println("* Using " + projectAbsolutePath + " as project root.");
        // Analyze and compute metrics
        ProjectMetricsResults projectMetricsResults = super.processProject(projectAbsolutePath);
        // Export
        LocalProjectResultsExporter localProjectResultsExporter = new LocalProjectResultsExporter(projectMetricsResults);
        try {
            localProjectResultsExporter.export(getExportFormat(), getOutFile());
        } catch (IOException e) {
            System.out.println("* Could not export results.");
            e.printStackTrace();
        }
    }
}