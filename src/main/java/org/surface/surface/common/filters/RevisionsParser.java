package org.surface.surface.common.filters;

import org.apache.commons.lang3.StringUtils;
import org.surface.surface.common.Utils;

public class RevisionsParser {

    public static String[] getRevisionsFromRange(String revisionRange) {
        String[] parts = revisionRange.split("\\.\\.");
        if (StringUtils.countMatches(revisionRange, '.') != 2 || parts.length != 2) {
            throw new IllegalArgumentException("The revision range must fulfill the expected format (\"<START-SHA>..<END-SHA>\").");
        }
        if (Utils.isAlphaNumeric(parts[0]) && Utils.isAlphaNumeric(parts[1])) {
            return parts;
        } else {
            throw new IllegalArgumentException("The revisions must be alphanumeric strings.");
        }
    }

}
