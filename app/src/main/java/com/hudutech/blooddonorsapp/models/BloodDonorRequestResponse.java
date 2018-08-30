package com.hudutech.blooddonorsapp.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BloodDonorRequestResponse {
    @SerializedName("status_code")
    private int statusCode;
    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private List<BloodDonor> bloodDonorList;

    public BloodDonorRequestResponse(int statusCode, String message, List<BloodDonor> bloodDonorList) {
        this.statusCode = statusCode;
        this.message = message;
        this.bloodDonorList = bloodDonorList;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getMessage() {
        return message;
    }

    public List<BloodDonor> getBloodDonorList() {
        return bloodDonorList;
    }
}
