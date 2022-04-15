package org.surface.surface.data.exports.local;

import org.surface.surface.data.exports.remote.SnapshotExporter;

public class ExporterFactory {
    public ResultsExporter getExporter(String exportFormat) {
        switch (exportFormat) {
            case JSONExporter.CODE:
                return new JSONExporter();
            default:
                return new NullExporter();
        }
    }
}
