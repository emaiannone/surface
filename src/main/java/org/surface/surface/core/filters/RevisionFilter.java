package org.surface.surface.core.filters;

import org.eclipse.jgit.api.Git;

public abstract class RevisionFilter {
    public abstract boolean filter(Git gitRepo, String revision);
}
