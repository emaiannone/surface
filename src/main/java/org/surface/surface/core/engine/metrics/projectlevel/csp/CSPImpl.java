package org.surface.surface.core.engine.metrics.projectlevel.csp;

import com.github.javaparser.resolution.types.ResolvedReferenceType;
import org.surface.surface.core.engine.inspection.results.ClassInspectorResults;
import org.surface.surface.core.engine.inspection.results.ProjectInspectorResults;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CSPImpl extends CSP {

    @Override
    public DoubleMetricValue compute(ProjectInspectorResults projectResults) {
        Map<String, Double> values = new LinkedHashMap<>();
        for (ClassInspectorResults classResults : projectResults.getClassResults()) {
            List<ResolvedReferenceType> superclasses = classResults.getAllSuperclasses();
            int totalSuperClasses = superclasses.size();
            // FQN name match seems weak... fine for now
            int criticalSuperClasses = 0;
            for (ResolvedReferenceType superclass : superclasses) {
                ClassInspectorResults superClassResults = projectResults.getClassResult(superclass.getQualifiedName());
                if (superClassResults != null && superClassResults.isCritical()) {
                    criticalSuperClasses++;
                }
            }
            double metricValue = totalSuperClasses != 0.0 ? (double) criticalSuperClasses / totalSuperClasses : 0.0;
            values.put(classResults.getClassFullyQualifiedName(), metricValue);
        }
        double value = values.values().stream().mapToDouble(x -> x).average().orElse(0.0);
        return new DoubleMetricValue(getName(), getCode(), value);
    }
}
