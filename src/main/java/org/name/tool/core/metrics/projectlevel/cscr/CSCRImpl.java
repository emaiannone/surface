package org.name.tool.core.metrics.projectlevel.cscr;

import com.github.javaparser.resolution.types.ResolvedReferenceType;
import org.name.tool.core.results.ClassifiedAnalyzerResults;
import org.name.tool.core.results.MetricResult;
import org.name.tool.core.results.ProjectAnalyzerResults;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CSCRImpl extends CSCR {

    @Override
    public MetricResult<Map<String, Double>> compute(ProjectAnalyzerResults projectResults) {
        Map<String, Double> value = new HashMap<>();
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
            value.put(classResults.getFullyQualifiedName(), metricValue);
        }
        return new MetricResult<>(getName(), getCode(), value);
    }
}
