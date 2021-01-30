package org.name.tool.cli;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.name.tool.core.ToolInput;
import org.name.tool.core.metrics.classlevel.ca.CA;
import org.name.tool.core.metrics.classlevel.cm.CM;

import java.util.Arrays;

public class CLIParser {

    // List of default metrics
    // FIXME: 25/01/21 Is this this strong coupling necessary?
    private static final String[] DEFAULT_METRICS = new String[]{
            CA.CODE,
            CM.CODE
    };
    // Current working directory as default
    private static final String DEFAULT_PROJECT = "";
    // csv as default
    private static final String DEFAULT_EXPORT = "csv";

    public ToolInput parse(String[] args) throws ParseException {
        // Accepted CLI options
        Options options = CLIOptions.getInstance();
        CommandLineParser cliParser = new DefaultParser();
        CommandLine commandLine = cliParser.parse(options, args);

        String[] metricsCodes;
        if (commandLine.hasOption(CLIOptions.METRICS)) {
            metricsCodes = commandLine.getOptionValues(CLIOptions.METRICS);
        } else {
            metricsCodes = DEFAULT_METRICS;
            System.out.println("* No specified set of metrics: using the defaults: " + Arrays.toString(metricsCodes) + ".");
        }

        String remoteProjects = null;
        if (commandLine.hasOption(CLIOptions.REMOTE_PROJECTS)) {
            remoteProjects = commandLine.getOptionValue(CLIOptions.REMOTE_PROJECTS);
        }

        String project;
        if (commandLine.hasOption(CLIOptions.PROJECT)) {
            project = commandLine.getOptionValue(CLIOptions.PROJECT);
        } else {
            project = DEFAULT_PROJECT;
            System.out.println("* No specified project root: using Current Working Directory.");
        }

        String export;
        if (commandLine.hasOption(CLIOptions.EXPORT)) {
            export = commandLine.getOptionValue(CLIOptions.EXPORT);
        } else {
            export = DEFAULT_EXPORT;
            System.out.println("* No specified export format: using " + export + " file.");
        }
        return new ToolInput(metricsCodes, remoteProjects, project, export);
    }
}
