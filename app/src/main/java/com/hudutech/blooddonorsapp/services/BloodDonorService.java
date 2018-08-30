package com.hudutech.blooddonorsapp.services;

import com.hudutech.blooddonorsapp.models.BloodDonor;
import com.hudutech.blooddonorsapp.models.BloodDonorRequestResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface BloodDonorService {

    @GET("blood_app_api/api/api.php?option=get_donors")
    Call<BloodDonorRequestResponse> getDonors(
            @Query("blood_group") String bloodGroup,
            @Query("district") String district,
            @Query("municipality") String municipality
    );

    @POST("blood_app_api/api/api.php")
    Call<BloodDonorRequestResponse> createDonor(@Body BloodDonor bloodDonor);

    @PUT("blood_app_api/api/api.php")
    Call<BloodDonorRequestResponse> updateDonor(@Body BloodDonor bloodDonor);


    @DELETE("blood_app_api/api/api.php")
    Call<BloodDonorRequestResponse> deleteDonor(@Query("id") int id);
}
