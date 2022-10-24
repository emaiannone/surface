package org.surface.surface.core.engine.metrics.classlevel.ccda;

import org.surface.surface.core.engine.inspection.results.ClassInspectorResults;
import org.surface.surface.core.engine.metrics.classlevel.cat.CAT;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;

public class CCDAImpl extends CCDA {
    private final CAT cat;

    public CCDAImpl(CAT cat) {
        this.cat = cat;
    }

    @Override
    public DoubleMetricValue compute(ClassInspectorResults classResults) {
        int nonPrivateStatic = classResults.getNumberNonPrivateClassClassifiedAttributes();
        int catValue = cat.compute(classResults).getValue();
        double value = catValue != 0.0 ? (double) nonPrivateStatic / catValue : 0.0;
        return new DoubleMetricValue(getName(), getCode(), value);
    }
}
