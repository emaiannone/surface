package org.surface.surface.core.engine.analysis.selectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HeadRevisionSelector extends RevisionSelector {
    private static final Logger LOGGER = LogManager.getLogger();

    public HeadRevisionSelector(String revisionString, String branchString) {
        super(revisionString, branchString);
    }

    @Override
    public List<RevCommit> selectRevisions(Git git) throws IOException {
        if (git == null) {
            return null;
        }
        List<RevCommit> commits = new ArrayList<>();
        ObjectId head;
        if (getTargetBranch() == null) {
            // HEAD of default branch
            head = git.getRepository().resolve(Constants.HEAD);
        } else {
            Ref targetBranch = getTargetBranch(git);
            if (targetBranch == null) {
                throw new RuntimeException("Could not find the target branch");
            }
            // HEAD of the target branch
            head = getTargetBranch(git).getObjectId();
        }
        System.out.println(head);
        try (RevWalk walk = new RevWalk(git.getRepository())) {
            commits.add(walk.parseCommit(head));
        }
        return commits;
    }
}
