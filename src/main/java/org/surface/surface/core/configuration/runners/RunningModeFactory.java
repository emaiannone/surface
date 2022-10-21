package org.surface.surface.core.configuration.runners;

import org.apache.commons.validator.routines.UrlValidator;
import org.surface.surface.core.Utils;
import org.surface.surface.core.engine.analysis.selectors.RevisionSelector;
import org.surface.surface.core.engine.metrics.api.MetricsManager;
import org.surface.surface.core.engine.writers.FileWriter;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class RunningModeFactory {
    // NOTE This method must be updated when new run modes are implemented
    public static RunningMode<?> newRunningMode(String target, Path workDirPath, FileWriter writer,
                                                MetricsManager metricsManager, String filesRegex,
                                                RevisionSelector revisionSelector, boolean includeTests, boolean excludeWorkTree) {
        if (target == null) {
            throw new IllegalArgumentException("No target specified. There must be a target of the analyses.");
        }
        Path path = Paths.get(target).toAbsolutePath();
        if (Utils.isPathToLocalDirectory(path)) {
            return new LocalDirectoryRunningMode(path, workDirPath, writer, metricsManager, filesRegex, includeTests);
        }
        if (Utils.isPathToGitDirectory(path)) {
            return new LocalGitRunningMode(path, workDirPath, writer, metricsManager, revisionSelector, filesRegex, excludeWorkTree, includeTests);
        }
        if (UrlValidator.getInstance().isValid(target)) {
            try {
                return new RemoteGitRunningMode(new URI(target), workDirPath, writer, metricsManager, revisionSelector, filesRegex, includeTests);
            }
            catch (URISyntaxException e) {
                throw new IllegalArgumentException("The target string is a malformed URL.");
            }
        }
        if (Utils.isPathToYamlFile(path)) {
            return new FlexibleRunningMode(path, workDirPath, writer, metricsManager, revisionSelector, filesRegex, includeTests, excludeWorkTree);
        }
        throw new IllegalArgumentException("The supplied target is not attributable to any supported run mode.");
    }
}
