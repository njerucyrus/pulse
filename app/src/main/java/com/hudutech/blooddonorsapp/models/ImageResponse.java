package com.hudutech.blooddonorsapp.models;

import com.google.gson.annotations.SerializedName;

public class ImageResponse {
    @SerializedName("status_code")
    private int statusCode;
    @SerializedName("message")
    private String message;

    public ImageResponse(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getMessage() {
        return message;
    }
}
