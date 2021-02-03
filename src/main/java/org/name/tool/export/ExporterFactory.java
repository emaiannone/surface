package org.name.tool.export;

public class ExporterFactory {

    public Exporter getExporter(String exportFormat) {
        switch (exportFormat) {
            case CSVExporter.CODE:
                return new CSVExporter();
            default:
                return new NullExporter();
        }
    }
}
