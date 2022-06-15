package org.surface.surface.core.configuration.runners;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.surface.surface.core.Utils;
import org.surface.surface.core.configuration.interpreters.MetricsFormulaInterpreter;
import org.surface.surface.core.configuration.interpreters.RevisionGroupInterpreter;
import org.surface.surface.core.configuration.runners.results.FlexibleRunResults;
import org.surface.surface.core.engine.analysis.Analyzer;
import org.surface.surface.core.engine.analysis.HistoryAnalyzer;
import org.surface.surface.core.engine.analysis.SnapshotAnalyzer;
import org.surface.surface.core.engine.analysis.results.AnalysisResults;
import org.surface.surface.core.engine.analysis.selectors.*;
import org.surface.surface.core.engine.analysis.setup.CloneSetupEnvironmentAction;
import org.surface.surface.core.engine.analysis.setup.CopySetupEnvironmentAction;
import org.surface.surface.core.engine.analysis.setup.SetupEnvironmentAction;
import org.surface.surface.core.engine.metrics.api.MetricsManager;
import org.surface.surface.core.engine.writers.FileWriter;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class FlexibleRunningMode extends RunningMode<FlexibleRunResults> {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final String CODE_NAME = "FLEXIBLE";

    private final Path configFilePath;
    private final boolean defaultExcludeWorkTree;
    private final RevisionSelector defaultRevisionSelector;
    private final Path workDirPath;

    public FlexibleRunningMode(Path configFilePath, MetricsManager metricsManager, FileWriter writer, String filesRegex, boolean includeTests, boolean defaultExcludeWorkTree, RevisionSelector defaultRevisionSelector, Path workDirPath) {
        super(metricsManager, writer, filesRegex, includeTests);
        this.configFilePath = configFilePath;
        this.defaultExcludeWorkTree = defaultExcludeWorkTree;
        this.defaultRevisionSelector = defaultRevisionSelector;
        this.workDirPath = workDirPath;
        setCodeName(CODE_NAME);
        setRunResults(new FlexibleRunResults());
    }

    @Override
    public void run() {
        FlexibleRunResults runResults = new FlexibleRunResults();
        Map<String, Analyzer> analyzers = prepareAnalyzers();
        if (analyzers.size() == 0) {
            throw new RuntimeException("Could not run any analyzer because all projects specifications in the configuration file had errors.");
        }
        List<String> failedProjects = new ArrayList<>();
        LOGGER.info("* Going to analyze the following projects: {}", analyzers.keySet());
        for (Map.Entry<String, Analyzer> analyzerEntry : analyzers.entrySet()) {
            String projectIdKey = analyzerEntry.getKey();
            try {
                AnalysisResults analysisResults = analyzerEntry.getValue().analyze();
                // Assign a new but similar ID if there is a collision
                while (runResults.containsKey(projectIdKey)) {
                    projectIdKey += "_" + UUID.randomUUID();
                }
                runResults.addAnalysisResults(projectIdKey, analysisResults);
                try {
                    exportResults(runResults);
                    LOGGER.info("* Results updated in {}", getWriter().getOutFile());
                } catch (IOException e) {
                    failedProjects.add(projectIdKey);
                    LOGGER.error("* Found a problem during the export of the results for project \"" + projectIdKey + "\". Going to the next one. Details:");
                    LOGGER.error("\t* {}", e.getMessage());
                }
            } catch (Exception e) {
                failedProjects.add(projectIdKey);
                LOGGER.error("* Found a problem during the analysis of project \"" + projectIdKey + "\". Going to the next one. Details:");
                LOGGER.error("\t* {}", e.getMessage());
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
            if (project.files != null) {
                try {
                    Pattern.compile(project.files);
                    filesRegex = project.files;
                } catch (PatternSyntaxException e) {
                    filesRegex = getFilesRegex();
                    LOGGER.warn("Project \"{}\": The regular expression to filter files must be compilable. Using the default:{}", projectId, filesRegex);
                }
                LOGGER.info("* Project \"{}\": Going to analyze only the .java files matching this expression {}", projectId, filesRegex);
            } else {
                filesRegex = getFilesRegex();
                LOGGER.warn("* Project \"{}\": No regular expression to filter files supplied. Using the default option.", projectId);
            }

            // Check the inclusion of test files
            boolean includeTests = false;
            if (project.includeTests != null) {
                if (project.includeTests) {
                    includeTests = true;
                    LOGGER.info("* Project \"{}\": Going to include test files as well.", projectId);
                }
            } else {
                includeTests = isIncludeTests();
                LOGGER.info("* Project \"{}\": No indication on how to manage test files. Using the default option.", projectId);
            }
            // Check the exclusion of files in the work tree
            boolean excludeWorkTree = false;
            if (project.excludeWorkTree != null) {
                if (project.excludeWorkTree) {
                    excludeWorkTree = true;
                    LOGGER.info("* Project \"{}\": Going to exclude files in the work tree.", projectId);
                }
            } else {
                excludeWorkTree = defaultExcludeWorkTree;
                LOGGER.info("* Project \"{}\": No indication on how to manage files in the work tree. Using the default option.", projectId);
            }

            // Interpret the Revision Filter
            RevisionSelector revisionSelector;
            if (project.revisionFilter == null) {
                revisionSelector = defaultRevisionSelector;
                LOGGER.warn("* Project \"{}\": No revisions specified. Using the default option", projectId);
            } else {
                Pair<String, String> selectedRevision = project.revisionFilter.getSelectedRevision();
                if (selectedRevision == null) {
                    revisionSelector = defaultRevisionSelector;
                    LOGGER.warn("* Project \"{}\": No valid revision selector supplied. Using the default: {}", projectId, revisionSelector);
                } else {
                    try {
                        revisionSelector = RevisionGroupInterpreter.interpretRevisionGroup(selectedRevision.getKey(), selectedRevision.getValue());
                        LOGGER.info("* Project \"{}\": Going to analyze \"{} {}\" revisions", projectId, selectedRevision.getKey(), selectedRevision.getValue());
                    } catch (IllegalArgumentException e) {
                        revisionSelector = defaultRevisionSelector;
                        LOGGER.warn("* Project \"{}\": The supplied revision selector must fulfill the requirements of each type (see options documentation). Using the default: {}", projectId, revisionSelector);
                    }
                }
            }

            // Instantiate the appropriate analyzer
            Analyzer analyzer;
            if (isSnapshot) {
                analyzer = new SnapshotAnalyzer(path, filesRegex, metricsManager, includeTests);
            } else {
                analyzer = new HistoryAnalyzer(projectId, filesRegex, metricsManager, includeTests, excludeWorkTree, revisionSelector, setupEnvironmentAction);
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
        public String files;
        public Boolean includeTests;
        public Boolean excludeWorkTree;
        public RevisionConfiguration revisionFilter;

        public ProjectConfiguration() {
        }
    }

    private static class RevisionConfiguration {
        public String type;
        public String value;

        public RevisionConfiguration() {
        }

        Pair<String, String> getSelectedRevision() {
            if (type.equalsIgnoreCase(AllRevisionSelector.CODE)) {
                return new ImmutablePair<>(type, "");
            }
            if (value != null
                    && type.equalsIgnoreCase(SingleRevisionSelector.CODE)
                    || type.equalsIgnoreCase(RangeRevisionSelector.CODE)
                    || type.equalsIgnoreCase(FromRevisionSelector.CODE)
                    || type.equalsIgnoreCase(ToRevisionSelector.CODE)
                    || type.equalsIgnoreCase(AllowRevisionSelector.CODE)
                    || type.equalsIgnoreCase(DenyRevisionSelector.CODE)
            ) {
                return new ImmutablePair<>(type, value);
            }
            return null;
        }
    }
}
