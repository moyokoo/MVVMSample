package net.android.anko.model.model;

public class BranchModel {

    private String name;
    private BranchCommitModel commit;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BranchCommitModel getCommit() {
        return commit;
    }

    public void setCommit(BranchCommitModel commit) {
        this.commit = commit;
    }


}