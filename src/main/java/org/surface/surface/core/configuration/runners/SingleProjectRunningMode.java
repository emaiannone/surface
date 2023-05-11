package org.surface.surface.core.configuration.runners;

import org.surface.surface.core.engine.analysis.setup.SetupEnvironmentAction;
import org.surface.surface.core.engine.metrics.api.MetricsManager;
import org.surface.surface.core.exporters.RunResultsExporter;

import java.nio.file.Path;
import java.util.Set;
import java.util.regex.Pattern;

public abstract class SingleProjectRunningMode extends RunningMode {
    private final Path workDirPath;
    private String projectName;
    private SetupEnvironmentAction setupEnvironmentAction;

    SingleProjectRunningMode(Path workDirPath, RunResultsExporter runResultsExporter, Set<Pattern> classifiedPatterns, MetricsManager metricsManager, String filesRegex, boolean includeTests) {
        super(runResultsExporter, classifiedPatterns, metricsManager, filesRegex, includeTests);
        if (workDirPath == null) {
            throw new IllegalArgumentException("The working directory must not be null.");
        }
        this.workDirPath = workDirPath;
    }

    public Path getWorkDirPath() {
        return workDirPath;
    }

    public SetupEnvironmentAction getSetupEnvironmentAction() {
        return setupEnvironmentAction;
    }

    public void setSetupEnvironmentAction(SetupEnvironmentAction setupEnvironmentAction) {
        this.setupEnvironmentAction = setupEnvironmentAction;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
}
