package org.surface.surface.core.engine.metrics.project.ccc;

import org.surface.surface.core.engine.inspection.results.ProjectInspectorResults;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;

public class CCCImpl extends CCC {

    @Override
    public DoubleMetricValue compute(ProjectInspectorResults projectResult) {
        int classesAccessing = projectResult.getNumberClassesAccessingClassifiedAttributes();
        int possibleAccesses = (projectResult.getNumberClasses() - 1) * projectResult.getNumberAllClassifiedAttributes();
        double value = possibleAccesses != 0.0 ? (double) classesAccessing / possibleAccesses : 0.0;
        return new DoubleMetricValue(getName(), getCode(), value);
    }
}
