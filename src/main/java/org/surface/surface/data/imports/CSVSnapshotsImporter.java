package org.surface.surface.data.imports;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.surface.surface.data.bean.Snapshot;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class CSVSnapshotsImporter implements SnapshotsImporter {

    public List<Snapshot> extractSnapshots(Path fileToRead) throws IOException {
        List<Snapshot> snapshots = new ArrayList<>();
        CSVParser csvParser = new CSVParser(Files.newBufferedReader(fileToRead), CSVFormat.DEFAULT
                .withFirstRecordAsHeader()
                .withIgnoreHeaderCase()
                .withTrim());
        for (CSVRecord csvRecord : csvParser) {
            String projectId = csvRecord.get("projectID");
            String repositoryURI = csvRecord.get("github");
            String commitHash = csvRecord.get("commitHash");
            snapshots.add(new Snapshot(projectId, repositoryURI, commitHash));
        }
        csvParser.close();
        return snapshots;
    }

}
