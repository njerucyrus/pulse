package com.hudutech.blooddonorsapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class BloodDonor implements Parcelable{
    @SerializedName("id")
    private int id;
    @SerializedName("person_name")
    private String personName;
    @SerializedName("blood_group")
    private String bloodGroup;
    @SerializedName("mobile_number")
    private String mobileNumber;
    @SerializedName("district")
    private String district;
    @SerializedName("municipality")
    private String municipality;
    @SerializedName("place")
    private String place;

    public BloodDonor(int id, String personName, String bloodGroup, String mobileNumber, String district, String municipality, String place) {
        this.id = id;
        this.personName = personName;
        this.bloodGroup = bloodGroup;
        this.mobileNumber = mobileNumber;
        this.district = district;
        this.municipality = municipality;
        this.place = place;
    }

    public BloodDonor() {
        //empty constructor needed
    }

    protected BloodDonor(Parcel in) {
        id = in.readInt();
        personName = in.readString();
        bloodGroup = in.readString();
        mobileNumber = in.readString();
        district = in.readString();
        municipality = in.readString();
        place = in.readString();
    }

    public static final Creator<BloodDonor> CREATOR = new Creator<BloodDonor>() {
        @Override
        public BloodDonor createFromParcel(Parcel in) {
            return new BloodDonor(in);
        }

        @Override
        public BloodDonor[] newArray(int size) {
            return new BloodDonor[size];
        }
    };

    public int getId() {
        return id;
    }

    public String getPersonName() {
        return personName;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public String getDistrict() {
        return district;
    }

    public String getMunicipality() {
        return municipality;
    }

    public String getPlace() {
        return place;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(personName);
        dest.writeString(bloodGroup);
        dest.writeString(mobileNumber);
        dest.writeString(district);
        dest.writeString(municipality);
        dest.writeString(place);
    }
}
