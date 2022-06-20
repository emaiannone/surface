package org.surface.surface.core.engine.inspection;

import org.surface.surface.core.engine.inspection.results.InspectorResults;

import java.io.IOException;

abstract class Inspector {
    abstract InspectorResults inspect() throws IOException;
}
