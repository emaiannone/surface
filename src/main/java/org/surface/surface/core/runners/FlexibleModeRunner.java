package org.surface.surface.core.runners;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.surface.surface.core.Utils;
import org.surface.surface.core.analysis.selectors.RevisionSelector;
import org.surface.surface.core.metrics.api.Metric;
import org.surface.surface.core.metrics.results.ProjectMetricsResults;
import org.surface.surface.core.out.exporters.MixedProjectsResultsExporter;
import org.surface.surface.core.out.writers.FileWriter;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class FlexibleModeRunner extends ModeRunner<Map<String, Map<String, ProjectMetricsResults>>> {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final String CODE_NAME = "FLEXIBLE";

    private final Path configFilePath;
    private final RevisionSelector defaultRevisionSelector;
    private final Path defaultWorkDirPath;

    public FlexibleModeRunner(Path configFilePath, List<Metric<?, ?>> metrics, FileWriter writer, String filesRegex, RevisionSelector defaultRevisionSelector, Path defaultWorkDirPath) {
        super(metrics, writer, filesRegex);
        this.configFilePath = configFilePath;
        this.defaultRevisionSelector = defaultRevisionSelector;
        this.defaultWorkDirPath = defaultWorkDirPath;
        setCodeName(CODE_NAME);
        setResultsExporter(new MixedProjectsResultsExporter());
    }

    @Override
    public void run() throws Exception {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        File configFile = configFilePath.toFile();
        if (!Utils.isYamlFile(configFile)) {
            throw new IllegalStateException("The target configuration file does not exist or is not a YAML file.");
        }
        Configuration configuration = mapper.readValue(configFile, Configuration.class);

        List<ProjectConfiguration> projects = configuration.projects;
        Map<String, Map<String, ProjectMetricsResults>> fullResults = new LinkedHashMap<>();
        for (int i = 0, projectsSize = projects.size(); i < projectsSize; i++) {
            ProjectConfiguration project = projects.get(i);

            String projectId = project.id;
            if (projectId == null || projectId.equals("")) {
                LOGGER.warn("* Project #{} has an invalid ID. Assigning the default project name.", i);
                projectId = project.getDefaultProjectName();
            }

            /* TODO OLD CODE: Revamp
            RunMode runMode;
            try {
                runMode = ModeRunnerFactory.discernRunMode(project.location);
            } catch (IllegalArgumentException e) {
                LOGGER.warn("* Project #{} has an invalid 'location' field. The project will be ignored.", i);
                continue;
            }

            List<String> metrics;
            try {
                metrics = MetricsFormulaParser.parseMetricsFormula(project.metrics.split(","));
            } catch (IllegalArgumentException e) {
                LOGGER.warn("* Project #{} has an invalid 'metrics' field. The project will be ignored.", i);
                continue;
            }

            // TODO Keep validating the other parts (see CLIArgumentParser)
            // TODO Build the history analyzer, run it collect the results. Progressively, export to the outFile (just call exportResults())
            System.out.println(projectId);
            System.out.println(runMode);
            System.out.println(metrics);
             */
        }
    }

    private static class Configuration {
        public List<ProjectConfiguration> projects;

        public Configuration() {
        }
    }

    private static class ProjectConfiguration {
        // Can either be a local path or a remote URL
        public String id;
        public String location;
        public String metrics;
        public String filesRegex;
        public List<RevisionConfiguration> revisions;

        public ProjectConfiguration() {
        }

        String getDefaultProjectName() {
            return Paths.get(location).getFileName().toString();
        }
    }

    private static class RevisionConfiguration {
        public String at;
        public String range;
        public Boolean all;

        public RevisionConfiguration() {
        }
    }
}
