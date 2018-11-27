package net.android.anko.model.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

@Entity(tableName = "gankModel")
public class GankModel implements Parcelable {

    @PrimaryKey
    public String _id;
    @SerializedName("createdAt")
    public String createdAt;
    public String desc;
    @SerializedName("publishedAt")
    public String publishedAt;
    public String source;
    public String type;
    public String url;
    public String imageUrl;
    public String videoUrl;
    public boolean used;
    public String who;
    public List<String> images;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this._id);
        dest.writeString(this.createdAt);
        dest.writeString(this.desc);
        dest.writeString(this.publishedAt);
        dest.writeString(this.source);
        dest.writeString(this.type);
        dest.writeString(this.url);
        dest.writeString(this.imageUrl);
        dest.writeString(this.videoUrl);
        dest.writeByte(this.used ? (byte) 1 : (byte) 0);
        dest.writeString(this.who);
        dest.writeStringList(this.images);
    }

    public GankModel() {
    }

    protected GankModel(Parcel in) {
        this._id = in.readString();
        this.createdAt = in.readString();
        this.desc = in.readString();
        this.publishedAt = in.readString();
        this.source = in.readString();
        this.type = in.readString();
        this.url = in.readString();
        this.imageUrl = in.readString();
        this.videoUrl = in.readString();
        this.used = in.readByte() != 0;
        this.who = in.readString();
        this.images = in.createStringArrayList();
    }

    public static final Parcelable.Creator<GankModel> CREATOR = new Parcelable.Creator<GankModel>() {
        @Override
        public GankModel createFromParcel(Parcel source) {
            return new GankModel(source);
        }

        @Override
        public GankModel[] newArray(int size) {
            return new GankModel[size];
        }
    };
}
