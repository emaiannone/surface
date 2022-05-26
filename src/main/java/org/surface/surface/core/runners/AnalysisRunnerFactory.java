package org.surface.surface.core.runners;

import org.surface.surface.common.RunSetting;

public class AnalysisRunnerFactory {

    public AnalysisRunner<?> getAnalysisRunner(RunSetting runSetting) {
        switch (runSetting.getRunMode()) {
            case LOCAL_DIR: {
                return new LocalDirectoryAnalysisRunner(runSetting.getMetrics(), runSetting.getTargetValue(), runSetting.getOutFilePath(), runSetting.getFilesRegex());
            }
            case LOCAL_GIT: {
                return new LocalGitAnalysisRunner(runSetting.getMetrics(), runSetting.getTargetValue(), runSetting.getOutFilePath(), runSetting.getFilesRegex(), runSetting.getRevision());
            }
            case REMOTE_GIT: {
                return new RemoteGitAnalysisRunner(runSetting.getMetrics(), runSetting.getTargetValue(), runSetting.getOutFilePath(), runSetting.getFilesRegex(), runSetting.getCloneDirPath(), runSetting.getRevision());
            }
            case FLEXIBLE: {
                return new FlexibleAnalysisRunner(runSetting.getMetrics(), runSetting.getTargetValue(), runSetting.getOutFilePath(), runSetting.getFilesRegex(), runSetting.getCloneDirPath(), runSetting.getRevision());
            }
            default:
                throw new IllegalArgumentException("The run mode + " + runSetting.getRunMode() + " is invalid: the analysis cannot be run.");
        }
    }
}
