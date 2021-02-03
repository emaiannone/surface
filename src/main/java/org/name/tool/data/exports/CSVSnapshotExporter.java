package org.name.tool.data.exports;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.name.tool.data.bean.Snapshot;
import org.name.tool.results.ProjectMetricsResults;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class CSVSnapshotExporter implements SnapshotExporter {
    public static final String CODE = "csv";
    private static final String[] BASE_HEADERS = {
            "project",
            "commit_sha"
    };

    @Override
    public boolean export(Snapshot snapshot, ProjectMetricsResults projectMetricsResults) throws IOException {
        String projectName = snapshot.getProjectName();
        Path exportFilePath = Paths.get(projectName + "_results.csv");
        String[] headers = Stream.concat(Stream.concat(Arrays.stream(BASE_HEADERS),
                projectMetricsResults.getProjectMetricsCodes().stream()),
                projectMetricsResults.getClassMetricsCodes().stream())
                .toArray(String[]::new);

        CSVFormat csvFormat = CSVFormat.DEFAULT
                .withHeader(headers)
                .withSkipHeaderRecord(exportFilePath.toFile().exists());
        CSVPrinter csvPrinter = new CSVPrinter(Files.newBufferedWriter(
                exportFilePath,
                StandardOpenOption.APPEND,
                StandardOpenOption.CREATE),
                csvFormat);

        List<Object> record = new ArrayList<>();
        record.add(projectName);
        record.add(snapshot.getCommitSha());
        record.addAll(projectMetricsResults.getProjectMetricsValues());
        //TODO Class metrics

        csvPrinter.printRecord(record);
        csvPrinter.close(true);
        return true;
    }
}
