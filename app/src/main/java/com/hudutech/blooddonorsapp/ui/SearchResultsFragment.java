package com.hudutech.blooddonorsapp.ui;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.hudutech.blooddonorsapp.R;
import com.hudutech.blooddonorsapp.adapters.BloodDonorAdapter;
import com.hudutech.blooddonorsapp.adapters.ImageViewPagerAdapter;
import com.hudutech.blooddonorsapp.api.ApiClient;
import com.hudutech.blooddonorsapp.models.BannerImage;
import com.hudutech.blooddonorsapp.models.BloodDonor;
import com.hudutech.blooddonorsapp.models.BloodDonorRequestResponse;
import com.hudutech.blooddonorsapp.models.ImageRequestResponse;
import com.hudutech.blooddonorsapp.util.Utils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchResultsFragment extends Fragment {

    private List<BloodDonor> bloodDonorList;
    private ProgressDialog mProgress;
    private Context mContext;
    private BloodDonorAdapter mAdapter;
    private ApiClient mApiClient;




    public SearchResultsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_search_results, container, false);

        ((MainActivity)getActivity()).mViewPager.setVisibility(View.VISIBLE);
        RecyclerView recyclerView = v.findViewById(R.id.searchResultsRecyclerView);
        bloodDonorList = new ArrayList<>();
        mProgress = new ProgressDialog(getContext());
        mContext = getContext();
        mAdapter = new BloodDonorAdapter(mContext, bloodDonorList);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        recyclerView.setHasFixedSize(true);
        mApiClient = new ApiClient();
        mApiClient.setIsDebug(true);


        mApiClient = new ApiClient();
        mApiClient.setIsDebug(true);




        if (Utils.isConnected(mContext)) {
            loadSearchResults();
           // loadBanners();
        }else {
            Toast.makeText(mContext, "Internet connection not available please enable internet connection and try again later.", Toast.LENGTH_SHORT).show();
        }
        return v;
    }

    private void loadSearchResults() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            mProgress.setMessage("loading please wait...");
            mProgress.setCanceledOnTouchOutside(false);
            mProgress.show();

            String bloodGroup = bundle.getString("bloodGroup");
            String district = bundle.getString("district");
            String municipality = bundle.getString("municipality");

            mApiClient.bloodDonorService().getDonors(bloodGroup, district, municipality)
                    .enqueue(new Callback<BloodDonorRequestResponse>() {
                        @Override
                        public void onResponse(Call<BloodDonorRequestResponse> call, Response<BloodDonorRequestResponse> response) {
                            if (mProgress.isShowing()) mProgress.dismiss();
                            if (response.isSuccessful()) {

                                BloodDonorRequestResponse responseBody = response.body();
                                if (responseBody != null) {
                                    if (responseBody.getStatusCode() == 200) {
                                        bloodDonorList.addAll(responseBody.getBloodDonorList());
                                        mAdapter.notifyDataSetChanged();


                                    } else {
                                        Toast.makeText(mContext, responseBody.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            } else {
                                Toast.makeText(mContext, "Error occurred while submitting request", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<BloodDonorRequestResponse> call, Throwable t) {
                            if (mProgress.isShowing()) mProgress.dismiss();
                            Timber.d("Search donors ",t.getMessage());
                        }
                    });

        } else {
            Toast.makeText(mContext, "Search parameters not set try again later.", Toast.LENGTH_SHORT).show();
        }
    }


}
