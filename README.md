# Surface

Surface (Java SecURity FlAws metriCs Extractor) is a command-line tool written in Java that can compute security metrics over the history of Java projects using a fully-automated static code analysis.

Currently, it supports **14 security metrics** from those defined by Alshammari et al. (https://doi.org/10.1109/QSIC.2011.31), here reported:

1. Classified Attributes Total (CAT)
2. Classified Methods Total (CMT)
3. Reflection Package Boolean (RPB)
4. Classified Instance Data Accessibility (CIDA)
5. Classified Class Data Accessibility (CCDA)
6. Classified Operation Accessibility (COA)
7. Classified Methods Weight (CMW)
8. Classified Attributes Interaction Weight (CAIW)
9. Critical Classes Total (CCT)
10. Critical Design Proportion (CDP) 
11. Critical Serialized Classes Proportion (CSCP)
12. Critical Classes Extensibility (CCE)
13. Classified Methods Extensibility (CME) 
14. Critical Superclasses Proportion (CSP)

# How to Build Surface

First, clone this repository into your local working directory. Then, move inside the projects' root directory and run `mvn package` (make sure you have `maven` installed). This command will generate the JAR with all the dependencies inside `target` directory, that can be freely renamed if desired.

# How to Run Surface

The Surface executable JAR can be run using `java -jar` command. The basic syntax is the following:  
`java -jar surface.jar [-all | -allow <arg> | -at <arg> | -deny <arg> | -from <arg> | -range <arg> | -to <arg>] [-excludeWorkTree] [-files <arg>] [-help] [-includeTests] -metrics <arg> -outFile <arg>  -target <arg> [-workDir <arg>]`

Surface accepts the following options:

- `-target <arg>` indicates the target of the analyses. Surface accepts four types of targets:
  - A path to a locally stored non-`git` project (**LOCAL_DIR** mode).
  - A path to a locally stored `git` project (**LOCAL_GIT** mode).
  - A URL to GitHub hosted project (**REMOTE_GIT** mode).
  - A path to a YAML configuration file (**FLEXIBLE** mode).
- `-metrics <arg>` indicates the list of metrics to analyze. `<arg>` is a list of comma-separate strings of metrics codes (see above the list of supported metrics). The special keyword "ALL" enables the execution of all metrics. If one metric code is preceded by the minus symbol (-), then the associated metric will not be computed.
- `-workDir <arg>` indicates the local directory where Surface will copy the locally-stored projects or clone the projects coming from remote. 
- `-outFile <arg>` indicates the file where to export the JSON results. Must have `.json` extension. 
- (Optional) `-files <arg>` selects the subset of files to analyze based on the regular expression `<arg>` applied on the names.
- (Optional) One among `-all`, `-allow`, `-at`, `-deny`, `-from`, `-range`, `-to` to select the revisions (commits) to analyze. If not specified, only the HEAD revision will be analyzed. Not interpreted when the target project has no history (not a `git` project):
  - `-all` analyzes all revisions.
  - `-allow <arg>` analyzes only the revisions appearing in file `<arg>` (one revision per line).
  - `-deny <arg>` analyzes all revision but the ones appearing in file `<arg>` (one revision per line).
  - `-at <arg>` analyzes only the revision `<arg>`.
  - `-from <arg>` analyzes all revisions starting from `<arg>` until HEAD.
  - `-to <arg>` analyzes all revision from the beginning to `<arg>`.
  - `-range <arg>` analyzes the revision range `<arg>`, that must follow the syntax `<from-sha>..<to-sha>`.
- (Optional) `-exceludeWorkTree` disables the analysis of local uncommitted changes for `git` projects.
- (Optional) `-includeTests` allows the analysis of test classes (i.e., those having @Test-like annotations).
- (Optional) `-help` displays the description of all command-line options accepted.

## How to Configure Surface with a YAML File (FLEXIBLE mode)

## Example
