package org.surface.surface.core.engine.analysis.selectors;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FromRevisionSelector extends RevisionSelector {
    public static final String CODE = "FROM";

    public FromRevisionSelector(String revisionString, String branchString) {
        super(revisionString, branchString);
    }

    @Override
    public List<RevCommit> selectRevisions(Git git) throws IOException, GitAPIException {
        if (git == null || getRevisionString() == null) {
            return null;
        }
        ObjectId from = git.getRepository().resolve(getRevisionString());
        if (from == null) {
            throw new IllegalStateException("The revision does not point to an existing snapshot");
        }
        List<RevCommit> commits = new ArrayList<>();
        Iterable<RevCommit> commitsIter;
        if (getTargetBranch() == null) {
            // To the default HEAD
            commitsIter = git.log().addRange(from, git.getRepository().resolve(Constants.HEAD)).call();
        } else {
            Ref targetBranch = getTargetBranch(git);
            if (targetBranch == null) {
                throw new RuntimeException("Could not find the target branch");
            }
            // To the target branch's HEAD
            try (RevWalk walk = new RevWalk(git.getRepository())) {
                RevCommit fromCommit = walk.parseCommit(from);
                if (!isCommitInBranch(fromCommit, walk, targetBranch)) {
                    throw new RuntimeException("Could not find the revision \"" + fromCommit.getName() + "\" in branch \"" + getTargetBranch() + "\"");
                }
                commitsIter = git.log().addRange(from, targetBranch.getObjectId()).call();
            }
        }
        commitsIter.spliterator().forEachRemaining(commits::add);
        commits.add(git.getRepository().parseCommit(from));
        Collections.reverse(commits);
        return commits;
    }
}
