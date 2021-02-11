package org.surface.surface.data.exports;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.surface.surface.data.bean.Snapshot;
import org.surface.surface.results.ProjectMetricsResults;
import org.surface.surface.results.values.MetricValue;
import org.surface.surface.results.values.NumericMetricValue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class CSVSnapshotExporter implements SnapshotExporter {
    public static final String CODE = "csv";
    private static final String[] BASE_HEADERS = {
            "projectID",
            "commitHash"
    };

    @Override
    public boolean export(Snapshot snapshot, ProjectMetricsResults projectMetricsResults) throws IOException {
        String projectId = snapshot.getProjectId();
        Path exportFilePath = Paths.get(projectId + "_results.csv");

        List<Object> record = new ArrayList<>();
        record.add(projectId);
        record.add(snapshot.getCommitHash());

        // Class Numeric Metrics (averages)
        List<String> classMetricsCode = new ArrayList<>();
        Map<String, List<MetricValue<?>>> groupedValues = projectMetricsResults.classMetricsGroupedByCode();
        for (Map.Entry<String, List<MetricValue<?>>> groupedValuesEntry : groupedValues.entrySet()) {
            List<MetricValue<?>> values = groupedValuesEntry.getValue();
            // Consider only numeric metric values
            if (values.size() > 0 && values.get(0) instanceof NumericMetricValue<?>) {
                classMetricsCode.add(groupedValuesEntry.getKey());
                double meanResult = values.stream()
                        .map(mv -> (NumericMetricValue<?>) mv)
                        .mapToDouble(mv -> mv.getValue().doubleValue())
                        .average().orElse(0.0);
                record.add(meanResult);
            }
        }

        // Project Metrics
        List<String> projectMetricsCode = new ArrayList<>();
        Map<String, Object> projectMetrics = projectMetricsResults.getProjectMetrics();
        for (Map.Entry<String, Object> projectMetricsEntry : projectMetrics.entrySet()) {
            projectMetricsCode.add(projectMetricsEntry.getKey());
            record.add(projectMetricsEntry.getValue());
        }

        String[] headers = Stream.concat(Stream.concat(Arrays.stream(BASE_HEADERS),
                classMetricsCode.stream()),
                projectMetricsCode.stream())
                .toArray(String[]::new);
        CSVFormat csvFormat = CSVFormat.DEFAULT
                .withHeader(headers)
                .withSkipHeaderRecord(exportFilePath.toFile().exists());
        CSVPrinter csvPrinter = new CSVPrinter(Files.newBufferedWriter(
                exportFilePath,
                StandardOpenOption.APPEND,
                StandardOpenOption.CREATE),
                csvFormat);

        csvPrinter.printRecord(record);
        csvPrinter.close(true);
        return true;
    }
}
