package org.name.tool.cli;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

public class CLIOptions extends Options {
    private static CLIOptions INSTANCE;

    public static final String PROJECT = "project";
    public static final String METRICS = "metrics";
    public static final String EXPORT = "export";


    private CLIOptions() {
        Option project = new Option(PROJECT, true, "Root directory of the project containing ALL .java files to be analyzed. If not set, the current working directory is chosen.");
        Option metrics = new Option(METRICS, true, "List of metrics of comma separated to be compute. If not present, all available are chosen.");
        metrics.setArgs(Option.UNLIMITED_VALUES);
        metrics.setValueSeparator(',');
        Option export = new Option(EXPORT, true, "Generate a {csv, json} export of the results into the current working directory. If not set, csv is chosen.");
        addOption(project);
        addOption(metrics);
        addOption(export);
    }

    public static CLIOptions getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CLIOptions();
        }
        return INSTANCE;
    }
}
