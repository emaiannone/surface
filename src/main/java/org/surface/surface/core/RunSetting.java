package org.surface.surface.core;

import org.apache.commons.lang3.tuple.Pair;

import java.io.File;
import java.util.List;
import java.util.Objects;

public class RunSetting {
    private final List<String> metrics;
    private final Pair<RunMode, String> target;
    private final File outFile;
    private final String filesRegex;
    private final File cloneDir;
    private final Pair<RevisionMode, String> revision;

    public RunSetting(List<String> metrics, Pair<RunMode, String> target, File outFile,  String filesRegex, File cloneDir, Pair<RevisionMode, String> revision) {
        this.metrics = Objects.requireNonNull(metrics);
        this.target = Objects.requireNonNull(target);
        this.outFile = Objects.requireNonNull(outFile);
        this.filesRegex = filesRegex;
        this.cloneDir = cloneDir;
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

    public File getOutFile() {
        return outFile;
    }

    public String getFilesRegex() {
        return filesRegex;
    }

    public File getCloneDir() {
        return cloneDir;
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
