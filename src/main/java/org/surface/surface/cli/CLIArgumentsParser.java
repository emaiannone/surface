package org.surface.surface.cli;

import org.apache.commons.cli.*;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.surface.surface.core.RevisionMode;
import org.surface.surface.core.RunMode;
import org.surface.surface.core.RunSetting;
import org.surface.surface.core.filter.RevisionsParser;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class CLIArgumentsParser {
    private static final Logger LOGGER = LogManager.getLogger();

    private static List<String> parseMetrics(String[] metricsFormula) {
        MetricsFormulaParser metricsFormulaParser = new MetricsFormulaParser();
        List<String> selectedMetrics;
        try {
            selectedMetrics = metricsFormulaParser.parse(metricsFormula);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("The list of metrics specified with " + CLIOptions.METRICS + " option is not indicated or not specified as a comma-separated list of acronyms of metrics.", e);
        }
        if (selectedMetrics.size() == 0) {
            throw new IllegalArgumentException("The list of metrics specified with " + CLIOptions.METRICS + " option resulted in an empty set of metrics.");
        }
        return selectedMetrics;
    }

    public static RunSetting parse(String[] args) throws ParseException {
        // Fetch the indicated CLI options
        Options options = CLIOptions.getInstance();
        CommandLineParser cliParser = new DefaultParser();
        CommandLine commandLine = cliParser.parse(options, args);

        // Parse Metrics, Mode
        List<String> selectedMetrics = parseMetrics(commandLine.getOptionValues(CLIOptions.METRICS));
        LOGGER.info("* Going to compute the following metrics: {}", selectedMetrics);
        String target = commandLine.getOptionValue(CLIOptions.TARGET);
        RunMode runMode = RunModeSelector.inferMode(target);
        LOGGER.info("* Going to run in the following mode: " + runMode);

        // Parse Output File
        String outFileValue = commandLine.getOptionValue(CLIOptions.OUT_FILE);
        Path outFilePath = Paths.get(outFileValue).toAbsolutePath();
        if (!FilenameUtils.getExtension(outFilePath.toFile().getName()).equalsIgnoreCase("json")) {
            throw new IllegalArgumentException("The output file must have extension .json.");
        }
        LOGGER.info("* Going to export results in file: " + outFilePath);

        // Parse the Revision group
        RevisionMode revisionMode = null;
        String revisionValue = null;
        if (runMode == RunMode.LOCAL_GIT || runMode == RunMode.REMOTE_GIT) {
            String revisionModeSelected = options.getOptionGroup(options.getOption(CLIOptions.RANGE)).getSelected();
            if (revisionModeSelected == null) {
                revisionMode = RevisionMode.CURRENT;
            } else {
                switch (revisionModeSelected) {
                    case CLIOptions.RANGE: {
                        revisionValue = commandLine.getOptionValue(CLIOptions.RANGE);
                        // Check only if the range has the expected format, not that the commits are valid
                        String[] revisionsFromRange = RevisionsParser.getRevisionsFromRange(revisionValue);
                        revisionMode = RevisionMode.RANGE;
                        break;
                    }
                    case CLIOptions.AT: {
                        revisionValue = commandLine.getOptionValue(CLIOptions.AT);
                        if (!RevisionsParser.isValidRevision(revisionValue)) {
                            throw new IllegalArgumentException("The revision must an alphanumeric string.");
                        }
                        revisionMode = RevisionMode.SINGLE;
                        break;
                    }
                    case CLIOptions.ALL: {
                        revisionMode = RevisionMode.ALL;
                        break;
                    }
                }
            }
            LOGGER.info("* Going to analyze the following revision: " + revisionMode);
        }

        // Parse the Clone Directory
        Path cloneDirPath = null;
        if (runMode == RunMode.REMOTE_GIT || runMode == RunMode.FLEXIBLE) {
            String cloneDirValue = commandLine.getOptionValue(CLIOptions.CLONE_DIR);
            if (cloneDirValue == null) {
                throw new IllegalArgumentException("The path where to clone the remote repositories must be indicated.");
            }
            cloneDirPath = Paths.get(cloneDirValue).toAbsolutePath();
            File cloneDir = cloneDirPath.toFile();
            if (!cloneDirPath.toFile().exists() || !cloneDir.isDirectory()) {
                throw new IllegalArgumentException("The path where to clone the remote repositories must point to an existent directory.");
            }
            LOGGER.info("* Going to clone in the following directory: " + cloneDirPath);
        }

        // Parse Files regex
        String filesRegex = null;
        if (commandLine.hasOption(CLIOptions.FILES)) {
            String filesValue = commandLine.getOptionValue(CLIOptions.FILES);
            try {
                Pattern.compile(filesValue);
                filesRegex = filesValue;
            } catch (PatternSyntaxException e) {
                throw new IllegalArgumentException("The supplied regular expression must be compilable.", e);
            }
            LOGGER.info("* Going to analyze only the .java files matching this expression: " + filesRegex);
        } else {
            LOGGER.info("* Going to analyze all .java files found");
        }

        return new RunSetting(selectedMetrics, new ImmutablePair<>(runMode, target), outFilePath, filesRegex, cloneDirPath, new ImmutablePair<>(revisionMode, revisionValue));
    }
}
