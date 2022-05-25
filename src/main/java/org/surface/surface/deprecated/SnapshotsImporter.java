package org.surface.surface.deprecated;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public interface SnapshotsImporter {
    List<Snapshot> extractSnapshots(Path fileToRead) throws IOException;
}
