package org.surface.surface.core.analysis.selectors;

import org.apache.commons.lang3.tuple.Pair;
import org.surface.surface.common.RevisionMode;

public class RevisionSelectorFactory {

    public RevisionSelector selectRevisionSelector(Pair<RevisionMode, String> revision) {
        if (revision.getKey() == null) {
            return null;
        }
        switch (revision.getKey()) {
            case RANGE: {
                return new RangeRevisionSelector(revision.getValue());
            }
            case SINGLE: {
                return new SingleRevisionSelector(revision.getValue());
            }
            case ALL: {
                return new AllRevisionSelector(revision.getValue());
            }
            case HEAD:
            default: {
                return new HeadRevisionSelector(revision.getValue());
            }
        }
    }
}
