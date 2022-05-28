package org.surface.surface.core.analysis.selectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.revwalk.RevCommit;
import org.surface.surface.common.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RangeRevisionSelector extends RevisionSelector {
    private static final Logger LOGGER = LogManager.getLogger();

    public RangeRevisionSelector(String revisionString) {
        super(revisionString);
    }

    @Override
    public List<RevCommit> selectRevisions(Git git) throws IOException, GitAPIException {
        if (git == null || getRevisionString() == null) {
            return null;
        }
        List<RevCommit> commits = new ArrayList<>();
        String[] range = Utils.getRevisionsFromRange(getRevisionString());
        ObjectId from = git.getRepository().resolve(range[0]);
        ObjectId to = git.getRepository().resolve(range[1]);
        if (from == null || to == null) {
            throw new IllegalStateException("One of the revisions in the range does not point to an existing snapshot");
        }
        Iterable<RevCommit> commitsIter = git.log().addRange(from, to).call();
        commitsIter.spliterator().forEachRemaining(commits::add);
        return commits;
    }
}
