package org.surface.surface.common;

import org.apache.commons.lang3.tuple.Pair;

import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

public class RunSetting {
    private final Pair<RunMode, String> target;
    private final List<String> metrics;
    private final Pair<String, String> outFile;
    private final String filesRegex;
    private final Path workDirPath;
    private final Pair<RevisionMode, String> revision;

    public RunSetting(Pair<RunMode, String> target, List<String> metrics, Pair<String, String> outFile, String filesRegex, Path workDirPath, Pair<RevisionMode, String> revision) {
        this.target = Objects.requireNonNull(target);
        this.metrics = Objects.requireNonNull(metrics);
        this.outFile = Objects.requireNonNull(outFile);
        this.filesRegex = filesRegex;
        this.workDirPath = workDirPath;
        this.revision = revision;
    }

    public Pair<RunMode, String> getTarget() {
        return target;
    }

    public List<String> getMetrics() {
        return metrics;
    }

    public RunMode getRunMode() {
        return target.getKey();
    }

    public String getTargetValue() {
        return target.getValue();
    }

    public Pair<String, String> getOutFile() {
        return outFile;
    }

    public String getFilesRegex() {
        return filesRegex;
    }

    public Path getWorkDirPath() {
        return workDirPath;
    }

    public Pair<RevisionMode, String> getRevision() {
        return revision;
    }

    public RevisionMode getRevisionMode() {
        return revision.getKey();
    }

    public String getRevisionString() {
        return revision.getValue();
    }
}
