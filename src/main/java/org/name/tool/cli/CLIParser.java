package org.name.tool.cli;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.name.tool.core.ToolInput;
import org.name.tool.core.metrics.ca.ClassifiedAttributes;

import java.nio.file.Paths;
import java.util.Optional;

public class CLIParser {

    // Current working directory as default
    private static final String DEFAULT_PROJECT = "";
    // csv as default
    private static final String DEFAULT_EXPORT = "csv";
    // List of default metrics
    private static final String[] DEFAULT_METRICS = new String[]{
            ClassifiedAttributes.CODE
    };

    public CLIParser() {
    }

    public ToolInput parse(String[] args) throws ParseException {
        // Accepted CLI options
        Options options = CLIOptions.getInstance();
        CommandLineParser cliParser = new DefaultParser();
        CommandLine commandLine = cliParser.parse(options, args);
        String project = Optional.ofNullable(commandLine.getOptionValue(CLIOptions.PROJECT)).orElse(DEFAULT_PROJECT);
        String export = Optional.ofNullable(commandLine.getOptionValue(CLIOptions.EXPORT)).orElse(DEFAULT_EXPORT);
        String[] metricsCodes = Optional.ofNullable(commandLine.getOptionValues(CLIOptions.METRICS)).orElse(DEFAULT_METRICS);
        return new ToolInput(Paths.get(project).toAbsolutePath(), metricsCodes, export);
    }
}
