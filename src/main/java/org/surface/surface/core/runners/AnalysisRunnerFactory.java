package org.surface.surface.core.runners;

import org.surface.surface.common.RunSetting;
import org.surface.surface.common.selectors.RevisionSelector;
import org.surface.surface.common.selectors.RevisionSelectorFactory;

public class AnalysisRunnerFactory {

    public AnalysisRunner<?> getAnalysisRunner(RunSetting runSetting) {
        switch (runSetting.getRunMode()) {
            case LOCAL_DIR: {
                return new LocalDirectoryAnalysisRunner(runSetting.getMetrics(), runSetting.getTargetValue(), runSetting.getOutFilePath(), runSetting.getFilesRegex());
            }
            case LOCAL_GIT: {
                RevisionSelector revisionSelector = new RevisionSelectorFactory().selectRevisionSelector(runSetting.getRevision());
                return new LocalGitAnalysisRunner(runSetting.getMetrics(), runSetting.getTargetValue(), runSetting.getOutFilePath(), runSetting.getFilesRegex(), runSetting.getWorkDirPath(), revisionSelector);
            }
            case REMOTE_GIT: {
                RevisionSelector revisionSelector = new RevisionSelectorFactory().selectRevisionSelector(runSetting.getRevision());
                return new RemoteGitAnalysisRunner(runSetting.getMetrics(), runSetting.getTargetValue(), runSetting.getOutFilePath(), runSetting.getFilesRegex(), runSetting.getWorkDirPath(), revisionSelector);
            }
            case FLEXIBLE: {
                RevisionSelector revisionSelector = new RevisionSelectorFactory().selectRevisionSelector(runSetting.getRevision());
                return new FlexibleAnalysisRunner(runSetting.getMetrics(), runSetting.getTargetValue(), runSetting.getOutFilePath(), runSetting.getFilesRegex(), runSetting.getWorkDirPath(), revisionSelector);
            }
            default:
                throw new IllegalArgumentException("The run mode + " + runSetting.getRunMode() + " is invalid: the analysis cannot be run.");
        }
    }
}
