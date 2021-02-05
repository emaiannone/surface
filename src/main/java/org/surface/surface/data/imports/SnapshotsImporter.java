package org.surface.surface.data.imports;

import org.surface.surface.data.bean.Snapshot;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public interface SnapshotsImporter {
    List<Snapshot> extractSnapshots(Path fileToRead) throws IOException;
}
