package org.surface.surface.common.selectors;

import org.apache.commons.lang3.tuple.Pair;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.surface.surface.common.RevisionMode;
import org.surface.surface.common.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RevisionsSelector {
    public static List<RevCommit> selectRevisions(Git git, Pair<RevisionMode, String> revision) throws IOException, GitAPIException {
        if (revision == null) {
            return null;
        }
        List<RevCommit> commits = new ArrayList<>();
        switch (revision.getKey()) {
            case RANGE: {
                String[] range = Utils.getRevisionsFromRange(revision.getValue());
                ObjectId from = git.getRepository().resolve(range[0]);
                ObjectId to = git.getRepository().resolve(range[1]);
                Iterable<RevCommit> commitsIter = git.log().addRange(from, to).call();
                commitsIter.spliterator().forEachRemaining(commits::add);
                return commits;
            }
            case SINGLE: {
                try (RevWalk walk = new RevWalk(git.getRepository())) {
                    RevCommit commit = walk.parseCommit(git.getRepository().resolve(revision.getValue()));
                    commits.add(commit);
                }
            }
            case ALL: {
                Iterable<RevCommit> commitsIter = git.log().all().call();
                commitsIter.spliterator().forEachRemaining(commits::add);
                return commits;
            }
            case HEAD:
            default: {
                ObjectId head = git.getRepository().resolve(Constants.HEAD);
                try (RevWalk walk = new RevWalk(git.getRepository())) {
                    RevCommit headCommit = walk.parseCommit(head);
                    commits.add(headCommit);
                }
            }
        }
        return commits;
    }
}
