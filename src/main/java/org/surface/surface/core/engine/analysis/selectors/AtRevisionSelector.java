package org.surface.surface.core.engine.analysis.selectors;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AtRevisionSelector extends RevisionSelector {
    public static final String CODE = "AT";

    public AtRevisionSelector(String revisionString) {
        super(revisionString);
        if (!isAlphaNumeric(revisionString)) {
            throw new IllegalArgumentException("The revision must an alphanumeric string.");
        }
    }

    @Override
    public List<RevCommit> selectRevisions(Git git) throws IOException {
        if (git == null || getRevisionString() == null) {
            return null;
        }
        List<RevCommit> commits = new ArrayList<>();
        try (RevWalk walk = new RevWalk(git.getRepository())) {
            RevCommit commit = walk.parseCommit(git.getRepository().resolve(getRevisionString()));
            commits.add(commit);
        }
        return commits;
    }
}
