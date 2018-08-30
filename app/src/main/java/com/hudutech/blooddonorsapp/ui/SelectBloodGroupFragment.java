package com.hudutech.blooddonorsapp.ui;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hudutech.blooddonorsapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SelectBloodGroupFragment extends Fragment implements View.OnClickListener {


    public SelectBloodGroupFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_select_blood_group, container, false);
        ((MainActivity)getActivity()).mViewPager.setVisibility(View.VISIBLE);
        v.findViewById(R.id.img_a_plus).setOnClickListener(this);
        v.findViewById(R.id.img_b_plus).setOnClickListener(this);
        v.findViewById(R.id.img_o_plus).setOnClickListener(this);
        v.findViewById(R.id.img_ab_plus).setOnClickListener(this);
        v.findViewById(R.id.img_a_minus).setOnClickListener(this);
        v.findViewById(R.id.img_b_minus).setOnClickListener(this);
        v.findViewById(R.id.img_o_minus).setOnClickListener(this);
        v.findViewById(R.id.img_ab_minus).setOnClickListener(this);



        return v;
    }

    @Override
    public void onClick(View v) {
        final int id = v.getId();
        Bundle bundle = getArguments();
        Fragment fragment = null;
        if (bundle != null) {
            String option = bundle.getString("option");
            if (id == R.id.img_a_plus) {
                if (option.equals("add_new")){
                    fragment = new PersonalDetailsFragment();
                } else if (option.equals("search")) {
                    fragment = new SearchParamsFragment();

                }
                bundle.putString("bloodGroup", "A_PLUS");
                if (fragment !=null) {
                    fragment.setArguments(bundle);
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.container, fragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            } else if (id == R.id.img_b_plus) {
                if (option.equals("add_new")){
                    fragment = new PersonalDetailsFragment();
                } else if (option.equals("search")) {
                    fragment = new SearchParamsFragment();

                }
                bundle.putString("bloodGroup", "B_PLUS");
                if (fragment !=null) {
                    fragment.setArguments(bundle);
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.container, fragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            } else if (id == R.id.img_o_plus) {
                if (option.equals("add_new")){
                    fragment = new PersonalDetailsFragment();
                } else if (option.equals("search")) {
                    fragment = new SearchParamsFragment();

                }
                bundle.putString("bloodGroup", "O_PLUS");
                if (fragment !=null) {
                    fragment.setArguments(bundle);
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.container, fragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            }
            else if (id == R.id.img_ab_plus) {
                if (option.equals("add_new")){
                    fragment = new PersonalDetailsFragment();
                } else if (option.equals("search")) {
                    fragment = new SearchParamsFragment();

                }
                bundle.putString("bloodGroup", "AB_PLUS");
                if (fragment !=null) {
                    fragment.setArguments(bundle);
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.container, fragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            } else if (id == R.id.img_a_minus) {
                if (option.equals("add_new")){
                    fragment = new PersonalDetailsFragment();
                } else if (option.equals("search")) {
                    fragment = new SearchParamsFragment();

                }
                bundle.putString("bloodGroup", "A_MINUS");
                if (fragment !=null) {
                    fragment.setArguments(bundle);
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.container, fragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            } else if (id == R.id.img_b_minus) {
                if (option.equals("add_new")){
                    fragment = new PersonalDetailsFragment();
                } else if (option.equals("search")) {
                    fragment = new SearchParamsFragment();

                }
                bundle.putString("bloodGroup", "B_MINUS");
                if (fragment !=null) {
                    fragment.setArguments(bundle);
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.container, fragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            } else if (id == R.id.img_o_minus) {
                if (option.equals("add_new")){
                    fragment = new PersonalDetailsFragment();
                } else if (option.equals("search")) {
                    fragment = new SearchParamsFragment();

                }
                bundle.putString("bloodGroup", "O_MINUS");
                if (fragment !=null) {
                    fragment.setArguments(bundle);
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.container, fragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            } else if (id == R.id.img_ab_minus) {
                if (option.equals("add_new")){
                    fragment = new PersonalDetailsFragment();
                } else if (option.equals("search")) {
                    fragment = new SearchParamsFragment();

                }
                bundle.putString("bloodGroup", "AB_MINUS");
                if (fragment !=null) {
                    fragment.setArguments(bundle);
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.container, fragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            }
        }
    }



}
