package org.surface.surface.core.filter;

import org.eclipse.jgit.api.Git;

public class AllRevisionFilter extends RevisionFilter {

    @Override
    public boolean filter(Git gitRepo, String revision) {
        return true;
    }
}
