package org.surface.surface.core.runner;

import org.surface.surface.core.RunSetting;
import org.surface.surface.core.filter.RevisionFilter;
import org.surface.surface.core.filter.RevisionFilterFactory;

public class AnalysisRunnerFactory {
    public AnalysisRunner getAnalysisRunner(RunSetting runSetting) {
        RevisionFilterFactory revisionFilterFactory = new RevisionFilterFactory();
        RevisionFilter revisionFilter = revisionFilterFactory.getRevisionFilter(runSetting.getRevision());
        switch (runSetting.getRunMode()) {
            case LOCAL_DIR: {
                return new LocalDirectoryAnalysisRunner(runSetting.getMetrics(), runSetting.getTargetValue(), runSetting.getOutFilePath(), runSetting.getFilesRegex());
            }
            case LOCAL_GIT: {
                return new LocalGitAnalysisRunner(runSetting.getMetrics(), runSetting.getTargetValue(), runSetting.getOutFilePath(), runSetting.getFilesRegex(), revisionFilter);
            }
            case REMOTE_GIT: {
                return new RemoteGitAnalysisRunner(runSetting.getMetrics(), runSetting.getTargetValue(), runSetting.getOutFilePath(), runSetting.getFilesRegex(), runSetting.getCloneDirPath(), revisionFilter);
            }
            case FLEXIBLE: {
                return new FlexibleAnalysisRunner(runSetting.getMetrics(), runSetting.getTargetValue(), runSetting.getOutFilePath(), runSetting.getFilesRegex(), runSetting.getCloneDirPath(), revisionFilter);
            }
            default:
                throw new IllegalArgumentException("The run mode + " + runSetting.getRunMode() + " is invalid: the analysis cannot be run.");
        }
    }
}
