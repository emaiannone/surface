package org.surface.surface.cli;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

public class CLIOptions extends Options {
    private static CLIOptions INSTANCE;

    public static final String METRICS = "metrics";
    public static final String REMOTE_PROJECTS = "remoteProjects";
    public static final String PROJECT = "project";
    public static final String EXPORT = "export";
    public static final String OUT_FILE = "outFile";

    private CLIOptions() {
        Option metrics = new Option(METRICS, true, "List of metrics of comma separated to be compute. If not present, a set of defaults are chosen.");
        metrics.setArgs(Option.UNLIMITED_VALUES);
        metrics.setValueSeparator(',');
        Option remoteProjects = new Option(REMOTE_PROJECTS, true, "Path to a csv file containing, at least, 'projectID' with the project name, 'github' column with the remote repository URI and 'commitHash' the commit to checkout on. The successfully cloned projects will be placed in /tmp directory, and, then, individually analyzed. It has the priority over -project option.");
        Option project = new Option(PROJECT, true, "Path to a root directory of a project containing ALL .java files to be analyzed. It is considered if -remoteProject is not set. If both -project and -remoteProject are not set, then the current working directory is chosen as default.");
        Option export = new Option(EXPORT, true, "Generate an export of the results of the specified format ({csv} or {json}). If not set, nothing will be exported. Note: {csv} is only available for Remote mode, while {json} only in Local mode.");
        Option outFile = new Option(OUT_FILE, true, "The path of the destination file where the export will be made.");
        addOption(metrics);
        addOption(remoteProjects);
        addOption(project);
        addOption(export);
        addOption(outFile);
    }

    public static CLIOptions getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CLIOptions();
        }
        return INSTANCE;
    }
}
