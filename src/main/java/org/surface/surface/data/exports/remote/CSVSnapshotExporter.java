package org.surface.surface.data.exports.remote;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.surface.surface.core.metrics.results.ProjectMetricsResults;
import org.surface.surface.core.metrics.results.values.MetricValue;
import org.surface.surface.core.metrics.results.values.NumericMetricValue;
import org.surface.surface.data.bean.Snapshot;

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
    public boolean export(Snapshot snapshot, ProjectMetricsResults projectMetricsResults, String[] metricsCodes, String outFile) throws IOException {
        String projectId = snapshot.getProjectId();
        Path destination = Paths.get(outFile);
        Path exportFilePath = Paths.get(destination.getParent().toString(), projectId + destination.getFileName().toString());

        List<Object> record = new ArrayList<>();
        record.add(projectId);
        record.add(snapshot.getCommitHash());

        Map<String, Object> projectMetrics = projectMetricsResults.getProjectMetrics();
        Map<String, List<MetricValue<?>>> groupedValues = projectMetricsResults.classMetricsGroupedByCode();
        for (String metricsCode : metricsCodes) {
            Object projectMetricValue = projectMetrics.get(metricsCode);
            // Check if it is a project metric
            if (projectMetricValue != null) {
                record.add(projectMetricValue);
            } else {
                // Check if it is a class metric (to be aggregated as average)
                List<MetricValue<?>> values = groupedValues.get(metricsCode);
                if (values != null) {
                    // Consider only numeric metric values
                    if (values.size() > 0 && values.get(0) instanceof NumericMetricValue<?>) {
                        double meanResult = values.stream()
                                .map(mv -> (NumericMetricValue<?>) mv)
                                .mapToDouble(mv -> mv.getValue().doubleValue())
                                .average().orElse(0.0);
                        record.add(meanResult);
                    }
                } else {
                    // Metrics not found in the results: set to 0.0
                    record.add(0.0);
                }
            }
        }

        String[] headers = Stream.concat(Arrays.stream(BASE_HEADERS), Arrays.stream(metricsCodes)).toArray(String[]::new);
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
