package org.surface.surface.core.analysis.selectors;

import org.apache.commons.lang3.tuple.Pair;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.revwalk.RevCommit;
import org.surface.surface.common.RevisionMode;

import java.io.IOException;
import java.util.List;

public abstract class RevisionSelector {

    private final String revisionString;

    RevisionSelector(String revisionString) {
        this.revisionString = revisionString;
    }

    public static RevisionSelector newRevisionSelector(Pair<RevisionMode, String> revision) {
        return RevisionSelectorFactory.newRevisionSelector(revision);
    }

    String getRevisionString() {
        return revisionString;
    }

    public abstract List<RevCommit> selectRevisions(Git git) throws IOException, GitAPIException;
}
