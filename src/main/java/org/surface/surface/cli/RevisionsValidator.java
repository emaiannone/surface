package org.surface.surface.cli;

import org.apache.commons.lang3.StringUtils;

public class RevisionsValidator {

    public String[] getRevisionsFromRange(String revisionRange) {
        String[] parts = revisionRange.split("\\.\\.");
        if (StringUtils.countMatches(revisionRange, '.') != 2 || parts.length != 2) {
            throw new IllegalArgumentException("The revision range must fulfill the expected format (\"<START-SHA>..<END-SHA>\").");
        }
        if (isValidRevision(parts[0]) && isValidRevision(parts[1])) {
            return parts;
        }
        else {
            throw new IllegalArgumentException("The revisions must be alphanumeric strings.");
        }
    }

    public boolean isValidRevision(String revision) {
        String revisionRegex = "[a-zA-Z\\d]+";
        return revision.matches(revisionRegex);
    }

}
