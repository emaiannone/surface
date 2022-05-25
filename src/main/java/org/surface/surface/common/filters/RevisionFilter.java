package org.surface.surface.common.filters;

import org.eclipse.jgit.api.Git;

public abstract class RevisionFilter {
    public abstract boolean filter(Git gitRepo, String revision);
}
