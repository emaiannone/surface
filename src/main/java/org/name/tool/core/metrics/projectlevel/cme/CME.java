package org.name.tool.core.metrics.projectlevel.cme;

import org.name.tool.core.metrics.api.ProjectMetric;
import org.name.tool.results.values.DoubleMetricValue;

public abstract class CME extends ProjectMetric<DoubleMetricValue> {
    public static final String NAME = "Critical Methods Extensibility";
    public static final String CODE = "CME";

    protected CME() {
        setName(NAME);
        setCode(CODE);
    }
}
