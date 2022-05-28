package org.surface.surface.out.writers;

import java.util.Map;

public class NullWriter extends Writer {
    public static final String CODE = "";

    @Override
    public void write(Map<String, Object> content) {
        return;
    }
}
