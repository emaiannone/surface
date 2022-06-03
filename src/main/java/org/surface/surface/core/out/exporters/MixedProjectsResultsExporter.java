package org.surface.surface.core.out.exporters;

import org.surface.surface.core.metrics.results.ProjectMetricsResults;

import java.util.LinkedHashMap;
import java.util.Map;

public class MixedProjectsResultsExporter extends ResultsExporter<Map<String, Map<String, ProjectMetricsResults>>> {

    @Override
    public Map<String, Object> export(Map<String, Map<String, ProjectMetricsResults>> fullResults) {
        Map<String, Object> content = new LinkedHashMap<>();
        for (Map.Entry<String, Map<String, ProjectMetricsResults>> fullResultsEntry : fullResults.entrySet()) {
            String projectId = fullResultsEntry.getKey();
            Map<String, ProjectMetricsResults> projectResults = fullResultsEntry.getValue();
            Map.Entry<String, ProjectMetricsResults> first = projectResults.entrySet().stream().iterator().next();
            Map<String, Object> projectExport;
            if (projectResults.size() == 1 && projectResults.containsKey(first.getValue().getProjectPath().toString())) {
                projectExport = first.getValue().toMap();
            } else {
                // TODO Find a way to receive remotePath of this project (if REMOTE_GIT mode) and pass it instead of null
                GitProjectResultsExporter gitProjectResultsExporter = new GitProjectResultsExporter(null);
                projectExport = gitProjectResultsExporter.export(projectResults);
            }
            content.put(projectId, projectExport);
        }
        return content;
    }
}
