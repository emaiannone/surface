package org.surface.surface.core.metrics.projectlevel.cscr;

import com.github.javaparser.resolution.types.ResolvedReferenceType;
import org.surface.surface.core.inspection.results.ClassInspectorResults;
import org.surface.surface.core.inspection.results.ProjectInspectorResults;
import org.surface.surface.core.metrics.results.values.DoubleMetricValue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CSCRImpl extends CSCR {

    @Override
    public DoubleMetricValue compute(ProjectInspectorResults projectResults) {
        Map<String, Double> values = new HashMap<>();
        for (ClassInspectorResults classResults : projectResults) {
            List<ResolvedReferenceType> superclasses = classResults.getSuperclasses();
            int totalSuperClasses = superclasses.size();
            // FQN name match seems weak... fine for now
            int criticalSuperClasses = 0;
            for (ResolvedReferenceType superclass : superclasses) {
                ClassInspectorResults superClassResults = projectResults.getClassResults(superclass.getQualifiedName());
                if (superClassResults != null && superClassResults.isCritical()) {
                    criticalSuperClasses++;
                }
            }
            double metricValue = totalSuperClasses != 0.0 ? (double) criticalSuperClasses / totalSuperClasses : 0.0;
            values.put(classResults.getFullyQualifiedClassName(), metricValue);
        }
        double value = values.values().stream().mapToDouble(x -> x).average().orElse(0.0);
        return new DoubleMetricValue(getName(), getCode(), value);
    }
}
