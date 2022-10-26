package org.surface.surface.core.engine.analysis.selectors;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AtRevisionSelector extends RevisionSelector {
    public static final String CODE = "AT";

    public AtRevisionSelector(String revisionString, String branchString) {
        super(revisionString, branchString);
        if (!isAlphaNumeric(revisionString)) {
            throw new IllegalArgumentException("The revision must an alphanumeric string.");
        }
    }

    @Override
    public List<RevCommit> selectRevisions(Git git) throws IOException, GitAPIException {
        if (git == null || getRevisionString() == null) {
            return null;
        }
        List<RevCommit> commits = new ArrayList<>();
        try (RevWalk walk = new RevWalk(git.getRepository())) {
            ObjectId commitId = git.getRepository().resolve(getRevisionString());
            if (commitId == null) {
                throw new RuntimeException("Could not find the revision \"" + getRevisionString() + "\"");
            }
            RevCommit commit = walk.parseCommit(commitId);
            if (getTargetBranch() == null) {
                commits.add(commit);
            } else {
                Ref targetBranch = getTargetBranch(git);
                if (targetBranch == null) {
                    throw new RuntimeException("Could not find the target branch");
                }
                if (!isCommitInBranch(commit, walk, targetBranch)) {
                    throw new RuntimeException("Could not find the revision \"" + commit.getName() + "\" in branch \"" + getTargetBranch() + "\"");
                }
                commits.add(commit);
            }
            return commits;
        }
    }
}
