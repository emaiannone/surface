package org.surface.surface.core.analysis;

import me.tongfei.progressbar.ConsoleProgressBarConsumer;
import me.tongfei.progressbar.ProgressBar;
import me.tongfei.progressbar.ProgressBarBuilder;
import me.tongfei.progressbar.ProgressBarStyle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ResetCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.revwalk.RevCommit;
import org.surface.surface.common.selectors.RevisionSelector;
import org.surface.surface.core.metrics.results.ProjectMetricsResults;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

public class HistoryAnalyzer {
    private static final Logger LOGGER = LogManager.getLogger();

    private final Path projectDirPath;
    private final String filesRegex;
    private final List<String> metrics;
    private final RevisionSelector revisionSelector;

    public HistoryAnalyzer(Path projectDirPath, String filesRegex, List<String> metrics, RevisionSelector revisionSelector) {
        this.projectDirPath = projectDirPath;
        this.filesRegex = filesRegex;
        this.metrics = metrics;
        this.revisionSelector = revisionSelector;
    }

    public Map<String, ProjectMetricsResults> analyze() throws Exception {
        Map<String, ProjectMetricsResults> allResults = new LinkedHashMap<>();
        List<String> notProcessedCommits = new ArrayList<>();
        try (Git git = Git.open(projectDirPath.toFile())) {
            List<RevCommit> commits;
            try {
                resetHard(git);
            } catch (GitAPIException e) {
                throw new RuntimeException("The reset of git repository " + git.getRepository().getDirectory().getName() + "failed", e);
            }
            try {
                commits = revisionSelector.selectRevisions(git);
            } catch (Exception e) {
                throw new Exception("Failed to fetch the required revisions from git repository " + projectDirPath, e);
            }
            Collections.reverse(commits);
            int numCommits = commits.size();
            LOGGER.info("* Going to analyze {} commits in git repository {}", numCommits, projectDirPath);
            try (ProgressBar progressBar = new ProgressBarBuilder()
                    .setTaskName("History Analysis")
                    .setInitialMax(numCommits)
                    .setStyle(ProgressBarStyle.ASCII)
                    .setMaxRenderedLength(150)
                    .setConsumer(new ConsoleProgressBarConsumer(System.out, 141))
                    //.setConsumer(new DelegatingProgressBarConsumer(LOGGER::info, 141))
                    //.setUpdateIntervalMillis(1)
                    .build()) {
                for (RevCommit commit : commits) {
                    try {
                        git.checkout().setName(commit.getName()).call();
                    } catch (GitAPIException e) {
                        notProcessedCommits.add(commit.getName());
                        continue;
                    }
                    progressBar.setExtraMessage("Inspecting " + commit.getName().substring(0, 8));
                    progressBar.step();
                    ProjectAnalyzer projectAnalyzer = new ProjectAnalyzer(projectDirPath, filesRegex, metrics);
                    ProjectMetricsResults projectMetricsResults = projectAnalyzer.analyze();
                    allResults.put(commit.getName(), projectMetricsResults);
                }
            }
        } catch (IOException e) {
            throw new IOException("Could not open git repository " + projectDirPath, e);
        } finally {
            if (notProcessedCommits.size() > 0) {
                LOGGER.warn("* Failed to process the following commits: " + notProcessedCommits);
            }
        }
        return allResults;
    }

    private void resetHard(Git git) throws GitAPIException {
        git.reset().setMode(ResetCommand.ResetType.HARD).call();
    }
}
