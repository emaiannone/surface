package org.surface.surface.core.runners;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.surface.surface.core.Utils;
import org.surface.surface.core.analysis.Analyzer;
import org.surface.surface.core.analysis.HistoryAnalyzer;
import org.surface.surface.core.analysis.SnapshotAnalyzer;
import org.surface.surface.core.analysis.selectors.AllRevisionSelector;
import org.surface.surface.core.analysis.selectors.RangeRevisionSelector;
import org.surface.surface.core.analysis.selectors.RevisionSelector;
import org.surface.surface.core.analysis.selectors.SingleRevisionSelector;
import org.surface.surface.core.analysis.setup.CloneSetupEnvironmentAction;
import org.surface.surface.core.analysis.setup.CopySetupEnvironmentAction;
import org.surface.surface.core.analysis.setup.SetupEnvironmentAction;
import org.surface.surface.core.interpreters.MetricsFormulaInterpreter;
import org.surface.surface.core.interpreters.RevisionGroupInterpreter;
import org.surface.surface.core.metrics.api.MetricsManager;
import org.surface.surface.core.metrics.results.ProjectMetricsResults;
import org.surface.surface.core.out.exporters.MixedProjectsResultsExporter;
import org.surface.surface.core.out.writers.FileWriter;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class FlexibleModeRunner extends ModeRunner<Map<String, Map<String, ProjectMetricsResults>>> {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final String CODE_NAME = "FLEXIBLE";

    private final Path configFilePath;
    private final RevisionSelector defaultRevisionSelector;
    private final Path workDirPath;

    public FlexibleModeRunner(Path configFilePath, MetricsManager metricsManager, FileWriter writer, String filesRegex, RevisionSelector defaultRevisionSelector, Path workDirPath) {
        super(metricsManager, writer, filesRegex);
        this.configFilePath = configFilePath;
        this.defaultRevisionSelector = defaultRevisionSelector;
        this.workDirPath = workDirPath;
        setCodeName(CODE_NAME);
        setResultsExporter(new MixedProjectsResultsExporter());
    }

    @Override
    public void run() throws Exception {
        Map<String, Map<String, ProjectMetricsResults>> fullResults = new LinkedHashMap<>();
        Map<String, Analyzer> analyzers = prepareAnalyzers();
        if (analyzers.size() == 0) {
            throw new RuntimeException("Could not build any analyzer because the configuration file did not contain any valid project configuration.");
        }
        for (Map.Entry<String, Analyzer> analyzerEntry : analyzers.entrySet()) {
            Map<String, ProjectMetricsResults> projectMetricsResults = analyzerEntry.getValue().analyze();
            String projectIdKey = analyzerEntry.getKey();
            // Regenerate ID if there is a collision
            while (fullResults.containsKey(projectIdKey)) {
                projectIdKey += "_" + UUID.randomUUID();
            }
            fullResults.put(projectIdKey, projectMetricsResults);
            exportResults(fullResults);
        }
    }

    private Map<String, Analyzer> prepareAnalyzers() {
        Map<String, Analyzer> analyzers = new LinkedHashMap<>();

        Configuration configuration = readConfigFile();
        LOGGER.info("* Parsing configuration file {}", configFilePath.toString());
        List<ProjectConfiguration> projects = configuration.projects;
        for (int i = 0, projectsSize = projects.size(); i < projectsSize; i++) {
            ProjectConfiguration project = projects.get(i);

            String projectId = project.id;
            if (projectId == null || projectId.equals("")) {
                LOGGER.warn("* Project #{} has an invalid ID. Using its index as id.", i);
                projectId = String.valueOf(i);
            }

            // Interpret Metrics
            MetricsManager metricsManager;
            try {
                metricsManager = MetricsFormulaInterpreter.interpretMetricsFormula(project.metrics, ",");
            } catch (RuntimeException e) {
                LOGGER.warn("* Project {}: Invalid metrics formula, which must be a list of comma-separate metric codes without any space in between. Using the default metrics.", projectId);
                metricsManager = getMetricsManager();
            }
            LOGGER.debug("* Project {}: Going to compute the following metrics: {}", projectId, metricsManager.getMetricsCodes());

            // Validate regex on files
            String filesRegex;
            if (project.filesRegex != null) {
                try {
                    Pattern.compile(project.filesRegex);
                    filesRegex = project.filesRegex;
                } catch (PatternSyntaxException e) {
                    LOGGER.warn("Project {} : The regular expression to filter files must be compilable. Using the default regular expression.", projectId);
                    filesRegex = getFilesRegex();
                }
                LOGGER.info("* Project {}: Going to analyze only the .java files matching this expression {}", projectId, filesRegex);
            } else {
                LOGGER.warn("* Project {}: No regular expression supplied. Using the default regular expression.", projectId);
                filesRegex = project.filesRegex;
            }

            // Interpret the Revision group
            RevisionSelector revisionSelector;
            if (project.revisions == null || project.revisions.size() == 0) {
                LOGGER.warn("* Project {}: No revision options found. Using the default revision option.", projectId);
                revisionSelector = defaultRevisionSelector;
            } else {
                RevisionConfiguration revisionConfiguration = project.revisions.get(0);
                if (project.revisions.size() > 1) {
                    LOGGER.warn("* Project {}: Selecting only the first revision filter found", projectId);
                }
                Pair<String, String> selectedRevision = revisionConfiguration.getSelectedRevision();
                if (selectedRevision == null) {
                    LOGGER.warn("* Project {}: No valid revision option supplied. Using the default revision option.", projectId);
                    revisionSelector = defaultRevisionSelector;
                } else {
                    try {
                        revisionSelector = RevisionGroupInterpreter.interpretRevisionGroup(selectedRevision.getKey(), selectedRevision.getValue());
                        LOGGER.info("* Project {}: Going to analyze {} {} ", projectId, selectedRevision.getKey(), selectedRevision.getValue());
                    } catch (IllegalArgumentException e) {
                        LOGGER.warn("* Project {}: The supplied revision option must fulfill the requirements of each type (see options documentation). Using the default revision option.", projectId);
                        revisionSelector = defaultRevisionSelector;
                    }
                }
            }

            // Interpret location to prepare the setup
            Path path = Paths.get(project.location).toAbsolutePath();
            Analyzer analyzer;
            if (Utils.isPathToLocalDirectory(path)) {
                analyzer = new SnapshotAnalyzer(path, filesRegex, metricsManager);
            } else {
                SetupEnvironmentAction setupEnvironmentAction = null;
                if (Utils.isPathToGitDirectory(path)) {
                    setupEnvironmentAction = new CopySetupEnvironmentAction(projectId, workDirPath, path);
                }
                if (Utils.isGitHubUrl(project.location)) {
                    try {
                        setupEnvironmentAction = new CloneSetupEnvironmentAction(projectId, workDirPath, new URI(project.location));
                    } catch (URISyntaxException e) {
                        LOGGER.warn("Project {}: The location URL is malformed. Ignoring project.", projectId);
                        continue;
                    }
                }
                if (setupEnvironmentAction == null) {
                    LOGGER.warn("Project {}: The supplied location is not attributable to any supported run mode. Ignoring project.", projectId);
                }
                analyzer = new HistoryAnalyzer(projectId, filesRegex, metricsManager, revisionSelector, setupEnvironmentAction);
            }
            analyzers.put(projectId, analyzer);
        }
        return analyzers;
    }

    private Configuration readConfigFile() {
        //YAMLMapper mapper = new YAMLMapper();
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        //mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        File configFile = configFilePath.toFile();
        if (!Utils.isYamlFile(configFile)) {
            throw new IllegalStateException("The target configuration file does not exist or is not a YAML file.");
        }
        try {
            return mapper.readValue(configFile, Configuration.class);
        } catch (IOException e) {
            throw new RuntimeException("Could not read file " + configFilePath + " likely because it has an invalid structure", e);
        }
    }

    private static class Configuration {
        public List<ProjectConfiguration> projects;

        public Configuration() {
        }
    }

    private static class ProjectConfiguration {
        public String id;
        public String location;
        public String metrics;
        public String filesRegex;
        public List<RevisionConfiguration> revisions;

        public ProjectConfiguration() {
        }
    }

    private static class RevisionConfiguration {
        public String at;
        public String range;
        public Boolean all;

        public RevisionConfiguration() {
        }

        Pair<String, String> getSelectedRevision() {
            if (at != null && range == null && all == null) {
                return new ImmutablePair<>(SingleRevisionSelector.CODE, at);
            }
            if (at == null && range != null && all == null) {
                return new ImmutablePair<>(RangeRevisionSelector.CODE, range);
            }
            if (at == null && range == null && all != null) {
                return new ImmutablePair<>(AllRevisionSelector.CODE, "");
            }
            return null;
        }
    }
}
