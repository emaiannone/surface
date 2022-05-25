package org.surface.surface.core.filters;

import org.eclipse.jgit.api.Git;

public class SingleRevisionFilter extends RevisionFilter {
    private final String specificRevision;

    public SingleRevisionFilter(String specificRevision) {
        this.specificRevision = specificRevision;
    }

    public String getSpecificRevision() {
        return specificRevision;
    }

    @Override
    public boolean filter(Git gitRepo, String revision) {
        //TODO Establish if the commit is... the specific revision
        return false;
    }
}
