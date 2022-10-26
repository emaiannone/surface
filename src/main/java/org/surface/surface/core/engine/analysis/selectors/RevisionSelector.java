package org.surface.surface.core.engine.analysis.selectors;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ListBranchCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.revwalk.RevWalkUtils;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public abstract class RevisionSelector {
    private final String revisionString;
    private final String targetBranch;

    RevisionSelector(String revisionString) {
        this.revisionString = revisionString;
        this.targetBranch = null;
    }

    RevisionSelector(String revisionString, String targetBranch) {
        this.revisionString = revisionString;
        this.targetBranch = targetBranch;
    }

    public static boolean isAlphaNumeric(String string) {
        String revisionRegex = "[a-zA-Z\\d]+";
        return string.matches(revisionRegex);
    }

    public String getRevisionString() {
        return revisionString;
    }

    public String getTargetBranch() {
        return targetBranch;
    }

    Ref getTargetBranch(Git git) {
        List<Ref> branchesMatched;
        try {
            // Full name was provided
            branchesMatched = git.branchList()
                    .setListMode(ListBranchCommand.ListMode.ALL)
                    .setContains(targetBranch)
                    .call();
        } catch (GitAPIException e1) {
            try {
                // Short name: search in local branches
                branchesMatched = git.branchList()
                        .setListMode(ListBranchCommand.ListMode.ALL)
                        .setContains("refs/heads/" + targetBranch)
                        .call();
            } catch (GitAPIException e2) {
                try {
                    // Short name: search in remote local branches
                    branchesMatched = git.branchList()
                            .setListMode(ListBranchCommand.ListMode.ALL)
                            .setContains("refs/remotes/origin/" + targetBranch)
                            .call();
                } catch (GitAPIException e3) {
                    return null;
                }
            }
        }
        return branchesMatched.get(0);
    }

    boolean isCommitInBranch(RevCommit commit, RevWalk walk, Ref branch) throws IOException {
        return RevWalkUtils.findBranchesReachableFrom(commit, walk, Collections.singleton(branch)).size() > 0;
    }

    public abstract List<RevCommit> selectRevisions(Git git) throws IOException, GitAPIException;

    @Override
    public String toString() {
        return revisionString;
    }
}
