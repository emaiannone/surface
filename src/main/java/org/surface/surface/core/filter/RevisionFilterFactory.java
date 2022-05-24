package org.surface.surface.core.filter;

import org.apache.commons.lang3.tuple.Pair;
import org.surface.surface.core.RevisionMode;

public class RevisionFilterFactory {
    public RevisionFilter getRevisionFilter(Pair<RevisionMode, String> revision) {
        switch (revision.getKey()) {
            case RANGE: {
                String[] range = RevisionsParser.getRevisionsFromRange(revision.getValue());
                return new RangeRevisionFilter(range[0], range[1]);
            }
            case SINGLE: {
                if (RevisionsParser.isValidRevision(revision.getValue())) {
                    return new SingleRevisionFilter(revision.getValue());
                }
                return null;
            }
            case ALL: {
                return new AllRevisionFilter();
            }
            case CURRENT:
            default:
                return null;
        }
    }
}
