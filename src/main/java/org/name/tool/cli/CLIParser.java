package org.name.tool.cli;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.name.tool.core.ToolInput;
import org.name.tool.core.metrics.ca.ClassifiedAttributes;

import java.nio.file.Paths;
import java.util.Arrays;

public class CLIParser {

    // Current working directory as default
    private static final String DEFAULT_PROJECT = "";
    // List of default metrics
    private static final String[] DEFAULT_METRICS = new String[]{
            ClassifiedAttributes.CODE
    };
    // csv as default
    private static final String DEFAULT_EXPORT = "csv";

    public CLIParser() {
    }

    public ToolInput parse(String[] args) throws ParseException {
        // Accepted CLI options
        Options options = CLIOptions.getInstance();
        CommandLineParser cliParser = new DefaultParser();
        CommandLine commandLine = cliParser.parse(options, args);

        String project;
        if (commandLine.hasOption(CLIOptions.PROJECT)) {
            project = commandLine.getOptionValue(CLIOptions.PROJECT);
            System.out.println("* Using " + project + " as project root.");
        } else {
            project = DEFAULT_PROJECT;
            System.out.println("* No specified project root: going to use Current Working Directory.");
        }

        String[] metricsCodes;
        if (commandLine.hasOption(CLIOptions.METRICS)) {
            metricsCodes = commandLine.getOptionValues(CLIOptions.METRICS);
            System.out.println("* Going to compute the following metrics: " + Arrays.toString(metricsCodes) + ".");
        } else {
            metricsCodes = DEFAULT_METRICS;
            System.out.println("* No specified set of metrics: going to compute the following defaults: " + Arrays.toString(metricsCodes) + ".");
        }

        String export;
        if (commandLine.hasOption(CLIOptions.EXPORT)) {
            export = commandLine.getOptionValue(CLIOptions.EXPORT);
            System.out.println("* Going to export as " + export + " file.");
        } else {
            export = DEFAULT_EXPORT;
            System.out.println("* No specified export format: going to export as " + export + " file.");
        }
        return new ToolInput(Paths.get(project).toAbsolutePath(), metricsCodes, export);
    }
}
