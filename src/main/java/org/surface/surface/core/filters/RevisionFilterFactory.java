package org.surface.surface.core.filters;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.surface.surface.core.RevisionMode;

public class RevisionFilterFactory {
    private static final Logger LOGGER = LogManager.getLogger();

    public RevisionFilter getRevisionFilter(Pair<RevisionMode, String> revision) {
        if (revision == null || revision.getKey() == null || revision.getValue() == null) {
            return null;
        }
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
