package com.hudutech.blooddonorsapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.hudutech.blooddonorsapp.R;
import com.hudutech.blooddonorsapp.models.BannerImage;
import com.hudutech.blooddonorsapp.util.AppContants;

import java.util.List;


public class ImageViewPagerAdapter extends PagerAdapter {
    private Context mContext;
    private List<BannerImage> bannerList;

    public ImageViewPagerAdapter(Context mContext, List<BannerImage> bannerList) {
        this.mContext = mContext;
        this.bannerList = bannerList;
    }

    @Override
    public int getCount() {
        return bannerList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.bottom_banner_layout, container, false);
        ImageView imageView = view.findViewById(R.id.imageView);
        BannerImage banner = bannerList.get(position);
        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.no_barner);
        String bannerUrl = AppContants.BASE_URL + "blood_app_api/api/uploads/" + banner.getFileName();

        Glide.with(mContext)
                .load(bannerUrl)
                .apply(requestOptions)
                .into(imageView);

        container.addView(view);
        return view;

    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.invalidate();
    }
}
