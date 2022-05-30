package org.surface.surface.core.out.writers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;


public class JsonFileWriter extends FileWriter {
    public static final String CODE = "json";

    public JsonFileWriter(Path outFilePath) {
        super(outFilePath);
    }

    @Override
    public void write(Map<String, Object> content) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (java.io.FileWriter fw = new java.io.FileWriter(getOutFilePath().toFile())) {
            gson.toJson(content, fw);
        }
    }
}
