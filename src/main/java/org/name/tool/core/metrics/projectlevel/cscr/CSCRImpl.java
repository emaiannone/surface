package org.name.tool.core.metrics.projectlevel.cscr;

import com.github.javaparser.resolution.types.ResolvedReferenceType;
import org.name.tool.results.ClassifiedAnalyzerResults;
import org.name.tool.results.ProjectAnalyzerResults;
import org.name.tool.results.values.DoubleMetricValue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CSCRImpl extends CSCR {

    @Override
    public DoubleMetricValue compute(ProjectAnalyzerResults projectResults) {
        Map<String, Double> values = new HashMap<>();
        for (ClassifiedAnalyzerResults classResults : projectResults) {
            List<ResolvedReferenceType> superclasses = classResults.getSuperclasses();
            int totalSuperClasses = superclasses.size();
            // FQN name match seems weak... fine for now
            int criticalSuperClasses = 0;
            for (ResolvedReferenceType superclass : superclasses) {
                ClassifiedAnalyzerResults superClassResults = projectResults.getClassResults(superclass.getQualifiedName());
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
