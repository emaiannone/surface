package org.surface.surface.cli;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;
import org.surface.surface.core.engine.analysis.selectors.*;

public class CLIOptions extends Options {
    public static final String TARGET = "target";
    public static final String WORK_DIR = "workDir";
    public static final String OUT_FILE = "outFile";
    public static final String CLASSIFIED_PATTERNS = "classifiedPatterns";
    public static final String METRICS = "metrics";
    public static final String BRANCH = "branch";
    public static final String RANGE = RangeRevisionSelector.CODE.toLowerCase();
    public static final String FROM = FromRevisionSelector.CODE.toLowerCase();
    public static final String TO = ToRevisionSelector.CODE.toLowerCase();
    public static final String ALLOW = AllowRevisionSelector.CODE.toLowerCase();
    public static final String DENY = DenyRevisionSelector.CODE.toLowerCase();
    public static final String ALL = AllRevisionSelector.CODE.toLowerCase();
    public static final String AT = AtRevisionSelector.CODE.toLowerCase();
    public static final String FILES = "files";
    public static final String INCLUDE_TESTS = "includeTests";
    public static final String EXCLUDE_WORK_TREE = "excludeWorkTree";
    public static final String HELP = "help";
    private static CLIOptions INSTANCE;

    private CLIOptions() {
        Option targetOpt = Option.builder(TARGET)
                .hasArg(true)
                .desc("Path to either (i) a local non-git directory (LOCAL_DIR), (ii) a local git directory (LOCAL_GIT), (iii) a remote URL to a remote git repository (REMOTE_GIT), or (iv) a local path to a YAML file (FLEXIBLE). Surface behaves differently depending on the type of target: (LOCAL_DIR) it scans the specified directory recursively to search for .java files to analyze; (LOCAL_GIT) it behaves like LOCAL_DIR but allows the selection of specific revisions; (REMOTE_GIT) it clones the remote git repository inside the directory indicated by the -" + WORK_DIR + " option and runs the analysis on it, also allowing the selection of specific revisions; (FLEXIBLE) parses the YAML file that indicates how Surface must operate. The specification of this YAML file are reported in the README at https://github.com/emaiannone/surface. All the repositories cloned during the execution will be deleted at the end (either successful or erroneous).")
                .build();

        Option workDirOpt = Option.builder(WORK_DIR)
                .hasArg(true)
                .desc("Path to a local directory where git repositories will be copied (LOCAL_GIT or FLEXIBLE) or cloned (REMOTE_GIT or FLEXIBLE). This option is ignored in LOCAL_DIR mode. In FLEXIBLE mode, this option represents the default directory where all remote repositories are cloned when not specified differently in the YAML file.")
                .build();

        Option outFileOpt = Option.builder(OUT_FILE)
                .hasArg(true)
                .desc("Path to a file where to store the results. If the path points to an existing file, its content will be overwritten. The output format is determined by the extension of the supplied filename. Currently, Surface only supports JSON (with .json extension) and plain text (with .txt extension).")
                .build();

        Option classifiedPatterns = Option.builder(CLASSIFIED_PATTERNS)
                .hasArg(true)
                .desc("Path to a file containing the patterns to use for detecting the classified attributes. The file is interpreted as a text file with a pattern on each non-empty line. If this option is not supplied, or the path points to a non existing, unreadable, or empty file, a built-in set of patterns will be used.")
                .build();

        Option metricsOpt = Option.builder(METRICS)
                .hasArg(true)
                .numberOfArgs(Option.UNLIMITED_VALUES)
                .valueSeparator(',')
                .desc("List of metrics Surface must and must not compute. The list is expressed as a comma-separate list of codes, e.g., \"CAT,CMT,CIDA\", without any space between elements. The special code \"ALL\" requests for the computation of all the supported metrics, shadowing all other codes in the list. If a code is preceded by a minus symbol (-), then the metric with that code will not be computed. If the format of this list is invalid an error is raised. Any unrecognized code will be ignored. After processing, there is no valid metric to compute, the analysis will not start at all. In FLEXIBLE mode, this option is considered when when not specified differently in the YAML file. ")
                .build();

        Option filesOpt = Option.builder(FILES)
                .hasArg(true)
                .desc("Pattern for selecting the .java files on which Surface will operate. If not specified, all the parsable .java files in the target project will be considered. The pattern looks for a match in any part of the file's absolute path. In FLEXIBLE mode, this option is considered when not specified differently in the YAML file.")
                .build();

        Option range = Option.builder(RANGE)
                .hasArg(true)
                .desc("Revisions (commits) range to analyze. Format: \"<START-SHA>..<END-SHA>\", where <START-SHA> must be reachable from <END-SHA> (i.e., is in its ancestor path), following a similar syntax to git log. Evaluated only in LOCAL_GIT and REMOTE_GIT modes. Mutually exclusive with -" + FROM + ", -" + TO + ", -" + ALLOW + ", -" + DENY + ", -" + ALL + ", -" + AT + " options. If none is specified, the analysis will be run on the repository's HEAD.")
                .build();
        Option from = Option.builder(FROM)
                .hasArg(true)
                .desc("Revision (commit) from which select the commits to analyze (inclusive). Format: \"<SHA>\", where <SHA> is the hash of the first revision to analyze. Evaluated only in LOCAL_GIT and REMOTE_GIT modes. Mutually exclusive with -" + RANGE + ", -" + TO + ", -" + ALLOW + ", -" + DENY + ", -" + ALL + ", -" + AT + " options. If none is specified, the analysis will be run on the repository's HEAD.")
                .build();
        Option to = Option.builder(TO)
                .hasArg(true)
                .desc("Revision (commit) up to which select the commits to analyze (inclusive). Format: \"<SHA>\", where <SHA> is the hash of the last revision to analyze. Evaluated only in LOCAL_GIT and REMOTE_GIT modes. Mutually exclusive with -" + RANGE + ", -" + FROM + ", -" + ALLOW + ", -" + DENY + ", -" + ALL + ", -" + AT + " options. If none is specified, the analysis will be run on the repository's HEAD.")
                .build();
        Option allow = Option.builder(ALLOW)
                .hasArg(true)
                .desc("Path to a file containing a list of revisions (commits) to analyze. The file must have one revision per line. Evaluated only in LOCAL_GIT and REMOTE_GIT modes. Mutually exclusive with -" + RANGE + ", -" + FROM + ", -" + TO + ", -" + DENY + ", -" + ALL + ", -" + AT + " options. If none is specified, the analysis will be run on the repository's HEAD.")
                .build();
        Option deny = Option.builder(DENY)
                .hasArg(true)
                .desc("Path to a file containing a list of revisions (commits) NOT to analyze. The file must have one revision per line. Evaluated only in LOCAL_GIT and REMOTE_GIT modes. Mutually exclusive with -" + RANGE + ", -" + FROM + ", -" + TO + ", -" + ALLOW + ", -" + ALL + ", -" + AT + " options. If none is specified, the analysis will be run on the repository's HEAD.")
                .build();
        Option all = Option.builder(ALL)
                .hasArg(false)
                .desc("Flag enabling the analysis of the entire project's history. Format: \"<START-SHA>..<END-SHA>\", where <START-SHA> must be reachable from <END-SHA> (i.e., is in its ancestor path). Evaluated only in LOCAL_GIT and REMOTE_GIT modes. Mutually exclusive with -" + RANGE + ", -" + FROM + ", -" + TO + ", -" + ALLOW + ", -" + DENY + ", -" + AT + " options. If none is specified, the analysis will be run on the repository's HEAD.")
                .build();
        Option at = Option.builder(AT)
                .hasArg(true)
                .desc("Revision (commit) to analyze. Format: \"<SHA>\", where <SHA> is the hash of the target revision. Evaluated only in LOCAL_GIT and REMOTE_GIT modes. Mutually exclusive with -" + RANGE + ", -" + FROM + ", -" + TO + ", -" + ALLOW + ", -" + DENY + ", -" + ALL + " options. If none is specified, the analysis will be run on the repository's HEAD.")
                .build();
        OptionGroup revisionGroup = new OptionGroup()
                .addOption(range)
                .addOption(from)
                .addOption(to)
                .addOption(allow)
                .addOption(deny)
                .addOption(all)
                .addOption(at);
        revisionGroup.setRequired(false);

        Option branchOpt = Option.builder(BRANCH)
                .hasArg(true)
                .desc("Name of the branch to analyze. Both short (\"<BRANCH-NAME>\") and complete (\"refs/.../<BRANCH-NAME>\") reference names are accepted. If not specified, all branches are taken into account when selecting the revisions to analyze and the HEAD is placed on the repository's default branch.")
                .build();

        Option includeTestsOpt = Option.builder(INCLUDE_TESTS)
                .hasArg(false)
                .desc("Flag for involving test files in the analysis, that is, classes with \"Test\"-like annotations, e.g., @Test or @ParameterizedTest. Disabled by default.")
                .build();

        Option excludeWorkTreeOpt = Option.builder(EXCLUDE_WORK_TREE)
                .hasArg(false)
                .desc("Flag for excluding the files changed locally (that is, in the work tree) from the analysis. Disabled by default.")
                .build();

        Option helpOpt = Option.builder(HELP)
                .hasArg(false)
                .desc("Show the options available. Has the precedence over all the other options.")
                .build();

        addOption(targetOpt);
        addOption(workDirOpt);
        addOption(classifiedPatterns);
        addOption(metricsOpt);
        addOption(outFileOpt);
        addOption(branchOpt);
        addOptionGroup(revisionGroup);
        addOption(filesOpt);
        addOption(includeTestsOpt);
        addOption(excludeWorkTreeOpt);
        addOption(helpOpt);
    }

    public static CLIOptions getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CLIOptions();
        }
        return INSTANCE;
    }

}
