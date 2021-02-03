package org.name.tool.data.exports;

public class SnapshotExporterFactory {

    public SnapshotExporter getExporter(String exportFormat) {
        switch (exportFormat) {
            case CSVSnapshotExporter.CODE:
                return new CSVSnapshotExporter();
            default:
                return new NullSnapshotExporter();
        }
    }
}
