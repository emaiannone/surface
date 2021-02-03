package org.name.tool.data.imports;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.name.tool.data.bean.Snapshot;

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
            String repositoryURI = csvRecord.get("github");
            String commitSha = csvRecord.get("commit_sha");
            snapshots.add(new Snapshot(repositoryURI, commitSha));
        }
        csvParser.close();
        return snapshots;
    }

}
