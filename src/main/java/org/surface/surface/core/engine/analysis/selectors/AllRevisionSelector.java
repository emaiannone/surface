package org.surface.surface.core.engine.analysis.selectors;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.revwalk.RevCommit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AllRevisionSelector extends RevisionSelector {
    public static final String CODE = "ALL";

    public AllRevisionSelector(String revisionString, String branchString) {
        super(revisionString, branchString);
    }

    @Override
    public List<RevCommit> selectRevisions(Git git) throws IOException, GitAPIException {
        if (git == null) {
            return null;
        }
        Ref targetBranch = getTargetBranch(git);
        if (targetBranch == null) {
            throw new RuntimeException("Could not find the target branch");
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
        return allCommits;
    }
}
