package net.android.anko.model.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class RepositoryModel implements Parcelable {

    private int id;
    private String name;
    @SerializedName("full_name") private String fullName;
    @SerializedName("private") private boolean repPrivate;
    @SerializedName("html_url") private String htmlUrl;
    private String description;
    private String language;
    private UserGithubModel owner;

    @SerializedName("default_branch") private String defaultBranch ;

    @SerializedName("created_at") private Date createdAt ;
    @SerializedName("updated_at") private Date updatedAt ;
    @SerializedName("pushed_at") private Date pushedAt ;

    @SerializedName("git_url") private String gitUrl ;
    @SerializedName("ssh_url") private String sshUrl ;
    @SerializedName("clone_url") private String cloneUrl ;
    @SerializedName("svn_url") private String svnUrl ;

    private int size ;
    @SerializedName("stargazers_count") private int stargazersCount ;
    @SerializedName("watchers_count") private int watchersCount ;
    @SerializedName("forks_count") private int forksCount ;
    @SerializedName("open_issues_count") private int openIssuesCount ;
    @SerializedName("subscribers_count") private int subscribersCount ;

    private boolean fork;
    private RepositoryModel parent;
    private RepositoryPermissionsModel permissions;

    @SerializedName("has_issues") private boolean hasIssues;
    @SerializedName("has_projects") private boolean hasProjects;
    @SerializedName("has_downloads") private boolean hasDownloads;
    @SerializedName("has_wiki") private boolean hasWiki;
    @SerializedName("has_pages") private boolean hasPages;

    private int sinceStargazersCount ;
    private String since;

    public RepositoryModel() {
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public boolean isRepPrivate() {
        return repPrivate;
    }

    public void setRepPrivate(boolean repPrivate) {
        this.repPrivate = repPrivate;
    }

    public String getHtmlUrl() {
        return htmlUrl;
    }

    public void setHtmlUrl(String htmlUrl) {
        this.htmlUrl = htmlUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public UserGithubModel getOwner() {
        return owner;
    }

    public void setOwner(UserGithubModel owner) {
        this.owner = owner;
    }

    public String getDefaultBranch() {
        return defaultBranch;
    }

    public void setDefaultBranch(String defaultBranch) {
        this.defaultBranch = defaultBranch;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Date getPushedAt() {
        return pushedAt;
    }

    public void setPushedAt(Date pushedAt) {
        this.pushedAt = pushedAt;
    }

    public String getGitUrl() {
        return gitUrl;
    }

    public void setGitUrl(String gitUrl) {
        this.gitUrl = gitUrl;
    }

    public String getSshUrl() {
        return sshUrl;
    }

    public void setSshUrl(String sshUrl) {
        this.sshUrl = sshUrl;
    }

    public String getCloneUrl() {
        return cloneUrl;
    }

    public void setCloneUrl(String cloneUrl) {
        this.cloneUrl = cloneUrl;
    }

    public String getSvnUrl() {
        return svnUrl;
    }

    public void setSvnUrl(String svnUrl) {
        this.svnUrl = svnUrl;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getStargazersCount() {
        return stargazersCount;
    }

    public void setStargazersCount(int stargazersCount) {
        this.stargazersCount = stargazersCount;
    }

    public int getWatchersCount() {
        return watchersCount;
    }

    public void setWatchersCount(int watchersCount) {
        this.watchersCount = watchersCount;
    }

    public int getForksCount() {
        return forksCount;
    }

    public void setForksCount(int forksCount) {
        this.forksCount = forksCount;
    }

    public int getOpenIssuesCount() {
        return openIssuesCount;
    }

    public void setOpenIssuesCount(int openIssuesCount) {
        this.openIssuesCount = openIssuesCount;
    }

    public boolean isFork() {
        return fork;
    }

    public void setFork(boolean fork) {
        this.fork = fork;
    }

    public RepositoryPermissionsModel getPermissions() {
        return permissions;
    }

    public void setPermissions(RepositoryPermissionsModel permissions) {
        this.permissions = permissions;
    }

    public int getSubscribersCount() {
        return subscribersCount;
    }

    public void setSubscribersCount(int subscribersCount) {
        this.subscribersCount = subscribersCount;
    }

    public RepositoryModel getParent() {
        return parent;
    }

    public void setParent(RepositoryModel parent) {
        this.parent = parent;
    }

    public boolean isHasIssues() {
        return hasIssues;
    }

    public void setHasIssues(boolean hasIssues) {
        this.hasIssues = hasIssues;
    }

    public boolean isHasProjects() {
        return hasProjects;
    }

    public void setHasProjects(boolean hasProjects) {
        this.hasProjects = hasProjects;
    }

    public boolean isHasDownloads() {
        return hasDownloads;
    }

    public void setHasDownloads(boolean hasDownloads) {
        this.hasDownloads = hasDownloads;
    }

    public boolean isHasWiki() {
        return hasWiki;
    }

    public void setHasWiki(boolean hasWiki) {
        this.hasWiki = hasWiki;
    }

    public boolean isHasPages() {
        return hasPages;
    }

    public void setHasPages(boolean hasPages) {
        this.hasPages = hasPages;
    }

    public int getSinceStargazersCount() {
        return sinceStargazersCount;
    }

    public void setSinceStargazersCount(int sinceStargazersCount) {
        this.sinceStargazersCount = sinceStargazersCount;
    }

    public String getSince() {
        return since;
    }

    public void setSince(String since) {
        this.since = since;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeString(this.fullName);
        dest.writeByte(this.repPrivate ? (byte) 1 : (byte) 0);
        dest.writeString(this.htmlUrl);
        dest.writeString(this.description);
        dest.writeString(this.language);
        dest.writeParcelable(this.owner, flags);
        dest.writeString(this.defaultBranch);
        dest.writeLong(this.createdAt != null ? this.createdAt.getTime() : -1);
        dest.writeLong(this.updatedAt != null ? this.updatedAt.getTime() : -1);
        dest.writeLong(this.pushedAt != null ? this.pushedAt.getTime() : -1);
        dest.writeString(this.gitUrl);
        dest.writeString(this.sshUrl);
        dest.writeString(this.cloneUrl);
        dest.writeString(this.svnUrl);
        dest.writeInt(this.size);
        dest.writeInt(this.stargazersCount);
        dest.writeInt(this.watchersCount);
        dest.writeInt(this.forksCount);
        dest.writeInt(this.openIssuesCount);
        dest.writeInt(this.subscribersCount);
        dest.writeByte(this.fork ? (byte) 1 : (byte) 0);
        dest.writeParcelable(this.parent, flags);
        dest.writeParcelable(this.permissions, flags);
        dest.writeByte(this.hasIssues ? (byte) 1 : (byte) 0);
        dest.writeByte(this.hasProjects ? (byte) 1 : (byte) 0);
        dest.writeByte(this.hasDownloads ? (byte) 1 : (byte) 0);
        dest.writeByte(this.hasWiki ? (byte) 1 : (byte) 0);
        dest.writeByte(this.hasPages ? (byte) 1 : (byte) 0);
        dest.writeInt(this.sinceStargazersCount);
        dest.writeString(this.since);

    }


    public static final Creator<RepositoryModel> CREATOR = new Creator<RepositoryModel>() {
        @Override
        public RepositoryModel createFromParcel(Parcel source) {
            return new RepositoryModel();
        }

        @Override
        public RepositoryModel[] newArray(int size) {
            return new RepositoryModel[size];
        }
    };
}
