package org.surface.surface.core.engine.exporters;

import org.surface.surface.core.configuration.runners.results.RunResults;
import org.surface.surface.core.engine.analysis.results.FormattableAnalysisResults;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PlainRunResultsExporter extends RunResultsExporter {
    public static final String CODE = "txt";

    public PlainRunResultsExporter(File outFile) {
        super(outFile);
    }

    @Override
    public String export(RunResults results) throws IOException {
        String exportString;
        if (results.getNumberResults() == 0) {
            exportString = "";
        } else if (results.getNumberResults() == 1) {
            exportString = results.getFirstResult().asPlain();
        } else {
            List<String> exportList = new ArrayList<>();
            for (Map.Entry<String, FormattableAnalysisResults> analysisResultsEntry : results.getAllResults().entrySet()) {
                FormattableAnalysisResults analysisResults = analysisResultsEntry.getValue();
                String projectExport = analysisResults.asPlain();
                exportList.add(projectExport.trim());
            }
            exportString = String.join("\n\n////////////////////////////////////////////\n\n", exportList);
        }
        try (FileWriter fw = new FileWriter(getOutFile())) {
            fw.write(exportString);
        }
        return exportString;
    }
}
