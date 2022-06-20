package org.surface.surface.core.engine.writers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;


public class JsonFileWriter extends FileWriter {
    public static final String CODE = "json";

    public JsonFileWriter(File outFile) {
        super(outFile);
    }

    @Override
    public void write(List<Map<String, Object>> content) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (java.io.FileWriter fw = new java.io.FileWriter(getOutFile())) {
            gson.toJson(content, fw);
        }
    }
}
