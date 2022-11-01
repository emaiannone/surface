package org.surface.surface.core.exporters;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.surface.surface.core.configuration.runners.results.RunResults;
import org.surface.surface.core.engine.analysis.results.FormattableAnalysisResults;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JsonRunResultsExporter extends RunResultsExporter {
    public static final String CODE = "json";
    private static final Gson gsonBuilder = new GsonBuilder().setPrettyPrinting().create();

    public JsonRunResultsExporter(File outFile) {
        super(outFile);
    }

    @Override
    public String export(RunResults results) throws IOException {
        String exportString;
        if (results.getNumberResults() == 0) {
            exportString = "";
        } else if (results.getNumberResults() == 1) {
            exportString = gsonBuilder.toJson(results.getFirstResult().asMap());
        } else {
            List<Map<String, Object>> exportList = new ArrayList<>();
            for (Map.Entry<String, FormattableAnalysisResults> analysisResultsEntry : results.getAllResults().entrySet()) {
                FormattableAnalysisResults analysisResults = analysisResultsEntry.getValue();
                Map<String, Object> projectExport = analysisResults.asMap();
                exportList.add(projectExport);
            }
            exportString = gsonBuilder.toJson(exportList);
        }
        try (FileWriter fw = new FileWriter(getOutFile())) {
            fw.write(exportString);
        }
        return exportString;
    }
}
