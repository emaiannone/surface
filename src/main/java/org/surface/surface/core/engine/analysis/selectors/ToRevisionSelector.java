package org.surface.surface.core.engine.analysis.selectors;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.revwalk.RevCommit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ToRevisionSelector extends RevisionSelector {
    public static final String CODE = "TO";

    public ToRevisionSelector(String revisionString) {
        super(revisionString);
    }

    @Override
    public List<RevCommit> selectRevisions(Git git) throws IOException, GitAPIException {
        if (git == null || getRevisionString() == null) {
            return null;
        }
        List<RevCommit> commits = new ArrayList<>();
        ObjectId to = git.getRepository().resolve(getRevisionString());
        if (to == null) {
            throw new IllegalStateException("The revision does not point to an existing snapshot");
        }
        Iterable<RevCommit> commitsIter = git.log().add(to).call();
        commitsIter.spliterator().forEachRemaining(commits::add);
        return commits;
    }
}
