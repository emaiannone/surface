package org.surface.surface.cli;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;
import org.surface.surface.core.analysis.selectors.AllRevisionSelector;
import org.surface.surface.core.analysis.selectors.RangeRevisionSelector;
import org.surface.surface.core.analysis.selectors.SingleRevisionSelector;

public class CLIOptions extends Options {
    private static CLIOptions INSTANCE;

    public static final String TARGET = "target";

    public static final String METRICS = "metrics";

    public static final String OUT_FILE = "outFile";

    public static final String RANGE = RangeRevisionSelector.CODE.toLowerCase();
    public static final String ALL = AllRevisionSelector.CODE.toLowerCase();
    public static final String AT = SingleRevisionSelector.CODE.toLowerCase();

    public static final String WORK_DIR = "workDir";

    public static final String FILES = "files";
    public static final String INCLUDE_TESTS = "includeTests";

    private CLIOptions() {
        Option target = Option.builder(TARGET)
                .hasArg(true)
                .required(true)
                .desc("Path to either (i) a local non-git directory (LOCAL), (ii) a local git directory (LOCAL_GIT), (iii) a remote URL to a GitHub repository (REMOTE_GIT), or (iv) a local path to a YAML file (FLEXIBLE). SURFACE behaves differently depending on the type of target: (LOCAL) it scans the specified directory recursively to search for .java files to analyze; (LOCAL_GIT) it behaves like in LOCAL but allows the selection of specific revisions; (REMOTE_GIT) it clones the GitHub repository inside the directory indicated by the -" + WORK_DIR + " option and runs the analysis on it, also allowing the selection of specific revisions; (FLEXIBLE) parses the YAML that dictates how SURFACE must operate. The specification of the YAML file for FLEXIBLE mode are reported in the README at https://github.com/emaiannone/surface. All the directories cloned during the execution of SURFACE will be deleted at the end (either successful or erroneous).")
                .build();

        Option metrics = Option.builder(METRICS)
                .hasArg(true)
                .numberOfArgs(Option.UNLIMITED_VALUES)
                .valueSeparator(',')
                .required(true)
                .desc("List of metrics to return or not with SURFACE. The list must be expressed as a comma-separate list of metrics codes, e.g., \"CAT,CMT,CIDA\" (no spaces between elements). The special argument \"ALL\" enables the execution of all metrics, shadowing all other codes in the list. If a code is preceded by a minus symbol (-), then the associated metric is excluded from the final report. If the format is invalid an error is raised. Any unrecognized code will be ignored. If there are not valid metrics to compute an error is raised.")
                .build();

        Option outFile = Option.builder(OUT_FILE)
                .hasArg(true)
                .required(true)
                .desc("Path to a file .json file where to store the results. If the file already exists, its content will be overwritten.")
                .build();

        Option range = Option.builder(RANGE)
                .hasArg(true)
                .desc("Revisions (commits) range on which to run SURFACE. Format: \"<START-SHA>..<END-SHA>\", where <START-SHA> must be reachable from <END-SHA> (i.e., is in its ancestor path in the main branch). Evaluated only when -" + TARGET + " is a remote URL. Evaluated only in LOCAL_GIT and REMOTE_GIT modes. Mutually exclusive with -" + ALL + " and -" + AT + " options. If none is specified, the analyses will be run on the repository's current state.")
                .build();
        Option all = Option.builder(ALL)
                .hasArg(false)
                .desc("Flag enabling the analysis of the entire project's history range on which to run SURFACE. Format: \"<START-SHA>..<END-SHA>\", where <START-SHA> must be reachable from <END-SHA> (i.e., is in its ancestor path in the main branch). Evaluated only when -" + TARGET + " is a remote URL. Evaluated only in LOCAL_GIT and REMOTE_GIT modes. Mutually exclusive with -" + RANGE + " and -" + AT + " options. If none is specified, the analyses will be run on the repository's current state.")
                .build();
        Option at = Option.builder(AT)
                .hasArg(true)
                .desc("Revision (commit) on which to run SURFACE. Format: \"<SHA>\", where <SHA> is part of the main branch. Evaluated only when -" + TARGET + " is a remote URL.Evaluated only in LOCAL_GIT and REMOTE_GIT modes. Mutually exclusive with -" + RANGE + " and -" + ALL + " options. If none is specified, the analyses will be run on the repository's current state.")
                .build();
        OptionGroup revisionGroup = new OptionGroup()
                .addOption(range)
                .addOption(all)
                .addOption(at);
        revisionGroup.setRequired(false);

        Option cloneDir = Option.builder(WORK_DIR)
                .hasArg(true)
                .desc("Path to a local directory where repositories will be copied (LOCAL_GIT or FLEXIBLE) or cloned (REMOTE_GIT or FLEXIBLE). Not evaluated in LOCAL_DIR mode. In FLEXIBLE mode it is the default directory where all remote repositories are cloned if not specified differently in the YAML file.")
                .build();

        Option files = Option.builder(FILES)
                .hasArg(true)
                .required(false)
                .desc("(Optional) Regular expression to select the .java files on which SURFACE is run. If not specified, all the parsable .java files in the target project will be select. In FLEXIBLE mode it is used as default regular expression, if not specified differently in the YAML file.")
                .build();

        Option includeTests = Option.builder(INCLUDE_TESTS)
                .hasArg(false)
                .required(false)
                .desc("(Optional) Flag admitting test files (i.e., classes with \"Test\"-like annotations, e.g. @Test or @ParameterizedTest. Disabled by default.")
                .build();

        addOption(target);
        addOption(metrics);
        addOption(outFile);
        addOptionGroup(revisionGroup);
        addOption(cloneDir);
        addOption(files);
        addOption(includeTests);
    }

    public static CLIOptions getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CLIOptions();
        }
        return INSTANCE;
    }

}
