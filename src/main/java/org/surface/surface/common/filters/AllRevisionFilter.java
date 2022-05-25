package org.surface.surface.common.filters;

import org.eclipse.jgit.api.Git;

public class AllRevisionFilter extends RevisionFilter {

    @Override
    public boolean filter(Git gitRepo, String revision) {
        return true;
    }
}
