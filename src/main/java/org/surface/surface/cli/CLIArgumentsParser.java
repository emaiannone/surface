package org.surface.surface.cli;

import org.apache.commons.cli.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.surface.surface.core.Utils;
import org.surface.surface.core.configuration.interpreters.MetricsFormulaInterpreter;
import org.surface.surface.core.configuration.interpreters.OutFileInterpreter;
import org.surface.surface.core.configuration.interpreters.RevisionGroupInterpreter;
import org.surface.surface.core.configuration.runners.RunningMode;
import org.surface.surface.core.configuration.runners.RunningModeFactory;
import org.surface.surface.core.engine.analysis.selectors.RevisionSelector;
import org.surface.surface.core.engine.exporters.RunResultsExporter;
import org.surface.surface.core.engine.metrics.api.MetricsManager;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

class CLIArgumentsParser {
    public static final String HEADER = "Surface: a lightweight tool for computing security metrics from Java source code.\n\nOptions:";
    public static final String SYNTAX = "java -jar surface.jar";
    public static final String FOOTER = "\nPlease report any issue at https://github.com/emaiannone/surface";
    private static final Logger LOGGER = LogManager.getLogger();

    public static RunningMode parse(String[] args) throws ParseException {
        // Fetch the indicated CLI options
        Options options = CLIOptions.getInstance();
        CommandLineParser cliParser = new DefaultParser();
        CommandLine commandLine = cliParser.parse(options, args);
        HelpFormatter helpFormatter = new HelpFormatter();
        String target = commandLine.getOptionValue(CLIOptions.TARGET);

        if (commandLine.hasOption(CLIOptions.HELP)) {
            helpFormatter.printHelp(SYNTAX, HEADER, options, FOOTER, true);
            return null;
        }

        // Validate the Working Directory
        Path workDirPath;
        String workDirValue = commandLine.getOptionValue(CLIOptions.WORK_DIR);
        if (workDirValue == null) {
            throw new IllegalArgumentException("The working directory path must be indicated.");
        }
        workDirPath = Paths.get(workDirValue).toAbsolutePath();
        if (!Utils.isPathToLocalDirectory(workDirPath)) {
            throw new IllegalArgumentException("The working directory path must point to an existing directory.");
        }
        LOGGER.info("* Going to work in directory: " + workDirPath);

        // Interpret Output File to get the Writer
        String outFileValue = commandLine.getOptionValue(CLIOptions.OUT_FILE);
        RunResultsExporter runResultsExporter;
        if (outFileValue == null) {
            throw new IllegalArgumentException("The output file must be indicated.");
        }
        try {
            runResultsExporter = new OutFileInterpreter().interpret(outFileValue);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("The supplied output file path must point to a file of one of the supported type.", e);
        }
        LOGGER.info("* Going to export results in file: " + outFileValue);

        // Interpret Metrics
        MetricsManager metricsManager;
        try {
            metricsManager = new MetricsFormulaInterpreter().interpret(commandLine.getOptionValues(CLIOptions.METRICS));
            if (metricsManager.getNumberLoadedMetrics() > 0) {
                LOGGER.info("* Going to compute the following metrics: {}", metricsManager.getLoadedMetrics());
            } else {
                LOGGER.warn("* Failed to load any metric, likely because the supplied codes are invalid.");
            }
        } catch (IllegalArgumentException e) {
            //throw new IllegalArgumentException("The supplied metrics formula must be a list of comma-separate metric codes without any space in between.", e);
            metricsManager = null;
        }

        // Validate regex on files
        String filesRegex = null;
        if (commandLine.hasOption(CLIOptions.FILES)) {
            String filesValue = commandLine.getOptionValue(CLIOptions.FILES);
            try {
                Pattern.compile(filesValue);
                filesRegex = filesValue;
            } catch (PatternSyntaxException e) {
                throw new IllegalArgumentException("The supplied regular expression to filter files must be compilable.", e);
            }
            LOGGER.info("* Going to analyze only the .java files matching this expression: {}", filesRegex);
        } else {
            LOGGER.info("* Going to analyze all .java files found (default).");
        }

        // Interpret the Revision group
        RevisionSelector revisionSelector;
        String revisionValue;
        String revisionModeSelected = options.getOptionGroup(options.getOption(CLIOptions.RANGE)).getSelected();
        if (revisionModeSelected == null) {
            LOGGER.info("* Going to analyze the HEAD revision (default).");
            revisionValue = null;
        } else {
            revisionValue = commandLine.getOptionValue(revisionModeSelected);
        }
        try {
            revisionSelector = new RevisionGroupInterpreter().interpret(revisionModeSelected + RevisionGroupInterpreter.SEP + revisionValue);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("The supplied revision option must fulfill the requirements of each type (see options documentation).", e);
        }
        if (revisionSelector.getRevisionString() != null) {
            LOGGER.info("* Going to analyze \"{} {}\" revisions", revisionModeSelected, revisionValue);
        } else {
            LOGGER.info("* Going to analyze all revisions");
        }

        // Check the inclusion of test files
        boolean includeTests = false;
        if (commandLine.hasOption(CLIOptions.INCLUDE_TESTS)) {
            includeTests = true;
            LOGGER.info("* Going to include test files as well.");
        }
        // Check the exclusion of files in the work tree
        boolean excludeWorkTree = false;
        if (commandLine.hasOption(CLIOptions.EXCLUDE_WORK_TREE)) {
            excludeWorkTree = true;
            LOGGER.info("* Going to exclude files in the current work tree.");
        }

        // Create the appropriate RunningMode
        RunningMode runningMode = RunningModeFactory.newRunningMode(target, workDirPath, runResultsExporter, metricsManager, filesRegex, revisionSelector, includeTests, excludeWorkTree);
        LOGGER.info("* Going to run in mode: {}", runningMode.getCodeName());
        return runningMode;
    }
}
