package org.surface.surface.core.engine.analysis.selectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.revwalk.RevCommit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AllRevisionSelector extends RevisionSelector {
    public static final String CODE = "ALL";

    private static final Logger LOGGER = LogManager.getLogger();

    public AllRevisionSelector(String revisionString) {
        super(revisionString);
    }

    @Override
    public List<RevCommit> selectRevisions(Git git) throws IOException, GitAPIException {
        if (git == null) {
            return null;
        }
        List<RevCommit> commits = new ArrayList<>();
        Iterable<RevCommit> commitsIter = git.log().all().call();
        commitsIter.spliterator().forEachRemaining(commits::add);
        return commits;
    }
}
