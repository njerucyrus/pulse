package com.hudutech.blooddonorsapp.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ImageRequestResponse {
    @SerializedName("status_code")
    private int statusCode;
    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private List<BannerImage> bannerImages;

    public ImageRequestResponse(int statusCode, String message, List<BannerImage> bannerImages) {
        this.statusCode = statusCode;
        this.message = message;
        this.bannerImages = bannerImages;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getMessage() {
        return message;
    }

    public List<BannerImage> getBannerImages() {
        return bannerImages;
    }
}
