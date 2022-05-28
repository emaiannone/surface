package org.surface.surface.core.analysis.setup;

import org.apache.commons.io.FileUtils;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public abstract class SetupEnvironmentAction {
    private static final String SURFACE_TMP = "SURFACE_TMP";

    private final String projectName;
    private final String target;
    private final Path workDirPath;

    SetupEnvironmentAction(String projectName, String target, Path workDirPath) {
        this.projectName = projectName;
        this.target = target;
        this.workDirPath = workDirPath;
    }

    public String getTarget() {
        return target;
    }

    Path getTmpDirPath() {
        return Paths.get(workDirPath.toString(), SURFACE_TMP);
    }

    Path getRepoDirPath() {
        return Paths.get(getTmpDirPath().toString(), projectName);
    }

    void deleteTmpDirectory(Path tmpDirPath) throws IOException {
        if (tmpDirPath.toFile().exists()) {
            FileUtils.deleteDirectory(tmpDirPath.toFile());
        }
    }

    public abstract Path setupEnvironment();
}
