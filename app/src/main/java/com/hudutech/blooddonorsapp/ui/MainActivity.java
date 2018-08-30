package com.hudutech.blooddonorsapp.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.hudutech.blooddonorsapp.R;
import com.hudutech.blooddonorsapp.adapters.ImageViewPagerAdapter;
import com.hudutech.blooddonorsapp.api.ApiClient;
import com.hudutech.blooddonorsapp.models.BannerImage;
import com.hudutech.blooddonorsapp.models.ImageRequestResponse;
import com.hudutech.blooddonorsapp.util.Utils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    LinearLayout layout;
    final int delay = 10000;
    Handler handler = new Handler();
    Runnable runnable;
    private ImageViewPagerAdapter mAdapter;
    public ViewPager mViewPager;
    private List<BannerImage> barnerList;
    private int[] pagerIndex = {-1};
    private ApiClient mApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("\t\t\t\t\t\t\t\tPulse");
        findViewById(R.id.buttonAddNew).setOnClickListener(this);
        findViewById(R.id.buttonSearch).setOnClickListener(this);
        layout = findViewById(R.id.layout_initial);

        barnerList = new ArrayList<>();
        mViewPager = findViewById(R.id.imageViewPager);
        mAdapter = new ImageViewPagerAdapter(this, barnerList);
        mViewPager.setAdapter(mAdapter);
        mApiClient = new ApiClient();
        mApiClient.setIsDebug(true);
        loadBanners();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem login = menu.findItem(R.id.action_login);
        MenuItem logout = menu.findItem(R.id.action_logout);
        MenuItem addbanner = menu.findItem(R.id.action_add_banner);

        if (Utils.isLoggedIn(MainActivity.this)) {
            logout.setVisible(true);
            login.setVisible(false);
            addbanner.setVisible(true);
        } else if (!Utils.isLoggedIn(MainActivity.this)) {
            logout.setVisible(false);
            login.setVisible(true);
            addbanner.setVisible(false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.


        int id = item.getItemId();

        if (id == R.id.action_logout) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Are you sure you want to logout?");
            builder.setMessage("You will be logged out");
            builder.setCancelable(false);
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Utils.setLoggedOut(MainActivity.this);
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                }
            });

            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            builder.show();
        } else if (id == R.id.action_login) {
            layout.setVisibility(View.GONE);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.container, new LoginFragment());
            transaction.addToBackStack(null);
            transaction.commit();

        } else if (id == R.id.action_add_banner) {
            layout.setVisibility(View.GONE);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.container, new BannerFragment());
            transaction.addToBackStack(null);
            transaction.commit();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        final int id = v.getId();

        if (id == R.id.buttonAddNew) {
            layout.setVisibility(View.GONE);
            Fragment fragment = new SelectBloodGroupFragment();
            Bundle bundle = new Bundle();
            bundle.putString("option", "add_new");
            fragment.setArguments(bundle);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.container, fragment);
            transaction.addToBackStack("FIRST_FRAG");
            transaction.commit();


        } else if (id == R.id.buttonSearch) {
            layout.setVisibility(View.GONE);

            Fragment fragment = new SelectBloodGroupFragment();
            Bundle bundle = new Bundle();
            bundle.putString("option", "search");
            fragment.setArguments(bundle);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.container, fragment);
            transaction.addToBackStack("FIRST_FRAG");
            transaction.commit();

        }
    }

    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();
        if (count > 0) {
            getSupportFragmentManager().popBackStack();

            int newCount = getSupportFragmentManager().getBackStackEntryCount();

            if (newCount == 1) {
                getFragmentManager().popBackStack();
                layout.setVisibility(View.VISIBLE);
            } else {
                layout.setVisibility(View.GONE);
            }


        } else {
            super.onBackPressed();
            layout.setVisibility(View.VISIBLE);
        }

    }


    @Override
    protected void onStart() {
        handler.postDelayed(
                new Runnable() {
                    public void run() {
                        pagerIndex[0]++;
                        if (pagerIndex[0] >= mAdapter.getCount()) {
                            pagerIndex[0] = 0;
                        }

                        mViewPager.setCurrentItem(pagerIndex[0]);
                        runnable = this;

                        handler.postDelayed(runnable, delay);
                    }
                }
                , delay);


        super.onStart();
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacks(runnable);
        super.onDestroy();
    }

    private void loadBanners() {


        mApiClient.imageService().getImages().enqueue(new Callback<ImageRequestResponse>() {
            @Override
            public void onResponse(Call<ImageRequestResponse> call, Response<ImageRequestResponse> response) {

                if (response.isSuccessful()) {
                    ImageRequestResponse newRes = response.body();
                    if (newRes != null) {
                        barnerList.clear();
                        barnerList.addAll(newRes.getBannerImages());
                        mAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<ImageRequestResponse> call, Throwable t) {

            }
        });
    }

}
