package org.surface.surface.cli;

import org.apache.commons.cli.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.surface.surface.core.Utils;
import org.surface.surface.core.analysis.selectors.RevisionSelector;
import org.surface.surface.core.interpreters.MetricsFormulaInterpreter;
import org.surface.surface.core.interpreters.OutFileInterpreter;
import org.surface.surface.core.interpreters.RevisionGroupInterpreter;
import org.surface.surface.core.metrics.api.MetricsManager;
import org.surface.surface.core.out.writers.FileWriter;
import org.surface.surface.core.runners.ModeRunner;
import org.surface.surface.core.runners.ModeRunnerFactory;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

class CLIArgumentsParser {
    private static final Logger LOGGER = LogManager.getLogger();

    public static ModeRunner<?> parse(String[] args) throws ParseException {
        // Fetch the indicated CLI options
        Options options = CLIOptions.getInstance();
        CommandLineParser cliParser = new DefaultParser();
        CommandLine commandLine = cliParser.parse(options, args);

        String target = commandLine.getOptionValue(CLIOptions.TARGET);

        // Interpret Metrics
        MetricsManager metricsManager;
        try {
            metricsManager = MetricsFormulaInterpreter.interpretMetricsFormula(commandLine.getOptionValues(CLIOptions.METRICS));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("The supplied metrics formula must be a list of comma-separate metric codes without any space in between.", e);
        }
        LOGGER.info("* Going to compute the following metrics: {}", metricsManager.getMetricsCodes());

        // Interpret Output File to get the Writer
        String outFileValue = commandLine.getOptionValue(CLIOptions.OUT_FILE);
        FileWriter writer;
        try {
            writer = OutFileInterpreter.interpretOutString(outFileValue);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("The supplied output file path must point to a file of one of the supported type.", e);
        }
        LOGGER.info("* Going to export results in file: " + outFileValue);

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
        String revisionModeSelected = options.getOptionGroup(options.getOption(CLIOptions.RANGE)).getSelected();
        String revisionValue = commandLine.getOptionValue(revisionModeSelected);
        try {
            revisionSelector = RevisionGroupInterpreter.interpretRevisionGroup(revisionModeSelected, revisionValue);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("The supplied revision option must fulfill the requirements of each type (see options documentation).", e);
        }
        if (revisionSelector.getRevisionString() == null) {
            LOGGER.info("* Going to analyze the HEAD revision (default).");
        } else {
            LOGGER.info("* Going to analyze \"{} {}\" revisions", revisionModeSelected, revisionValue);
        }

        // Validate the Working Directory
        Path workDirPath = null;
        if (!Utils.isPathToLocalDirectory(Paths.get(target))) {
            String workDirValue = commandLine.getOptionValue(CLIOptions.WORK_DIR);
            if (workDirValue == null) {
                throw new IllegalArgumentException("The working directory path must be indicated.");
            }
            workDirPath = Paths.get(workDirValue).toAbsolutePath();
            if (!Utils.isPathToLocalDirectory(workDirPath)) {
                throw new IllegalArgumentException("The working directory path must point to an existing directory.");
            }
            LOGGER.info("* Going to clone in the following directory: " + workDirPath);
        }

        // Interpret RunMode
        ModeRunner<?> modeRunner = ModeRunnerFactory.newModeRunner(target, metricsManager, writer, filesRegex, revisionSelector, workDirPath);
        LOGGER.info("* Going to run mode: {}", modeRunner.getCodeName());
        return modeRunner;
    }
}
