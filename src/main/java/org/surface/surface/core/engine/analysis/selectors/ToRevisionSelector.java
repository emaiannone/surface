package org.surface.surface.core.engine.analysis.selectors;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ToRevisionSelector extends RevisionSelector {
    public static final String CODE = "TO";

    public ToRevisionSelector(String revisionString, String branchString) {
        super(revisionString, branchString);
    }

    @Override
    public List<RevCommit> selectRevisions(Git git) throws IOException, GitAPIException {
        if (git == null || getRevisionString() == null) {
            return null;
        }
        ObjectId to = git.getRepository().resolve(getRevisionString());
        if (to == null) {
            throw new IllegalStateException("The revision does not point to an existing snapshot");
        }
        List<RevCommit> commits = new ArrayList<>();
        // If there is a branch, ensure the commit is in that branch
        if (getTargetBranch() != null) {
            Ref targetBranch = getTargetBranch(git);
            if (targetBranch == null) {
                throw new RuntimeException("Could not find the target branch");
            }
            try (RevWalk walk = new RevWalk(git.getRepository())) {
                RevCommit toCommit = walk.parseCommit(to);
                if (!isCommitInBranch(toCommit, walk, targetBranch)) {
                    throw new RuntimeException("Could not find the revision \"" + toCommit.getName() + "\" in branch \"" + getTargetBranch() + "\"");
                }
            }
        }
        Iterable<RevCommit> commitsIter = git.log().add(to).call();
        commitsIter.spliterator().forEachRemaining(commits::add);
        Collections.reverse(commits);
        return commits;
    }
}
