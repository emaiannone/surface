package org.surface.surface.common;

import org.apache.commons.lang3.tuple.Pair;

import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

public class RunSetting {
    private final List<String> metrics;
    private final Pair<RunMode, String> target;
    private final Path outFilePath;
    private final String filesRegex;
    private final Path cloneDirPath;
    private final Pair<RevisionMode, String> revision;

    public RunSetting(List<String> metrics, Pair<RunMode, String> target, Path outFilePath, String filesRegex, Path cloneDirPath, Pair<RevisionMode, String> revision) {
        this.metrics = Objects.requireNonNull(metrics);
        this.target = Objects.requireNonNull(target);
        this.outFilePath = Objects.requireNonNull(outFilePath);
        this.filesRegex = filesRegex;
        this.cloneDirPath = cloneDirPath;
        this.revision = revision;
    }

    public List<String> getMetrics() {
        return metrics;
    }

    public Pair<RunMode, String> getTarget() {
        return target;
    }

    public RunMode getRunMode() {
        return target.getKey();
    }

    public String getTargetValue() {
        return target.getValue();
    }

    public Path getOutFilePath() {
        return outFilePath;
    }

    public String getFilesRegex() {
        return filesRegex;
    }

    public Path getCloneDirPath() {
        return cloneDirPath;
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
