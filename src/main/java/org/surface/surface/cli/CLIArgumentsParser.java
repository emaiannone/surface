package org.surface.surface.cli;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.validator.routines.UrlValidator;
import org.surface.surface.core.RevisionMode;
import org.surface.surface.core.RunMode;
import org.surface.surface.core.RunSetting;

import java.io.File;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class CLIArgumentsParser {

    private List<String> parseMetrics(String[] metricsFormula) {
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

    private RunMode determineMode(String target) {
        if (UrlValidator.getInstance().isValid(target) && target.contains("github.com")) {
            return RunMode.REMOTE_GIT;
        }
        try {
            File file = Paths.get(target).toAbsolutePath().toFile();
            if (file.exists()) {
                if (file.isDirectory()) {
                    boolean isGit = Arrays.stream(Objects.requireNonNull(file.listFiles())).anyMatch(dir -> dir.getName().equals(".git"));
                    if (isGit) {
                        return RunMode.LOCAL_GIT;
                    }
                    return RunMode.LOCAL;
                } else if (file.isFile()) {
                    String extension = FilenameUtils.getExtension(file.getName());
                    if (extension.equalsIgnoreCase("yml")
                            || extension.equalsIgnoreCase("yaml")) {
                        return RunMode.FLEXIBLE;
                    }
                }
            }
        } catch (InvalidPathException ignored) {
        }
        throw new IllegalArgumentException("The supplied target must be (i) a local non-git directory (LOCAL), (ii) a local git directory (LOCAL_GIT), (iii) a remote URL to a GitHub repository (REMOTE_GIT), or (iv) a local path to a YAML file (FLEXIBLE).");
    }

    public RunSetting parse(String[] args) throws ParseException {
        // Fetch the indicated CLI options
        Options options = CLIOptions.getInstance();
        CommandLineParser cliParser = new DefaultParser();
        CommandLine commandLine = cliParser.parse(options, args);

        // Parse Metrics, Mode
        List<String> selectedMetrics = parseMetrics(commandLine.getOptionValues(CLIOptions.METRICS));
        System.out.println("* Going to compute the following metrics: " + selectedMetrics);
        RunMode mode = determineMode(commandLine.getOptionValue(CLIOptions.TARGET));
        System.out.println("* Going to run in the following mode: " + mode);

        // Parse Output File
        String outFileValue = commandLine.getOptionValue(CLIOptions.OUT_FILE);
        File outFile = Paths.get(outFileValue).toAbsolutePath().toFile();
        if (!FilenameUtils.getExtension(outFile.getName()).equalsIgnoreCase("json")) {
            throw new IllegalArgumentException("The output file must have extension .json.");
        }
        String outFileDest = outFile.getAbsolutePath();
        System.out.println("* Going to export results in file: " + outFileDest);

        // Parse the Revision group
        RevisionMode revisionMode = null;
        if (mode == RunMode.LOCAL_GIT || mode == RunMode.REMOTE_GIT) {
            String revisionModeSelected = options.getOptionGroup(options.getOption(CLIOptions.RANGE)).getSelected();
            if (revisionModeSelected == null) {
                revisionMode = RevisionMode.HEAD;
            } else {
                RevisionsValidator revisionsValidator = new RevisionsValidator();
                switch (revisionModeSelected) {
                    case CLIOptions.RANGE: {
                        String revisionRangeValue = commandLine.getOptionValue(CLIOptions.RANGE);
                        // Check only if the range has the expected format, not that the commits are valid
                        String[] revisionsFromRange = revisionsValidator.getRevisionsFromRange(revisionRangeValue);
                        revisionMode = RevisionMode.RANGE;
                        break;
                    }
                    case CLIOptions.AT: {
                        String revisionValue = commandLine.getOptionValue(CLIOptions.AT);
                        if (!revisionsValidator.isValidRevision(revisionValue)) {
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
            System.out.println("* Going to analyze the following revision: " + revisionMode);
        }

        // Parse the Clone Directory
        String cloneDest;
        if (mode == RunMode.REMOTE_GIT || mode == RunMode.FLEXIBLE) {
            String cloneDirValue = commandLine.getOptionValue(CLIOptions.CLONE_DIR);
            if (cloneDirValue == null) {
                throw new IllegalArgumentException("The path where to clone the remote repositories must be indicated.");
            }
            File cloneDir = Paths.get(cloneDirValue).toAbsolutePath().toFile();
            if (!cloneDir.exists() || !cloneDir.isDirectory()) {
                throw new IllegalArgumentException("The path where to clone the remote repositories must point to an existent directory.");
            }
            cloneDest = cloneDir.getAbsolutePath();
            System.out.println("* Going to clone in the following directory: " + cloneDest);
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
            System.out.println("* Going to analyze only the .java files matching this expression: " + filesRegex);
        } else {
            System.out.println("* Going to analyze all .java files found");
        }
        System.exit(1);

        // TODO Update RunSetting class
        return new RunSetting(null, null, null, null, null);
    }
}
