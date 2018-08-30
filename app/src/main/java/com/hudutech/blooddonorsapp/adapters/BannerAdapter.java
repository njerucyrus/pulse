package com.hudutech.blooddonorsapp.adapters;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.hudutech.blooddonorsapp.R;
import com.hudutech.blooddonorsapp.api.ApiClient;
import com.hudutech.blooddonorsapp.models.BannerImage;
import com.hudutech.blooddonorsapp.models.ImageResponse;
import com.hudutech.blooddonorsapp.util.AppContants;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class BannerAdapter extends RecyclerView.Adapter<BannerAdapter.ViewHolder> {

    private List<BannerImage> bannerImageList;
    private Context mContext;
    private ApiClient mApiClient;
    private ProgressDialog mProgress;


    public BannerAdapter(Context mContext, List<BannerImage> bannerImageList) {
        this.bannerImageList = bannerImageList;
        this.mContext = mContext;

    }


    @Override
    public BannerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.banner_layout, parent, false);
        return new BannerAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final BannerImage bannerImage = bannerImageList.get(position);

        mApiClient = new ApiClient();
        mApiClient.setIsDebug(true);
        mProgress = new ProgressDialog(mContext);


        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.no_barner);
        String bannerUrl = AppContants.BASE_URL + "blood_app_api/api/uploads/" + bannerImage.getFileName();
        Glide.with(mContext)
                .load(bannerUrl)
                .apply(requestOptions)
                .into(holder.imageView);

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("Are you sure you want to delete?");
                builder.setMessage("the record will be permanently lost");
                builder.setCancelable(false);
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        holder.deleteBanner(bannerImage);
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builder.show();
            }
        });


    }


    @Override
    public int getItemCount() {
        return bannerImageList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private static final String TAG = "ViewHolder";

        ImageView imageView;
        Button deleteButton;
        View mView;

        public ViewHolder(final View itemView) {
            super(itemView);
            mView = itemView;
            imageView = itemView.findViewById(R.id.imageView);
            deleteButton = itemView.findViewById(R.id.buttonDelete);


        }


        public void deleteBanner(final BannerImage bannerImage) {
            mProgress.setMessage("Deleting image please wait");
            mProgress.setCanceledOnTouchOutside(false);
            mProgress.show();
            mApiClient.imageService().deleteImage(bannerImage.getId(), bannerImage.getFileName())
                    .enqueue(new Callback<ImageResponse>() {
                        @Override
                        public void onResponse(Call<ImageResponse> call, Response<ImageResponse> response) {
                            if (mProgress.isShowing()) mProgress.dismiss();
                            if (response.isSuccessful()) {
                                if (response.body().getStatusCode() == 200) {
                                    Toast.makeText(mContext, "Banner Deleted", Toast.LENGTH_SHORT).show();
                                    bannerImageList.remove(bannerImage);
                                    notifyDataSetChanged();
                                } else {
                                    Toast.makeText(mContext, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(mContext, "Unable to delete requested file please try again later.", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ImageResponse> call, Throwable t) {

                            Timber.d("ERROR", t.getMessage());

                            Toast.makeText(mContext, "Internal server error occurred please try again.. ", Toast.LENGTH_SHORT).show();

                        }
                    });
        }


    }
}
