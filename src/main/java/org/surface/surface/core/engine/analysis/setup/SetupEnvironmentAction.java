package org.surface.surface.core.engine.analysis.setup;

import java.nio.file.Path;
import java.nio.file.Paths;

public abstract class SetupEnvironmentAction {
    private static final String SURFACE_TMP = "SURFACE_TMP";

    private final String projectName;
    private final Path workDirPath;

    SetupEnvironmentAction(String projectName, Path workDirPath) {
        this.projectName = projectName;
        this.workDirPath = workDirPath;
    }

    public Path getWorkDirPath() {
        return workDirPath;
    }

    Path getTmpDirPath() {
        return Paths.get(workDirPath.toString(), SURFACE_TMP);
    }

    Path getRepoDirPath() {
        return Paths.get(getTmpDirPath().toString(), projectName);
    }

    public abstract Path setupEnvironment();
}
