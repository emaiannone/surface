package org.surface.surface.core.runners;

import org.surface.surface.common.RunSetting;
import org.surface.surface.common.selectors.RevisionSelector;
import org.surface.surface.common.selectors.RevisionSelectorFactory;

public class AnalysisModeFactory {

    public ModeRunner<?> getModeRunner(RunSetting runSetting) {
        switch (runSetting.getRunMode()) {
            case LOCAL_DIR: {
                return new LocalDirectoryModeRunner(runSetting.getMetrics(), runSetting.getTargetValue(), runSetting.getOutFilePath(), runSetting.getFilesRegex());
            }
            case LOCAL_GIT: {
                RevisionSelector revisionSelector = new RevisionSelectorFactory().selectRevisionSelector(runSetting.getRevision());
                return new LocalGitModeRunner(runSetting.getMetrics(), runSetting.getTargetValue(), runSetting.getOutFilePath(), runSetting.getFilesRegex(), runSetting.getWorkDirPath(), revisionSelector);
            }
            case REMOTE_GIT: {
                RevisionSelector revisionSelector = new RevisionSelectorFactory().selectRevisionSelector(runSetting.getRevision());
                return new RemoteGitModeRunner(runSetting.getMetrics(), runSetting.getTargetValue(), runSetting.getOutFilePath(), runSetting.getFilesRegex(), runSetting.getWorkDirPath(), revisionSelector);
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
