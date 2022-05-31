package org.surface.surface.core.analysis.selectors;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.revwalk.RevCommit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RangeRevisionSelector extends RevisionSelector {
    public static final String CODE = "RANGE";

    private final String fromString;
    private final String toString;

    public RangeRevisionSelector(String revisionString) {
        super(revisionString);
        String[] range = getRevisionsFromRange(getRevisionString());
        this.fromString = range[0];
        this.toString = range[1];
    }

    @Override
    public List<RevCommit> selectRevisions(Git git) throws IOException, GitAPIException {
        if (git == null || getRevisionString() == null) {
            return null;
        }
        List<RevCommit> commits = new ArrayList<>();
        ObjectId from = git.getRepository().resolve(fromString);
        ObjectId to = git.getRepository().resolve(toString);
        if (from == null || to == null) {
            throw new IllegalStateException("One of the revisions in the range does not point to an existing snapshot");
        }
        Iterable<RevCommit> commitsIter = git.log().addRange(from, to).call();
        commitsIter.spliterator().forEachRemaining(commits::add);
        return commits;
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
}
