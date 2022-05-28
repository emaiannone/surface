package org.surface.surface.core.runners;

import org.surface.surface.common.RunSetting;
import org.surface.surface.core.analysis.selectors.RevisionSelector;
import org.surface.surface.core.analysis.selectors.RevisionSelectorFactory;

public class ModeRunnerFactory {

    public ModeRunner<?> getModeRunner(RunSetting runSetting) {
        switch (runSetting.getRunMode()) {
            case LOCAL_DIR: {
                return new LocalDirectoryModeRunner(runSetting.getMetrics(), runSetting.getTargetValue(), runSetting.getOutFilePath(), runSetting.getFilesRegex());
            }
            case LOCAL_GIT: {
                return new LocalGitModeRunner(runSetting.getMetrics(), runSetting.getTargetValue(), runSetting.getOutFilePath(), runSetting.getFilesRegex(), runSetting.getRevision(), runSetting.getWorkDirPath());
            }
            case REMOTE_GIT: {
                return new RemoteGitModeRunner(runSetting.getMetrics(), runSetting.getTargetValue(), runSetting.getOutFilePath(), runSetting.getFilesRegex(), runSetting.getRevision(), runSetting.getWorkDirPath());
            }
            case FLEXIBLE: {
                RevisionSelector revisionSelector = new RevisionSelectorFactory().selectRevisionSelector(runSetting.getRevision());
                return new FlexibleModeRunner(runSetting.getMetrics(), runSetting.getTargetValue(), runSetting.getOutFilePath(), runSetting.getFilesRegex(), runSetting.getWorkDirPath(), revisionSelector);
            }
            default:
                throw new IllegalArgumentException("The run mode + " + runSetting.getRunMode() + " is invalid: the analysis cannot be run.");
        }
    }
}
