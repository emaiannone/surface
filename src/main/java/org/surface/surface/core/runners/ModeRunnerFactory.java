package org.surface.surface.core.runners;

import org.surface.surface.common.RunSetting;

public class ModeRunnerFactory {

    static ModeRunner<?> newModeRunner(RunSetting runSetting) {
        // NOTE Any new run mode must be added here to be supported
        switch (runSetting.getRunMode()) {
            case LOCAL_DIR: {
                return new LocalDirectoryModeRunner(runSetting.getMetrics(), runSetting.getTargetValue(), runSetting.getOutFile(), runSetting.getFilesRegex());
            }
            case LOCAL_GIT: {
                return new LocalGitModeRunner(runSetting.getMetrics(), runSetting.getTargetValue(), runSetting.getOutFile(), runSetting.getFilesRegex(), runSetting.getRevision(), runSetting.getWorkDirPath());
            }
            case REMOTE_GIT: {
                return new RemoteGitModeRunner(runSetting.getMetrics(), runSetting.getTargetValue(), runSetting.getOutFile(), runSetting.getFilesRegex(), runSetting.getRevision(), runSetting.getWorkDirPath());
            }
            case FLEXIBLE: {
                return new FlexibleModeRunner(runSetting.getMetrics(), runSetting.getTargetValue(), runSetting.getOutFile(), runSetting.getFilesRegex(), runSetting.getRevision(), runSetting.getWorkDirPath());
            }
            default:
                throw new IllegalArgumentException("The run mode + " + runSetting.getRunMode() + " is invalid: the analysis cannot be run.");
        }
    }
}
