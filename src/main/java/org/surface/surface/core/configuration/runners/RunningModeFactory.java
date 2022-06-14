package org.surface.surface.core.configuration.runners;

import org.surface.surface.core.Utils;
import org.surface.surface.core.engine.analysis.selectors.RevisionSelector;
import org.surface.surface.core.engine.metrics.api.MetricsManager;
import org.surface.surface.core.engine.writers.FileWriter;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class RunningModeFactory {
    public static RunningMode<?> newRunningMode(String target, MetricsManager metricsManager, FileWriter writer,
                                                String filesRegex, boolean includeTests,
                                                RevisionSelector revisionSelector, Path workDirPath) {
        // NOTE This method must be updated when new run modes are implemented
        if (target == null) {
            throw new IllegalArgumentException("No target selected. There must be the target of the analyses.");
        }
        if (metricsManager == null || metricsManager.getMetricsCodes().size() == 0) {
            throw new IllegalArgumentException("No metrics selected. There must be at least one metric to compute.");
        }
        if (writer == null) {
            throw new IllegalArgumentException("No writer selected. There must be a writer to export the results.");
        }

        Path path = Paths.get(target).toAbsolutePath();
        if (Utils.isPathToLocalDirectory(path)) {
            return new LocalDirectoryRunningMode(path, metricsManager, writer, filesRegex, includeTests);
        }

        if (revisionSelector == null) {
            throw new IllegalArgumentException("No revision selector selected. There must be a revision selector.");
        }
        if (workDirPath == null) {
            throw new IllegalArgumentException("No working directory path selected. There must be a directory where to setup the working environment.");
        }

        if (Utils.isPathToGitDirectory(path)) {
            return new LocalGitRunningMode(path, metricsManager, writer, filesRegex, includeTests, revisionSelector, workDirPath);
        }
        if (Utils.isGitHubUrl(target)) {
            try {
                return new RemoteGitRunningMode(new URI(target), metricsManager, writer, filesRegex, includeTests, revisionSelector, workDirPath);
            } catch (URISyntaxException e) {
                throw new IllegalArgumentException("The target string is a malformed URL.");
            }
        }
        if (Utils.isPathToYamlFile(path)) {
            return new FlexibleRunningMode(path, metricsManager, writer, filesRegex, includeTests, revisionSelector, workDirPath);
        }
        throw new IllegalArgumentException("The supplied target is not attributable to any supported run mode.");
    }
}
