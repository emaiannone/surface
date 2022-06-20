package org.surface.surface.core.engine.analysis.selectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.revwalk.RevCommit;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class AllowRevisionSelector extends RevisionSelector {
    public static final String CODE = "ALLOW";
    private static final Logger LOGGER = LogManager.getLogger();

    public AllowRevisionSelector(String revisionString) {
        super(revisionString);
    }

    @Override
    public List<RevCommit> selectRevisions(Git git) throws IOException {
        if (git == null || getRevisionString() == null) {
            return null;
        }
        List<RevCommit> commits = new ArrayList<>();
        File file = Paths.get(getRevisionString()).toFile();
        if (!file.isFile()) {
            throw new IllegalStateException("The file that should contain the list of commits to analyze does not exist");
        }
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                ObjectId commitId = git.getRepository().resolve(line);
                if (commitId == null) {
                    LOGGER.debug("Line \"{}\" is not a valid commit. Ignoring it.", line);
                    continue;
                }
                commits.add(git.getRepository().parseCommit(commitId));
            }
        }
        return commits;
    }
}