package net.android.anko.model.model;

import android.os.Parcel;
import android.os.Parcelable;


public class RepositoryPermissionsModel implements Parcelable {

    private boolean admin;
    private boolean push;
    private boolean pull;

    public RepositoryPermissionsModel() {

    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public boolean isPush() {
        return push;
    }

    public void setPush(boolean push) {
        this.push = push;
    }

    public boolean isPull() {
        return pull;
    }

    public void setPull(boolean pull) {
        this.pull = pull;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.admin ? (byte) 1 : (byte) 0);
        dest.writeByte(this.push ? (byte) 1 : (byte) 0);
        dest.writeByte(this.pull ? (byte) 1 : (byte) 0);
    }

    protected RepositoryPermissionsModel(Parcel in) {
        this.admin = in.readByte() != 0;
        this.push = in.readByte() != 0;
        this.pull = in.readByte() != 0;
    }

    public static final Creator<RepositoryPermissionsModel> CREATOR = new Creator<RepositoryPermissionsModel>() {
        @Override
        public RepositoryPermissionsModel createFromParcel(Parcel source) {
            return new RepositoryPermissionsModel(source);
        }

        @Override
        public RepositoryPermissionsModel[] newArray(int size) {
            return new RepositoryPermissionsModel[size];
        }
    };
}
