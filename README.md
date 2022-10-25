# Surface

Surface (Java SecURity FlAws metriCs Extractor) is a command-line tool written in Java that can compute security metrics
over the history of Java projects using a fully-automated static code analysis.

It supports all **25 data-flow security metrics** defined by Alshammari et
al. (https://doi.org/10.1109/QSIC.2011.31), also at different granularity levels, whenever possible:

1. [x] CAT - Classified Attributes Total (Class, Project)
2. [x] CMT - Classified Methods Total (Class, Project)
3. [x] CCT - Critical Classes Total (Project)
4. [x] CIDA - Classified Instance Data Accessibility (Class, Project)
5. [x] CCDA - Classified Class Data Accessibility (Class, Project)
6. [x] COA - Classified Operation Accessibility (Class, Project)
7. [x] RPB - Reflection Package Boolean (Class, Project)
8. [x] CMAI - Classified Mutator Attribute Interactions (Class, Project)
9. [x] CAAI - Classified Accessor Attribute Interactions (Class, Project)
10. [x] CAIW - Classified Attributes Interaction Weight (Class, Project)
11. [x] CMW - Classified Methods Weight (Class, Project)
12. [x] CWMP - Classified Writing Methods Proportion (Class, Project)
13. [x] CCC - Critical Classes Coupling (Project)
14. [x] CPCC - Composite-Part Critical Classes (Project)
15. [x] CCE - Critical Classes Extensibility (Project)
16. [x] CME - Classified Methods Extensibility (Project)
17. [x] UACA - Unaccessed Assigned Classified Attribute (Class, Project)
18. [x] UCAM - Uncalled Classified Accessor Method (Class, Project)
19. [x] UCAC - Unused Critical Accessor Class (Project)
20. [x] CDP - Critical Design Proportion (Project)
21. [x] CSCP - Critical Serialized Classes Proportion (Project)
22. [x] CSP - Critical Superclasses Proportion (Project)
23. [x] CSI - Critical Superclass Inheritance (Project)
24. [x] CMI - Classified Methods Inheritance (Project)
25. [x] CAI - Classified Attributes Inheritance (Project)

# How to Build Surface

Requirements:

- JDK Version 8
- Maven

First, clone this repository into your local working directory. Then, move inside the projects' root directory and
run `mvn package` (make sure you have `maven` installed). This command will generate the JAR with all the dependencies
inside the `target` directory, which can be freely renamed if desired.

# How to Run Surface

Requirements:

- JRE Version 8

The Surface executable JAR can be run using `java -jar` command. The basic syntax is the following:  
`java -jar surface.jar [-all | -allow <arg> | -at <arg> | -deny <arg> | -from <arg> | -range <arg> | -to <arg>] [-excludeWorkTree] [-files <arg>] [-help] [-includeTests] -metrics <arg> -outFile <arg>  -target <arg> [-workDir <arg>]`

Surface accepts the following options:

- `-target <arg>` indicates the target of the analyses. Surface accepts four types of targets:
    - A path to a locally-stored non-`git` project (**LOCAL_DIR** mode).
    - A path to a locally-stored `git` project (**LOCAL_GIT** mode).
    - A URL to a remote `git` project (**REMOTE_GIT** mode).
    - A path to a YAML configuration file (**FLEXIBLE** mode).
- `-metrics <arg>` indicates the list of metrics to analyze. `<arg>` is a list of comma-separated strings of metrics
  codes (see above the list of supported metrics). The special keyword "ALL" enables the execution of all metrics. If
  one metric code is preceded by the minus symbol (-), then the associated metric will not be computed.
- `-workDir <arg>` indicates the local directory where Surface will copy the locally-stored projects or clone the
  projects from the remote.
- `-outFile <arg>` indicates the file where to export the JSON or TEXT results. Must have `.json` or `.txt` extension.
- (Optional) `-files <arg>` selects the subset of files to analyze based on the regular expression `<arg>` applied to
  the names.
- (Optional) One among `-all`, `-allow`, `-at`, `-deny`, `-from`, `-range`, `-to` to select the revisions (commits) to
  analyze. If not specified, only the HEAD revision will be analyzed. Not interpreted when the target project has no
  history (not a `git` project):
    - `-all` analyzes all revisions.
    - `-allow <arg>` analyzes only the revisions appearing in file `<arg>` (one revision per line).
    - `-deny <arg>` analyzes all revisions but the ones appearing in file `<arg>` (one revision per line).
    - `-at <arg>` analyzes only the revision `<arg>`.
    - `-from <arg>` analyzes all revisions starting from `<arg>` until HEAD.
    - `-to <arg>` analyzes all revisions from the beginning to `<arg>`.
    - `-range <arg>` analyzes the revision range `<arg>`, that must follow the syntax `<from-sha>..<to-sha>`.
- (Optional) `-excludeWorkTree` disables the analysis of uncommitted local changes for `git` projects.
- (Optional) `-includeTests` allows the analysis of test classes (i.e., those having @Test-like annotations).
- (Optional) `-help` displays the description of all command-line options accepted.

## How to Configure Surface with a YAML file (FLEXIBLE mode)

When Surface is supplied with a YAML configuration file, it is run in the so-called FLEXIBLE mode. In this mode, Surface
will interpret the configuration file to enable the analysis of multiple projects (either local or remote) at once. Each
project can be configured with individual configurations (e.g., revision or file filters), using the command-line
arguments as default in case of missing YAML parameters.

The YAML configuration file follows this structure:

```
projects:
  - [id: <STRING>]
    location: <PATH-TO-LOCAL-PROJECTS>
    [metrics: <LIST-COMMA-SEPARATE-CODES>]
    [files: <REG-EXP>]
    [revisionFilter:
      type: all|allow|deny|at|from|to|range
      [value: <SHA>|<SHA>..<SHA>]
    ]
    [includeTests: true|false]
    [excludeWorkTree: true|false]
  - ...
```

Each parameter follows the same syntax as those defined by the command-line arguments. The YAML follows a slightly
different structure than the command-line arguments:

- `revisionFilter` is an object encompassing `-all`, `-allow`, `-deny`, `-at`, `-from`, `-to`, `-range` options.
- `location` is the equivalent of `target`, except that you cannot point to another YAML file.

## Examples

### Analyze a local non-git project (LOCAL_DIR mode)

`java -jar surface.jar -target path/to/project -workDir /home/myself -outFile /home/myself/results.json -files /dao/`

Run Surface on a locally-stored project, analyzing only the files in the "dao" sub-directory, using `/home/myself` as
the working directory where the project will be copied before analyzing it, and exporting the results
in `/home/myself/results.json`.

### Analyze a local git project (LOCAL_GIT mode)

`java -jar surface.jar -target path/to/project -workDir /home/myself -outFile /home/myself/results.json -from abcd1234 -includeTests`

Run Surface on a locally-stored git project, including the test files, from commit `abcd1234` (included) to the HEAD
revision (included), using `/home/myself` as the working directory where the project will be copied before analyzing it,
and exporting the results in `/home/myself/results.json`.

### Analyze a local git project (REMOTE_GIT mode)

`java -jar surface.jar -target https://github.com/org/project -workDir /home/myself -outFile /home/myself/results.json -range abcd1234..5678wxyz -excludeWorkTree`

Run Surface on a remotely-stored git project, including the test files, from commit `abcd1234` (excluded)
to `5678wxyz` (included), using `/home/myself` as the working directory where the project will be cloned before
analyzing it, and exporting the results in `/home/myself/results.json`. In addition, the extra analysis of any local
uncommitted change will not be done (`-excludeWorkTree`).

### Analyze multiple projects with YAML file (FLEXIBLE mode)

`java -jar surface.jar -target config.yml -workDir /tmp -outFile /tmp/results.json -all`

Run Surface following the configuration provided in `config.yml`, using `/tmp` as the working directory where the
projects will be copied or cloned before analyzing them, and exporting the results in `/tmp/results.json`.
If not specified differently in the YAML file, the complete history of all `git` projects will be analyzed (`-all`).
