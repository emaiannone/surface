package org.surface.surface.data.bean;

public class Snapshot {
    private final String projectId;
    private final String repositoryURI;
    private final String commitHash;

    public Snapshot(String projectId, String repositoryURI, String commitHash) {
        this.projectId = projectId;
        this.repositoryURI = repositoryURI;
        this.commitHash = commitHash;
    }

    public String getProjectId() {
        return projectId;
    }

    public String getRepositoryURI() {
        return repositoryURI;
    }

    public String getCommitHash() {
        return commitHash;
    }
}
