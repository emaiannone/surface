package org.surface.surface.cli;

import org.apache.commons.cli.*;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.surface.surface.common.RevisionMode;
import org.surface.surface.common.RunMode;
import org.surface.surface.common.RunSetting;
import org.surface.surface.common.Utils;
import org.surface.surface.common.parsers.MetricsParser;
import org.surface.surface.common.parsers.TargetParser;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

class CLIArgumentsParser {
    private static final Logger LOGGER = LogManager.getLogger();

    public static RunSetting parse(String[] args) throws ParseException {
        // Fetch the indicated CLI options
        Options options = CLIOptions.getInstance();
        CommandLineParser cliParser = new DefaultParser();
        CommandLine commandLine = cliParser.parse(options, args);

        // Parse RunMode
        String target = commandLine.getOptionValue(CLIOptions.TARGET);
        RunMode runMode;
        try {
            runMode = TargetParser.parseTargetString(target);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("The supplied target must be (i) a local non-git directory (LOCAL), (ii) a local git directory (LOCAL_GIT), (iii) a remote URL to a GitHub repository (REMOTE_GIT), or (iv) a local path to a YAML file (FLEXIBLE).", e);
        }
        LOGGER.info("* Going to run in the following mode: " + runMode);

        // Parse Metrics
        List<String> selectedMetrics;
        try {
            selectedMetrics = MetricsParser.parseMetricsString(commandLine.getOptionValues(CLIOptions.METRICS));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("The supplied metrics formula must be a list of comma-separate metric codes without any space in between.", e);
        }
        LOGGER.info("* Going to compute the following metrics: {}", selectedMetrics);

        // Parse Output File
        String outFileValue = commandLine.getOptionValue(CLIOptions.OUT_FILE);
        Path outFilePath = Paths.get(outFileValue).toAbsolutePath();
        if (!Utils.hasJsonExtension(outFilePath)) {
            throw new IllegalArgumentException("The supplied output must have a supported extension.");
        }

        LOGGER.info("* Going to export results in file: " + outFilePath);

        // Parse the Revision group
        RevisionMode revisionMode = null;
        String revisionValue = null;
        if (runMode == RunMode.LOCAL_GIT || runMode == RunMode.REMOTE_GIT) {
            String revisionModeSelected = options.getOptionGroup(options.getOption(CLIOptions.RANGE)).getSelected();
            if (revisionModeSelected == null) {
                revisionMode = RevisionMode.HEAD;
            } else {
                switch (revisionModeSelected) {
                    case CLIOptions.RANGE: {
                        revisionValue = commandLine.getOptionValue(CLIOptions.RANGE);
                        // Check only if the range has the expected format, not that the commits are valid
                        String[] range = Utils.getRevisionsFromRange(revisionValue);
                        revisionMode = RevisionMode.RANGE;
                        break;
                    }
                    case CLIOptions.AT: {
                        revisionValue = commandLine.getOptionValue(CLIOptions.AT);
                        if (!Utils.isAlphaNumeric(revisionValue)) {
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

        // Parse the Working Directory
        Path workDirPath = null;
        if (runMode != RunMode.LOCAL_DIR) {
            String workDirValue = commandLine.getOptionValue(CLIOptions.WORK_DIR);
            if (workDirValue == null) {
                throw new IllegalArgumentException("The path where to copy or clone the repositories must be indicated.");
            }
            workDirPath = Paths.get(workDirValue).toAbsolutePath();
            if (!workDirPath.toFile().isDirectory()) {
                throw new IllegalArgumentException("The path where to copy or clone the repositories must point to a directory.");
            }
            LOGGER.info("* Going to clone in the following directory: " + workDirPath);
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

        return new RunSetting(selectedMetrics, new ImmutablePair<>(runMode, target), outFilePath, filesRegex, workDirPath, new ImmutablePair<>(revisionMode, revisionValue));
    }
}
