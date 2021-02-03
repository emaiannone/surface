package org.name.tool.data.bean;

public class Snapshot {
    private final String projectName;
    private final String repositoryURI;
    private final String commitSha;

    public Snapshot(String projectName, String repositoryURI, String commitSha) {
        this.projectName = projectName;
        this.repositoryURI = repositoryURI;
        this.commitSha = commitSha;
    }

    public String getProjectName() {
        return projectName;
    }

    public String getRepositoryURI() {
        return repositoryURI;
    }

    public String getCommitSha() {
        return commitSha;
    }
}
