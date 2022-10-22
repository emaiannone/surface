package org.surface.surface.core.engine.analysis.results;

import java.util.Map;

public interface FormattableAnalysisResults {
    Map<String, Object> asMap();
    String asPlain();
}
