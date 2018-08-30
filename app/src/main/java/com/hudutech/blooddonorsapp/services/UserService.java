package com.hudutech.blooddonorsapp.services;

import com.hudutech.blooddonorsapp.models.User;
import com.hudutech.blooddonorsapp.models.UserRequestResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserService {
    @POST("blood_app_api/api/users.php?option=login")
    Call<UserRequestResponse> login(@Body User user);
}
