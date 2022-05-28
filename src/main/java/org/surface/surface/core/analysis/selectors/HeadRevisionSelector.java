package org.surface.surface.core.analysis.selectors;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HeadRevisionSelector extends RevisionSelector {

    public HeadRevisionSelector(String revisionString) {
        super(revisionString);
    }

    @Override
    public List<RevCommit> selectRevisions(Git git) throws IOException {
        if (git == null) {
            return null;
        }
        List<RevCommit> commits = new ArrayList<>();
        ObjectId head = git.getRepository().resolve(Constants.HEAD);
        try (RevWalk walk = new RevWalk(git.getRepository())) {
            RevCommit headCommit = walk.parseCommit(head);
            commits.add(headCommit);
        }
        return commits;
    }
}
