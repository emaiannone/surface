package org.name.tool.core.metrics.projectlevel.sccr;

import org.name.tool.core.metrics.api.ProjectMetric;

public abstract class SCCR extends ProjectMetric<Double> {
    public static final String NAME = "Serializable Critical Classes Ratio";
    public static final String CODE = "SCCR";

    protected SCCR() {
        setName(NAME);
        setCode(CODE);
    }
}
