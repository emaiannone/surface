package org.name.tool.data.exports;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.name.tool.core.results.ProjectMetricsResults;
import org.name.tool.data.bean.Snapshot;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class CSVSnapshotExporter implements SnapshotExporter {
    public static final String CODE = "csv";
    private static final String[] headers = {
            "github",
            "commit_sha"
    };

    @Override
    public boolean export(Snapshot snapshot, ProjectMetricsResults projectMetricsResults) throws IOException {
        String projectName = projectMetricsResults.getProjectRoot().getRoot().getFileName().toString();
        Path exportFilePath = Paths.get(projectName + "_results.csv");
        CSVFormat csvFormat = CSVFormat.DEFAULT
                .withHeader(headers)
                .withSkipHeaderRecord(exportFilePath.toFile().exists());
        CSVPrinter csvPrinter = new CSVPrinter(Files.newBufferedWriter(
                exportFilePath,
                StandardOpenOption.APPEND,
                StandardOpenOption.CREATE),
                csvFormat);
        //TODO Complete Implementation
        csvPrinter.printRecord(snapshot.getRepositoryURI(), snapshot.getCommitSha());
        csvPrinter.close(true);
        return true;
    }
}
