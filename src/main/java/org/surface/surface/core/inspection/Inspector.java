package org.surface.surface.core.inspection;

import org.surface.surface.core.inspection.results.InspectorResults;

import java.io.IOException;

abstract class Inspector {
    abstract InspectorResults inspect() throws IOException;
}
