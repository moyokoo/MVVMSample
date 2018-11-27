package net.android.anko.model.model;

import android.os.Parcel;
import android.os.Parcelable;

public class BranchCommitModel implements Parcelable {
    String sha;
    String url;

    public String getSha() {
        return sha;
    }

    public void setSha(String sha) {
        this.sha = sha;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.sha);
        dest.writeString(this.url);
    }

    public BranchCommitModel() {
    }

    protected BranchCommitModel(Parcel in) {
        this.sha = in.readString();
        this.url = in.readString();
    }

    public static final Parcelable.Creator<BranchCommitModel> CREATOR = new Parcelable.Creator<BranchCommitModel>() {
        @Override
        public BranchCommitModel createFromParcel(Parcel source) {
            return new BranchCommitModel(source);
        }

        @Override
        public BranchCommitModel[] newArray(int size) {
            return new BranchCommitModel[size];
        }
    };
}
