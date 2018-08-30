package com.hudutech.blooddonorsapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class BannerImage implements Parcelable{

    @SerializedName("id")
    private int id;
    @SerializedName("image_download_url")
    private String fileName;

    public BannerImage(int id, String fileName) {
        this.id = id;
        this.fileName = fileName;
    }

    protected BannerImage(Parcel in) {
        id = in.readInt();
        fileName = in.readString();
    }

    public static final Creator<BannerImage> CREATOR = new Creator<BannerImage>() {
        @Override
        public BannerImage createFromParcel(Parcel in) {
            return new BannerImage(in);
        }

        @Override
        public BannerImage[] newArray(int size) {
            return new BannerImage[size];
        }
    };

    public int getId() {
        return id;
    }

    public String getFileName() {
        return fileName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(fileName);
    }
}
