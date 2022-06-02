package org.surface.surface.core.runners;

import org.surface.surface.core.Utils;
import org.surface.surface.core.analysis.selectors.RevisionSelector;
import org.surface.surface.core.metrics.api.MetricsManager;
import org.surface.surface.core.out.writers.FileWriter;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ModeRunnerFactory {
    public static ModeRunner<?> newModeRunner(String target, MetricsManager metricsManager, FileWriter writer,
                                              String filesRegex, RevisionSelector revisionSelector,
                                              Path workDirPath) {
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
            return new LocalDirectoryModeRunner(path, metricsManager, writer, filesRegex);
        }

        if (revisionSelector == null) {
            throw new IllegalArgumentException("No revision selector selected. There must be a revision selector.");
        }
        if (workDirPath == null) {
            throw new IllegalArgumentException("No working directory path selected. There must be a directory where to setup the working environment.");
        }

        if (Utils.isPathToGitDirectory(path)) {
            return new LocalGitModeRunner(path, metricsManager, writer, filesRegex, revisionSelector, workDirPath);
        }
        if (Utils.isGitHubUrl(target)) {
            try {
                return new RemoteGitModeRunner(new URI(target), metricsManager, writer, filesRegex, revisionSelector, workDirPath);
            } catch (URISyntaxException e) {
                throw new IllegalArgumentException("The target string is a malformed URL.");
            }
        }
        if (Utils.isPathToYamlFile(path)) {
            return new FlexibleModeRunner(path, metricsManager, writer, filesRegex, revisionSelector, workDirPath);
        }
        throw new IllegalArgumentException("The supplied target is not attributable to any supported run mode.");
    }
}
