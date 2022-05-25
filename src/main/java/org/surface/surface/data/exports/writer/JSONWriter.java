package org.surface.surface.data.exports.writer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.surface.surface.core.metrics.results.ClassMetricsResults;
import org.surface.surface.core.metrics.results.ProjectMetricsResults;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class JSONWriter implements ResultsWriter {
    public static final String CODE = "json";

    @Override
    public boolean export(ProjectMetricsResults projectMetricsResults, Path outFilePath) throws IOException {
        Map<String, Object> output = new LinkedHashMap<>();
        output.put("projectPath", projectMetricsResults.getProjectRoot().toString());
        output.put("metrics", projectMetricsResults.getProjectMetrics());
        List<Map<?, ?>> classes = new ArrayList<>();
        for (ClassMetricsResults classMetricsResult : projectMetricsResults.getClassMetricsResults()) {
            Map<String, Object> clazz = new LinkedHashMap<>();
            clazz.put("className", classMetricsResult.getFullyQualifiedClassName());
            clazz.put("filePath", classMetricsResult.getFilepath().toString());
            clazz.put("metrics", classMetricsResult.getClassMetrics());
            classes.add(clazz);
        }
        output.put("classes", classes);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter fw = new FileWriter(outFilePath.toFile())) {
            gson.toJson(output, fw);
        }
        return true;
    }
}
