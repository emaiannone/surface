package org.surface.surface.data.exports.writer;

public class WriterFactory {
    public ResultsWriter getWriter(String writeFormat) {
        switch (writeFormat.toLowerCase()) {
            case JSONWriter.CODE:
                return new JSONWriter();
            default:
                return new NullWriter();
        }
    }
}
