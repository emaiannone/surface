package org.surface.surface.common.filters;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.surface.surface.common.RevisionMode;
import org.surface.surface.common.Utils;

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
                if (Utils.isAlphaNumeric(revision.getValue())) {
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
