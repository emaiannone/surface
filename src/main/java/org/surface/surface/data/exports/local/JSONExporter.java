package org.surface.surface.data.exports.local;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.surface.surface.results.ClassMetricsResults;
import org.surface.surface.results.ProjectMetricsResults;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class JSONExporter implements ResultsExporter {
    public static final String CODE = "json";

    @Override
    public boolean export(ProjectMetricsResults projectMetricsResults, String outFile) throws IOException {
        Map<String, Object> output = new LinkedHashMap<>();
        output.put("path", projectMetricsResults.getProjectRoot().toString());
        output.put("metrics", projectMetricsResults.getProjectMetrics());
        List<Map<?, ?>> classes = new ArrayList<>();
        for (ClassMetricsResults classMetricsResult : projectMetricsResults.getClassMetricsResults()) {
            Map<String, Object> clazz = new LinkedHashMap<>();
            clazz.put("name", classMetricsResult.getClassName());
            clazz.put("path", classMetricsResult.getFilepath().toString());
            clazz.put("metrics", classMetricsResult.getClassMetrics());
            classes.add(clazz);
        }
        output.put("classes", classes);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter fw = new FileWriter(Paths.get(outFile).toFile())) {
            gson.toJson(output, fw);
        }
        return true;
    }
}
