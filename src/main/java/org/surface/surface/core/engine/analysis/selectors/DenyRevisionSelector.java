package org.surface.surface.core.engine.analysis.selectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.revwalk.RevCommit;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class DenyRevisionSelector extends RevisionSelector {
    public static final String CODE = "DENY";
    private static final Logger LOGGER = LogManager.getLogger();

    public DenyRevisionSelector(String revisionString, String branchString) {
        super(revisionString, branchString);
    }

    @Override
    public List<RevCommit> selectRevisions(Git git) throws IOException, GitAPIException {
        if (git == null || getRevisionString() == null) {
            return null;
        }
        Ref targetBranch = getTargetBranch(git);
        if (targetBranch == null) {
            throw new RuntimeException("Could not find the target branch");
        }
        List<RevCommit> commitsToDiscard = new ArrayList<>();
        File file = Paths.get(getRevisionString()).toFile();
        if (!file.isFile()) {
            throw new IllegalStateException("The file that should contain the list of commits NOT to analyze does not exist");
        }
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                ObjectId commitId = git.getRepository().resolve(line);
                if (commitId == null) {
                    LOGGER.debug("Line \"{}\" is not a valid commit. Ignoring it.", line);
                    continue;
                }
                commitsToDiscard.add(git.getRepository().parseCommit(commitId));
            }
        }
        Iterable<RevCommit> commitsIter;
        if (getRevisionString() == null) {
            commitsIter = git.log().all().call();
        } else {
            commitsIter = git.log().add(targetBranch.getObjectId()).call();
        }
        List<RevCommit> allCommits = new ArrayList<>();
        commitsIter.spliterator().forEachRemaining(allCommits::add);
        Collections.reverse(allCommits);
        return allCommits.stream()
                .filter(c -> !commitsToDiscard.contains(c))
                .collect(Collectors.toList());
    }
}
