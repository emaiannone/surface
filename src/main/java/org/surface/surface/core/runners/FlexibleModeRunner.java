package org.surface.surface.core.runners;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.surface.surface.common.RevisionMode;
import org.surface.surface.common.Utils;
import org.surface.surface.core.analysis.selectors.RevisionSelector;
import org.surface.surface.core.metrics.results.ProjectMetricsResults;
import org.surface.surface.out.exporters.MixedProjectsResultsExporter;
import org.surface.surface.out.writers.Writer;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class FlexibleModeRunner extends ModeRunner<Map<String, Map<String, ProjectMetricsResults>>> {
    private static final Logger LOGGER = LogManager.getLogger();

    private final Path workDirPath;
    private final RevisionSelector defaultRevisionSelector;

    FlexibleModeRunner(List<String> metrics, String target, Path outFilePath, String filesRegex, Pair<RevisionMode, String> revision, Path workDirPath) {
        super(metrics, target, outFilePath, filesRegex);
        this.workDirPath = workDirPath;
        this.defaultRevisionSelector = RevisionSelector.newRevisionSelector(revision);
        Writer writer = Writer.newWriter(getOutFilePath());
        setResultsExporter(new MixedProjectsResultsExporter(writer));
    }

    @Override
    public void run() throws Exception {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        File configFile = Paths.get(getTarget()).toFile();
        if (!Utils.isYamlFile(configFile)) {
            throw new IllegalStateException("The target configuration file does not exist or is not a YAML file.");
        }
        Configuration configuration = mapper.readValue(Paths.get(getTarget()).toFile(), Configuration.class);

        List<ProjectConfiguration> projects = configuration.projects;
        Map<String, Map<String, ProjectMetricsResults>> fullResults = new LinkedHashMap<>();
        for (int i = 0, projectsSize = projects.size(); i < projectsSize; i++) {
            ProjectConfiguration project = projects.get(i);
            if (project.id == null || project.id.equals("")) {
                LOGGER.warn("* Project #{} has an invalid ID. Assigning the default project name.", i);
                project.id = project.getDefaultProjectName();
            }
            // TODO Validate. If not valid, skip it, otherwise instantiate an HistoryAnalyzer.
            //  Check if the validation logic can be factorized from CliArgumentsParser and placed elsewhere (validators?)
            // TODO Run history analyzer and collect the results. Progressively, export to the outFile

            System.out.println(project.location);
            System.out.println(project.metrics);
        }
    }

    private static class Configuration {
        public List<ProjectConfiguration> projects;
        public Configuration() {}
    }

    private static class ProjectConfiguration {
        // Can either be a local path or a remote URL
        public String id;
        public String location;
        public String metrics;
        public String filesRegex;
        public List<RevisionConfiguration> revisions;
        public ProjectConfiguration() {}

        String getDefaultProjectName() {
            return Paths.get(location).getFileName().toString();
        }
    }

    private static class RevisionConfiguration {
        public String at;
        public String range;
        public Boolean all;
        public RevisionConfiguration() {}
    }
}
