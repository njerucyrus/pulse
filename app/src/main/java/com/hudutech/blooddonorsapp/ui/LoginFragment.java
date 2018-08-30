package com.hudutech.blooddonorsapp.ui;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.hudutech.blooddonorsapp.R;
import com.hudutech.blooddonorsapp.adapters.ImageViewPagerAdapter;
import com.hudutech.blooddonorsapp.api.ApiClient;
import com.hudutech.blooddonorsapp.models.BannerImage;
import com.hudutech.blooddonorsapp.models.ImageRequestResponse;
import com.hudutech.blooddonorsapp.models.User;
import com.hudutech.blooddonorsapp.models.UserRequestResponse;
import com.hudutech.blooddonorsapp.util.Utils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment implements View.OnClickListener {

private TextInputEditText username;
private TextInputEditText password;
private Context mContext;
private ProgressDialog mProgress;
private ApiClient mApiClient;





    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_login, container, false);

        ((MainActivity)getActivity()).mViewPager.setVisibility(View.GONE);

        mContext = getContext();
        username = v.findViewById(R.id.username);
        password = v.findViewById(R.id.password);

        mApiClient = new ApiClient();
        mApiClient.setIsDebug(true);
        mProgress = new ProgressDialog(getContext());



        mApiClient = new ApiClient();
        mApiClient.setIsDebug(true);


        v.findViewById(R.id.buttonLogin).setOnClickListener(this);
        return v;
    }

    @Override
    public void onClick(View v) {
        if (validateInputs()){
            if (Utils.isConnected(mContext)) {
                login();
            }else {
                Toast.makeText(mContext, "Internet connection not available please enable internet connection and try again later.", Toast.LENGTH_SHORT).show();
            }

        }else{
            Snackbar.make(v, "Fix the errors above", Snackbar.LENGTH_LONG).show();
        }
    }

    private void login() {
        User user = new User(
                username.getText().toString().trim(),
                password.getText().toString().trim()
        );
        mProgress.setMessage("authenticating please wait...");
        mProgress.setCanceledOnTouchOutside(false);
        mProgress.show();
        mApiClient.userService().login(user).enqueue(new Callback<UserRequestResponse>() {
            @Override
            public void onResponse(Call<UserRequestResponse> call, Response<UserRequestResponse> response) {
                if (mProgress.isShowing()) mProgress.dismiss();
                if (response.isSuccessful()) {
                    UserRequestResponse requestResponse = response.body();
                    if (requestResponse != null) {
                        if (requestResponse.getStatusCode() == 200) {
                            Toast.makeText(mContext, requestResponse.getMessage(), Toast.LENGTH_SHORT).show();
                            Utils.setLoggedIn(mContext);
                            getFragmentManager().popBackStack();
                            mContext.startActivity(new Intent(mContext, MainActivity.class));
                            getActivity().finish();
                        } else {
                            Toast.makeText(mContext, requestResponse.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    } else {
                        Toast.makeText(mContext, "Server not found.", Toast.LENGTH_SHORT).show();
                    }

                }else {
                    Toast.makeText(mContext, "Could not reach the server at this time please try again later!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserRequestResponse> call, Throwable t) {
                if (mProgress.isShowing()) mProgress.dismiss();
                Toast.makeText(mContext, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean validateInputs() {
        boolean valid =true;
        if (TextUtils.isEmpty(username.getText().toString())) {
            valid = false;
            username.setError("*Username cannot be empty");
        } else {
            username.setError(null);
        }
        if (TextUtils.isEmpty(password.getText().toString())) {
            valid = false;
            password.setError("*Password cannot be empty");
        } else {
            password.setError(null);
        }
        return valid;
    }


}
