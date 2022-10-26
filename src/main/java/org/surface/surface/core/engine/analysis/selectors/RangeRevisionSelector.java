package org.surface.surface.core.engine.analysis.selectors;

import org.apache.commons.lang3.StringUtils;
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

public class RangeRevisionSelector extends RevisionSelector {
    public static final String CODE = "RANGE";

    private final String fromString;
    private final String toString;

    public RangeRevisionSelector(String revisionString, String branchString) {
        super(revisionString, branchString);
        String[] range = getRevisionsFromRange(getRevisionString());
        this.fromString = range[0];
        this.toString = range[1];
    }

    private static String[] getRevisionsFromRange(String revisionRange) {
        String[] parts = revisionRange.split("\\.\\.");
        if (StringUtils.countMatches(revisionRange, '.') != 2 || parts.length != 2) {
            throw new IllegalArgumentException("The revision range must fulfill the format (\"<START-SHA>..<END-SHA>\").");
        }
        if (!isAlphaNumeric(parts[0]) || !isAlphaNumeric(parts[1])) {
            throw new IllegalArgumentException("Both the revisions in the range must be alphanumeric strings.");
        }
        return parts;
    }

    @Override
    public List<RevCommit> selectRevisions(Git git) throws IOException, GitAPIException {
        if (git == null || getRevisionString() == null) {
            return null;
        }
        ObjectId from = git.getRepository().resolve(fromString);
        ObjectId to = git.getRepository().resolve(toString);
        if (from == null || to == null) {
            throw new IllegalStateException("One of the revisions in the range does not point to an existing snapshot");
        }
        List<RevCommit> commits = new ArrayList<>();
        // If there is a branch, ensure the two commits are in that branch
        if (getTargetBranch() != null) {
            Ref targetBranch = getTargetBranch(git);
            if (targetBranch == null) {
                throw new RuntimeException("Could not find the target branch");
            }
            try (RevWalk walk = new RevWalk(git.getRepository())) {
                RevCommit fromCommit = walk.parseCommit(from);
                if (!isCommitInBranch(fromCommit, walk, targetBranch)) {
                    throw new RuntimeException("Could not find the revision \"" + fromCommit.getName() + "\" in branch \"" + getTargetBranch() + "\"");
                }
                RevCommit toCommit = walk.parseCommit(to);
                if (!isCommitInBranch(toCommit, walk, targetBranch)) {
                    throw new RuntimeException("Could not find the revision \"" + toCommit.getName() + "\" in branch \"" + getTargetBranch() + "\"");
                }
            }
        }
        Iterable<RevCommit> commitsIter = git.log().addRange(from, to).call();
        commitsIter.spliterator().forEachRemaining(commits::add);
        commits.add(git.getRepository().parseCommit(from));
        Collections.reverse(commits);
        return commits;
    }
}
