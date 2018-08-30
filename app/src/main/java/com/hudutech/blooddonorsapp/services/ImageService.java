package com.hudutech.blooddonorsapp.services;

import com.hudutech.blooddonorsapp.models.ImageRequestResponse;
import com.hudutech.blooddonorsapp.models.ImageResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ImageService {

    @Multipart
    @POST("blood_app_api/api/image_uploader.php")
    Call<ImageResponse> postImage(@Part MultipartBody.Part image, @Part("name") RequestBody name);

    @GET("blood_app_api/api/api.php/?option=get_images")
    Call<ImageRequestResponse> getImages();

    @GET("blood_app_api/api/api.php/?option=delete_image")
    Call<ImageResponse> deleteImage(@Query("id") int id, @Query("file_name") String fileName);


}
