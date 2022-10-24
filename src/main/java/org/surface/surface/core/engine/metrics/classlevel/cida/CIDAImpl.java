package org.surface.surface.core.engine.metrics.classlevel.cida;

import org.surface.surface.core.engine.inspection.results.ClassInspectorResults;
import org.surface.surface.core.engine.metrics.classlevel.cat.CAT;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;

public class CIDAImpl extends CIDA {
    private final CAT cat;

    public CIDAImpl(CAT cat) {
        this.cat = cat;
    }

    @Override
    public DoubleMetricValue compute(ClassInspectorResults classResults) {
        int nonPrivateNonStatic = classResults.getNumberNonPrivateInstanceClassifiedAttributes();
        int catValue = cat.compute(classResults).getValue();
        double value = catValue != 0.0 ? (double) nonPrivateNonStatic / catValue : 0.0;
        return new DoubleMetricValue(getName(), getCode(), value);
    }
}
