package org.surface.surface.core.analysis.selectors;

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

    public String getRevisionString() {
        return revisionString;
    }

    public abstract List<RevCommit> selectRevisions(Git git) throws IOException, GitAPIException;

    public static boolean isAlphaNumeric(String string) {
        String revisionRegex = "[a-zA-Z\\d]+";
        return string.matches(revisionRegex);
    }
}
