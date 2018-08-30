package com.hudutech.blooddonorsapp.ui;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.hudutech.blooddonorsapp.R;
import com.hudutech.blooddonorsapp.adapters.BannerAdapter;
import com.hudutech.blooddonorsapp.api.ApiClient;
import com.hudutech.blooddonorsapp.models.BannerImage;
import com.hudutech.blooddonorsapp.models.ImageRequestResponse;
import com.hudutech.blooddonorsapp.models.ImageResponse;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class BannerFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "BannerFragment";
    private static final int IMAGE_PICK = 100;
    private Context mContext;
    private ProgressDialog mProgress;
    private ApiClient mApiClient;
    private BannerAdapter mAdapter;
    private List<BannerImage> bannerImageList;


    public BannerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_add_banner, container, false);
        v.findViewById(R.id.buttonChooseImg).setOnClickListener(this);
        mContext = getContext();
        mProgress = new ProgressDialog(getContext());
        mApiClient = new ApiClient();
        mApiClient.setIsDebug(true);
        bannerImageList = new ArrayList<>();
        mAdapter = new BannerAdapter(getContext(), bannerImageList);
        RecyclerView recyclerView = v.findViewById(R.id.banner_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        recyclerView.setHasFixedSize(true);

        ((MainActivity)getActivity()).mViewPager.setVisibility(View.GONE);



       loadBanners();

        return v;

    }

    private void loadBanners() {

        mProgress.setMessage("Loading...");
        mProgress.show();

        mApiClient.imageService().getImages().enqueue(new Callback<ImageRequestResponse>() {
            @Override
            public void onResponse(Call<ImageRequestResponse> call, Response<ImageRequestResponse> response) {
                if (mProgress.isShowing()) mProgress.dismiss();
                if (response.isSuccessful()) {
                    ImageRequestResponse newRes = response.body();
                    if (newRes != null) {
                        bannerImageList.clear();
                        bannerImageList.addAll(newRes.getBannerImages());
                        mAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<ImageRequestResponse> call, Throwable t) {
                if (mProgress.isShowing()) mProgress.dismiss();
                Toast.makeText(mContext, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        final int id = v.getId();
        if (id == R.id.buttonChooseImg) {
            openImageChooser();
        }
    }

    @Override
    public void onActivityResult(final int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == IMAGE_PICK && resultCode == RESULT_OK) {

            android.net.Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            android.database.Cursor cursor = mContext.getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            if (cursor == null)
                return;

            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String filePath = cursor.getString(columnIndex);
            cursor.close();

            File file = new File(filePath);

            RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
            MultipartBody.Part body = MultipartBody.Part.createFormData("fileToUpload", file.getName(), reqFile);
            RequestBody name = RequestBody.create(MediaType.parse("text/plain"), "upload_test");

//            Log.d("THIS", data.getData().getPath());

            mProgress.setMessage("Uploading banner please wait...");
            mProgress.setCanceledOnTouchOutside(false);
            mProgress.show();

            mProgress.show();
            mApiClient.imageService().postImage(body, name).enqueue(new Callback<ImageResponse>() {
                @Override
                public void onResponse(Call<ImageResponse> call, Response<ImageResponse> response) {
                    mProgress.dismiss();

                    if (response.isSuccessful()) {
                        Toast.makeText(mContext, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        loadBanners();
                    } else {
                        Toast.makeText(mContext, "Error occurred while processing your request please try again later", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onFailure(Call<ImageResponse> call, Throwable t) {
                    mProgress.dismiss();
                    Toast.makeText(mContext, "Internal Server Error occurred. please try again", Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

    private void openImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        // intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), IMAGE_PICK);
    }


}
