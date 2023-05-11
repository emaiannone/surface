# Surface

Surface (SecURity FlAws metriCs Extractor) is a command-line tool written in Java that computes security metrics over the history of Java projects using a fully-automated static source code analysis.

Surface supports **all security metrics** in the hierarchical model defined by Alshammari et al. (https://doi.org/10.1109/QSIC.2011.31), also at different granularity levels, whenever possible:

- [x] Data-flow Metrics
  - [x] CAT - Classified Attributes Total (Class, Project)
  - [x] CMT - Classified Methods Total (Class, Project)
  - [x] CCT - Critical Classes Total (Project)
  - [x] CIDA - Classified Instance Data Accessibility (Class, Project)
  - [x] CCDA - Classified Class Data Accessibility (Class, Project)
  - [x] COA - Classified Operation Accessibility (Class, Project)
  - [x] RPB - Reflection Package Boolean (Class, Project)
  - [x] CMAI - Classified Mutator Attribute Interactions (Class, Project)
  - [x] CAAI - Classified Accessor Attribute Interactions (Class, Project)
  - [x] CAIW - Classified Attributes Interaction Weight (Class, Project)
  - [x] CMW - Classified Methods Weight (Class, Project)
  - [x] CWMP - Classified Writing Methods Proportion (Class, Project)
  - [x] CCC - Critical Classes Coupling (Project)
  - [x] CPCC - Composite-Part Critical Classes (Project)
  - [x] CCE - Critical Classes Extensibility (Project)
  - [x] CME - Classified Methods Extensibility (Project)
  - [x] UACA - Unaccessed Assigned Classified Attribute (Class, Project)
  - [x] UCAM - Uncalled Classified Accessor Method (Class, Project)
  - [x] UCAC - Unused Critical Accessor Class (Project)
  - [x] CDP - Critical Design Proportion (Project)
  - [x] CSCP - Critical Serialized Classes Proportion (Project)
  - [x] CSP - Critical Superclasses Proportion (Project)
  - [x] CSI - Critical Superclass Inheritance (Project)
  - [x] CMI - Classified Methods Inheritance (Project)
  - [x] CAI - Classified Attributes Inheritance (Project)
- [x] Readability and Writability Metrics
  - [x] RCA - Readability of Classified Attributes
  - [x] WCA - Writability of Classified Attributes
  - [x] RCM - Readability via Classified Methods
  - [x] WCM - Writability via Classified Methods
  - [x] RCC - Readability via Critical Classes
  - [x] WCC - Writability via Critical Classes
  - [x] SAM - Security Absolute Measurements
- [x] Security Design Principle Metrics
  - [x] PLP - Grant Least Privilege
  - [x] PRAS - Reduce Attack Surface
  - [x] PSWL - Secure the Weakest Link
  - [x] PFSD - Fail-Safe Defaults
  - [x] PLCM - Least Common Mechanism
  - [x] PI - Isolation
  - [x] PEM - Economy of Mechanism
- [x] TSI - Total Security Index

# How to Build Surface

Requirements:

- JDK Version 8+

Clone this repository into your local working directory and move inside the root:

```sh
git clone https://github.com/emaiannone/surface
cd surface
```

Build the project using the Maven Wrapper:

```sh
./mvnw clean package
```

If you are on Windows:

```cmd
mvnw.cmd clean install
```

If you don't want to use the wrapper, you can use your local Maven installation:

```sh
mvn clean package
```

In any case, after building the project you will find the executable JAR, with all the dependencies, in `target/surface-jar-with-dependencies.jar`. Feel free to move and rename this file.

# How to Run Surface

Requirements:

- JDK Version 8+

The Surface's executable JAR can be run using `java -jar` command. The basic syntax is the following:

```sh
java -jar surface.jar -target <arg> -outFile <arg> [-classifiedPatterns <arg>] [-metrics <arg>] [-workDir <arg>] [-all | -allow <arg> | -at <arg> | -deny <arg> | -from <arg> | -range <arg> | -to <arg>]  [-excludeWorkTree] [-files <arg>] [-includeTests] [-help]
```

Please, refer to the help (`-help`) for the complete description of each command-line option. Here you can find a summary:

- `-target <arg>` indicates the target of the analyses. Surface accepts four types of targets:
  - A path to a locally-stored non-`git` project (**LOCAL_DIR** mode).
  - A path to a locally-stored `git` project (**LOCAL_GIT** mode).
  - A URL to a remote `git` project (**REMOTE_GIT** mode).
  - A path to a YAML configuration file (**FLEXIBLE** mode).
- `-outFile <arg>` indicates the file where to export the JSON or TEXT results. Must have `.json` or `.txt` extension.
- (Optional in **FLEXIBLE**) `-classifiedPatterns <arg>` indicates the file containing the patterns (regular expressions) for detecting the classified attributes.
- (Optional in **FLEXIBLE**) `-metrics <arg>` indicates the list of metrics to analyze. `<arg>` is a list of comma-separated metric codes (see above the list of supported metrics). The special code "ALL" enables the execution of all metrics. If one metric code is preceded by the minus symbol (-), then the associated metric will not be computed.
- (Optional) `-workDir <arg>` indicates the local directory where Surface will work, that is where it will copy the locally-stored projects or clone the remote projects.
- (Optional) `-files <arg>` selects the .java files to analyze based on the pattern `<arg>` applied to their absolute path. If not supplied, all are considered.
- (Optional) `-branch <arg>` selects the branch to analyze. Can either be a short name or the complete reference name (e.g., `refs/.../<BRANCH-NAME>`). When not specified, all branches are taken into account when selecting the revisions to analyze and the HEAD is set to the default branch.
- (Optional) One among `-all`, `-allow`, `-at`, `-deny`, `-from`, `-range`, `-to` to select the revisions (commits) to analyze. If not specified, only the HEAD revision (i.e., the latest one) of the target branch is analyzed. Not interpreted when the target project has no history (not a `git` project):
  - `-all` analyzes all revisions.
  - `-allow <arg>` analyzes only the revisions appearing in file `<arg>` (one revision per line).
  - `-deny <arg>` analyzes all revisions but the ones appearing in file `<arg>` (one revision per line).
  - `-at <arg>` analyzes only the revision `<arg>`.
  - `-from <arg>` analyzes all revisions starting from `<arg>` until HEAD of the target branch.
  - `-to <arg>` analyzes all revisions from the beginning to `<arg>`.
  - `-range <arg>` analyzes the revision range `<arg>`, that must follow the syntax `<from-sha>..<to-sha>`.
- (Optional) `-excludeWorkTree` disables the analysis of uncommitted local changes for `git` projects.
- (Optional) `-includeTests` allows the analysis of test classes (i.e., those having @Test-like annotations).
- (Optional) `-help` displays the description of all command-line options.

## How to Configure Surface with a YAML file (i.e., FLEXIBLE mode)

When the `-target` option is given a YAML configuration file, Surface is run in **FLEXIBLE** mode. In this mode, Surface can analyze multiple projects (either local or remote) at once. Each project can be configured with individual configurations (e.g., revision or classified patterns), using the command-line arguments as default when a YAML parameter is missing.

The YAML configuration file follows this structure (the order of the parameters is irrelevant):

```
projects:
  - [id: <STRING>]
    location: <PATH-TO-LOCAL-PROJECTS>
    [classifiedPatterns: <PATH-TO-FILE>]
    [metrics: <LIST-COMMA-SEPARATE-CODES>]
    [files: <REG-EXP>]
    [branch: [refs/.../]<NAME>]
    [revisionFilter:
      type: all|allow|deny|at|from|to|range
      [value: <SHA>|<SHA>..<SHA>]
    ]
    [includeTests: true|false]
    [excludeWorkTree: true|false]
  - <SECOND-PROJECT>
  - ...
```

Each parameter follows the same syntax as those defined by the command-line arguments, with some minor differences:

- `revisionFilter` is an object encompassing `-all`, `-allow`, `-deny`, `-at`, `-from`, `-to`, `-range` options.
- `location` is the equivalent of `-target` option, except that you cannot point to another YAML file.

## Examples

### Analyze a local non-git project (LOCAL_DIR mode)

```sh
java -jar surface.jar -target path/to/project -workDir /home/myself -outFile /home/myself/results.json -files /dao/
```

Run Surface on a locally-stored project, analyzing only the files in the "dao" subdirectory, using `/home/myself` as working directory where the project will be copied before analyzing it, and exporting the results in `/home/myself/results.json`.

### Analyze a local git project (LOCAL_GIT mode)

```sh
java -jar surface.jar -target path/to/project -workDir /home/myself -outFile /home/myself/results.json -from abcd1234 -includeTests
```

Run Surface on a locally-stored git project, including the test files, from commit `abcd1234` (included) to the HEAD revision (included), using `/home/myself` as the working directory where the project will be copied before analyzing it, and exporting the results in `/home/myself/results.json`.

### Analyze a local git project (REMOTE_GIT mode)

```sh
java -jar surface.jar -target https://github.com/org/project -workDir /home/myself -outFile /home/myself/results.json -range abcd1234..5678wxyz -excludeWorkTree
```

Run Surface on a remotely-stored git project, including the test files, from commit `abcd1234` (excluded)to `5678wxyz` (included), using `/home/myself` as the working directory where the project will be cloned before analyzing it, and exporting the results in `/home/myself/results.json`. The local uncommitted change will not be analyzed (`-excludeWorkTree`).

### Analyze multiple projects with YAML file (FLEXIBLE mode)

```sh
java -jar surface.jar -target config.yml -workDir /tmp -outFile /tmp/results.json -all -classifiedPatterns mypatterns.txt
```

Run Surface following the configuration provided in `config.yml`, using `/tmp` as the working directory where the projects will be copied or cloned before analyzing them, and exporting the results in `/tmp/results.json`. If not specified differently in the YAML file, the complete history of all `git` projects will be analyzed (`-all`). If not specified differently in the YAML file, the default set of patterns used to detect the classified attributes is read from `mypatterns.txt`.

An additional example can be found [here](https://github.com/emaiannone/surface-scenario).
