package org.surface.surface.out.writers;

import java.io.IOException;
import java.util.Map;

public interface Writer {
    void write(Map<String, Object> content) throws IOException;

}
