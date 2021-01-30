package org.name.tool.cli;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

public class CLIOptions extends Options {
    private static CLIOptions INSTANCE;

    public static final String METRICS = "metrics";
    public static final String REMOTE_PROJECTS = "remoteProjects";
    public static final String PROJECT = "project";
    public static final String EXPORT = "export";

    private CLIOptions() {
        Option metrics = new Option(METRICS, true, "List of metrics of comma separated to be compute. If not present, a set of defaults are chosen.");
        metrics.setArgs(Option.UNLIMITED_VALUES);
        metrics.setValueSeparator(',');
        Option remoteProjects = new Option(REMOTE_PROJECTS, true, "Path to a text file containing, on separate lines, a list of git remote repositories, which will be cloned in /tmp directory. All successfully closed repository are, then, individually analyzed. It has the priority over -project option.");
        Option project = new Option(PROJECT, true, "Path to a root directory of a project containing ALL .java files to be analyzed. It is considered if -remoteProject is not set. If both -project and -remoteProject are not set, then the current working directory is chosen as default.");
        Option export = new Option(EXPORT, true, "Generate a {csv, json} export of the results into the current working directory. If not set, csv is chosen.");
        addOption(metrics);
        addOption(remoteProjects);
        addOption(project);
        addOption(export);
    }

    public static CLIOptions getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CLIOptions();
        }
        return INSTANCE;
    }
}
