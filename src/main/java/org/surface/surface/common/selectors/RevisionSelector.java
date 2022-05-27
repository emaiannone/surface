package org.surface.surface.common.selectors;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.revwalk.RevCommit;

import java.io.IOException;
import java.util.List;

public abstract class RevisionSelector {

    private final String revisionString;

    RevisionSelector(String revisionString) {
        this.revisionString = revisionString;
    }

    String getRevisionString() {
        return revisionString;
    }

    public abstract List<RevCommit> selectRevisions(Git git) throws IOException, GitAPIException;
}
