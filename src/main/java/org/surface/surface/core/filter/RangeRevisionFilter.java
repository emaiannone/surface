package org.surface.surface.core.filter;

import org.eclipse.jgit.api.Git;

public class RangeRevisionFilter extends RevisionFilter {
    private final String from;
    private final String to;

    public RangeRevisionFilter(String from, String to) {
        this.from = from;
        this.to = to;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }


    @Override
    public boolean filter(Git gitRepo, String revision) {
        //TODO Establish if the commit is between from and to
        return false;
    }
}
