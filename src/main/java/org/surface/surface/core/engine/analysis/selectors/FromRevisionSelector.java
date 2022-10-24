package org.surface.surface.core.engine.analysis.selectors;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.revwalk.RevCommit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FromRevisionSelector extends RevisionSelector {
    public static final String CODE = "FROM";

    public FromRevisionSelector(String revisionString) {
        super(revisionString);
    }

    @Override
    public List<RevCommit> selectRevisions(Git git) throws IOException, GitAPIException {
        if (git == null || getRevisionString() == null) {
            return null;
        }
        List<RevCommit> commits = new ArrayList<>();
        ObjectId from = git.getRepository().resolve(getRevisionString() + "^");
        if (from == null) {
            throw new IllegalStateException("The revision does not point to an existing snapshot");
        }
        Iterable<RevCommit> commitsIter = git.log().addRange(from, git.getRepository().resolve(Constants.HEAD)).call();
        commitsIter.spliterator().forEachRemaining(commits::add);
        Collections.reverse(commits);
        return commits;
    }
}
