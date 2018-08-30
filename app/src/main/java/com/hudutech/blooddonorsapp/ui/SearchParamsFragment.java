package com.hudutech.blooddonorsapp.ui;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;

import com.hudutech.blooddonorsapp.R;
import com.hudutech.blooddonorsapp.util.Utils;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchParamsFragment extends Fragment implements View.OnClickListener {
    String[] autoCompleteArr;
    private Spinner districtSpinner;
    private AutoCompleteTextView municipalityAutoComplete;
    private Context mContext;
    private ProgressDialog mProgress;
    private String district;



    public SearchParamsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_search_params, container, false);
        ((MainActivity)getActivity()).mViewPager.setVisibility(View.GONE);

        mContext = getContext();
        districtSpinner = v.findViewById(R.id.district);
        municipalityAutoComplete = v.findViewById(R.id.municipality);

        mProgress = new ProgressDialog(getContext());

        v.findViewById(R.id.buttonSearch).setOnClickListener(this);

        autoCompleteArr = Utils.getArrayMap();

        districtSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    district = parent.getItemAtPosition(position).toString();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                district = null;
            }
        });

        ArrayAdapter<String> municipalityAdapter = new ArrayAdapter<>(mContext, android.R.layout.simple_dropdown_item_1line, autoCompleteArr);

        municipalityAutoComplete.setAdapter(municipalityAdapter);
        municipalityAutoComplete.setThreshold(1);




        return v;
    }

    @Override
    public void onClick(View v) {
        if (!TextUtils.isEmpty(district)) {
            Bundle bundle = getArguments();
            if (bundle != null) {
                bundle.putString("district",district);
                bundle.putString("municipality", municipalityAutoComplete.getText().toString());
                Fragment fragment = new SearchResultsFragment();
                fragment.setArguments(bundle);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.container, fragment);
                transaction.addToBackStack(null);
                transaction.commit();

            }

        } else {
            Snackbar.make(v, "Please select  district to continue", Snackbar.LENGTH_LONG).show();
        }
    }






}
