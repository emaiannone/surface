package org.surface.surface.core.runners;

import com.fasterxml.jackson.databind.DeserializationFeature;
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
import java.util.*;
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
    public void run() {
        Map<String, Map<String, ProjectMetricsResults>> fullResults = new LinkedHashMap<>();
        Map<String, Analyzer> analyzers = prepareAnalyzers();
        if (analyzers.size() == 0) {
            throw new RuntimeException("Could not run any analyzer because all projects specifications in the configuration file had errors.");
        }
        List<String> failedProjects = new ArrayList<>();
        LOGGER.info("* Going to analyze the following projects: {}", analyzers.keySet());
        for (Map.Entry<String, Analyzer> analyzerEntry : analyzers.entrySet()) {
            // TODO If there is an exception, instead of letting it pass, catch it and go to the next project. Store the failed project and print the in stdout to warn the user of the failed runs
            String projectIdKey = analyzerEntry.getKey();
            try {
                Map<String, ProjectMetricsResults> projectMetricsResults = analyzerEntry.getValue().analyze();
                // Assign a new but similar ID if there is a collision
                while (fullResults.containsKey(projectIdKey)) {
                    projectIdKey += "_" + UUID.randomUUID();
                }
                fullResults.put(projectIdKey, projectMetricsResults);
                try {
                    exportResults(fullResults);
                    LOGGER.info("* Results updated in {}", getWriter().getOutFile());
                } catch (IOException e) {
                    failedProjects.add(projectIdKey);
                    LOGGER.error("* Found a problem during the export of the results for project \"" + projectIdKey + "\". Going to the next one. Details:");
                    LOGGER.error("\t* {}", e.getMessage());
                    continue;
                }
            } catch (Exception e) {
                failedProjects.add(projectIdKey);
                LOGGER.error("* Found a problem during the analysis of project \"" + projectIdKey + "\". Going to the next one. Details:");
                LOGGER.error("\t* {}", e.getMessage());
                continue;
            }
        }
        if (failedProjects.size() > 0) {
            LOGGER.info("* The following projects could not be analyzed successfully because of some errors during the process: {}", failedProjects);
        }
    }

    private Map<String, Analyzer> prepareAnalyzers() {
        Map<String, Analyzer> analyzers = new LinkedHashMap<>();
        List<String> ignoredProjects = new ArrayList<>();
        LOGGER.info("* Starting {} mode: parsing configuration file {} and using all given options as default", CODE_NAME, configFilePath.toString());
        Configuration configuration = readConfigFile();
        List<ProjectConfiguration> projects = configuration.projects;
        for (int i = 0, projectsSize = projects.size(); i < projectsSize; i++) {
            ProjectConfiguration project = projects.get(i);

            // Interpret ID
            String projectId = project.id;
            if (projectId == null || projectId.equals("")) {
                LOGGER.warn("* Project #{} has an invalid ID. Using its index as ID.", i);
                projectId = String.valueOf(i);
            }

            // Interpret location to prepare the setup
            Path path = Paths.get(project.location).toAbsolutePath();
            boolean isSnapshot = false;
            SetupEnvironmentAction setupEnvironmentAction = null;
            if (Utils.isPathToLocalDirectory(path)) {
                isSnapshot = true;
            } else {
                if (Utils.isPathToGitDirectory(path)) {
                    setupEnvironmentAction = new CopySetupEnvironmentAction(projectId, workDirPath, path);
                }
                if (Utils.isGitHubUrl(project.location)) {
                    try {
                        setupEnvironmentAction = new CloneSetupEnvironmentAction(projectId, workDirPath, new URI(project.location));
                    } catch (URISyntaxException e) {
                        ignoredProjects.add(projectId);
                        LOGGER.warn("* Project \"{}\": The location URL is malformed. Ignoring project.", projectId);
                        continue;
                    }
                }
                if (setupEnvironmentAction == null) {
                    ignoredProjects.add(projectId);
                    LOGGER.warn("* Project \"{}\": The supplied location is not attributable to any supported run mode. Ignoring project.", projectId);
                    continue;
                }
            }

            // Interpret Metrics
            MetricsManager metricsManager;
            try {
                metricsManager = MetricsFormulaInterpreter.interpretMetricsFormula(project.metrics, ",");
            } catch (RuntimeException e) {
                metricsManager = getMetricsManager();
                LOGGER.warn("* Project \"{}\": Invalid metrics formula: must be a list of comma-separate metric codes without any space in between. Using the default: {}", projectId, metricsManager.getMetricsCodes());
            }
            LOGGER.debug("* Project \"{}\": Going to compute the following metrics: {}", projectId, metricsManager.getMetricsCodes());

            // Validate regex on files
            String filesRegex;
            if (project.filesRegex != null) {
                try {
                    Pattern.compile(project.filesRegex);
                    filesRegex = project.filesRegex;
                } catch (PatternSyntaxException e) {
                    filesRegex = getFilesRegex();
                    LOGGER.warn("Project \"{}\": The regular expression to filter files must be compilable. Using the default:{}", projectId, filesRegex);
                }
                LOGGER.info("* Project \"{}\": Going to analyze only the .java files matching this expression {}", projectId, filesRegex);
            } else {
                filesRegex = getFilesRegex();
                LOGGER.warn("* Project \"{}\": No regular expression supplied. Using the default: {}", projectId, filesRegex);
            }

            // Interpret the Revision group
            RevisionSelector revisionSelector;
            if (project.revisions == null || project.revisions.size() == 0) {
                revisionSelector = defaultRevisionSelector;
                LOGGER.warn("* Project \"{}\": No revision options found. Using the default: {}", projectId, revisionSelector);
            } else {
                RevisionConfiguration revisionConfiguration = project.revisions.get(0);
                if (project.revisions.size() > 1) {
                    LOGGER.warn("* Project \"{}\": Selecting only the first revision filter found.", projectId);
                }
                Pair<String, String> selectedRevision = revisionConfiguration.getSelectedRevision();
                if (selectedRevision == null) {
                    revisionSelector = defaultRevisionSelector;
                    LOGGER.warn("* Project \"{}\": No valid revision option supplied. Using the default: {}", projectId, revisionSelector);
                } else {
                    try {
                        revisionSelector = RevisionGroupInterpreter.interpretRevisionGroup(selectedRevision.getKey(), selectedRevision.getValue());
                        LOGGER.info("* Project \"{}\": Going to analyze {} {} ", projectId, selectedRevision.getKey(), selectedRevision.getValue());
                    } catch (IllegalArgumentException e) {
                        revisionSelector = defaultRevisionSelector;
                        LOGGER.warn("* Project \"{}\": The supplied revision option must fulfill the requirements of each type (see options documentation). Using the default: {}", projectId, revisionSelector);
                    }
                }
            }

            // Instantiate the appropriate analyzer
            Analyzer analyzer;
            if (isSnapshot) {
                analyzer = new SnapshotAnalyzer(path, filesRegex, metricsManager);
            } else {
                analyzer = new HistoryAnalyzer(projectId, filesRegex, metricsManager, revisionSelector, setupEnvironmentAction);
            }
            analyzers.put(projectId, analyzer);
        }
        if (ignoredProjects.size() > 0) {
            LOGGER.info("* The following projects will not be analyzed because of errors in the configuration file: {}", ignoredProjects);
        }
        return analyzers;
    }

    private Configuration readConfigFile() {
        //YAMLMapper mapper = new YAMLMapper();
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
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
