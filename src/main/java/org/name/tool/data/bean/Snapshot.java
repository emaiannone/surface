package org.name.tool.data.bean;

public class Snapshot {
    private final String repositoryURI;
    private final String commitSha;

    public Snapshot(String repositoryURI, String commitSha) {
        this.repositoryURI = repositoryURI;
        this.commitSha = commitSha;
    }

    public String getRepositoryURI() {
        return repositoryURI;
    }

    public String getCommitSha() {
        return commitSha;
    }
}
