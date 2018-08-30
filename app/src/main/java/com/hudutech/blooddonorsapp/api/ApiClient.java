package com.hudutech.blooddonorsapp.api;

import com.hudutech.blooddonorsapp.services.BloodDonorService;
import com.hudutech.blooddonorsapp.services.ImageService;
import com.hudutech.blooddonorsapp.services.UserService;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.hudutech.blooddonorsapp.util.AppContants.BASE_URL;
import static com.hudutech.blooddonorsapp.util.AppContants.CONNECT_TIMEOUT;
import static com.hudutech.blooddonorsapp.util.AppContants.READ_TIMEOUT;
import static com.hudutech.blooddonorsapp.util.AppContants.WRITE_TIMEOUT;

public class ApiClient {

    private Retrofit retrofit;
    private boolean isDebug;

    private HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();

    /**
     * Set the {@link Retrofit} log level. This allows one to view network traffic.
     *
     * @param isDebug If true, the log level is set to
     *                {@link HttpLoggingInterceptor.Level#BODY}. Otherwise
     *                {@link HttpLoggingInterceptor.Level#NONE}.
     */
    public ApiClient setIsDebug(boolean isDebug) {
        this.isDebug = isDebug;
        return this;
    }


    /**
     * Configure OkHttpClient
     *
     * @return OkHttpClient
     */
    private OkHttpClient.Builder okHttpClient() {
        OkHttpClient.Builder okHttpClient = new OkHttpClient.Builder();
        okHttpClient
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                .addInterceptor(httpLoggingInterceptor);

        return okHttpClient;
    }

    /**
     * Return the current {@link Retrofit} instance. If none exists (first call, API key changed),
     * builds a new one.
     * <p/>
     * When building, sets the endpoint and a {@link HttpLoggingInterceptor} which adds the API key as query param.
     */
    private Retrofit getRestAdapter() {

        Retrofit.Builder builder = new Retrofit.Builder();
        builder.baseUrl(BASE_URL);
        builder.addConverterFactory(GsonConverterFactory.create());

        if (isDebug) {
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        }

        OkHttpClient.Builder okhttpBuilder = okHttpClient();



        builder.client(okhttpBuilder.build());

        retrofit = builder.build();

        return retrofit;
    }

    //Create a service here
    public BloodDonorService bloodDonorService() {
        return getRestAdapter().create(BloodDonorService.class);
    }

    public UserService userService() {
        return getRestAdapter().create(UserService.class);
    }


    public ImageService imageService() {
        return getRestAdapter().create(ImageService.class);
    }
}
