package com.hudutech.blooddonorsapp.ui;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.hudutech.blooddonorsapp.R;
import com.hudutech.blooddonorsapp.api.ApiClient;
import com.hudutech.blooddonorsapp.models.BloodDonor;
import com.hudutech.blooddonorsapp.models.BloodDonorRequestResponse;
import com.hudutech.blooddonorsapp.util.Utils;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddressFragment extends Fragment implements View.OnClickListener {

    String[] autoCompleteArr;
    private Spinner districtSpinner;
    private AutoCompleteTextView municipalityAutoComplete;
    private TextInputEditText mPlace;
    private Context mContext;
    private String district = "";
    private ApiClient mApiClient;
    private ProgressDialog mProgress;
    private Dialog confirmDialog;

    private HashMap<String, String[]> dataHashMap = new HashMap<>();

    public AddressFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_address, container, false);

        ((MainActivity)getActivity()).mViewPager.setVisibility(View.GONE);



        mContext = getContext();
        districtSpinner = v.findViewById(R.id.district);
        municipalityAutoComplete = v.findViewById(R.id.municipality);
        mPlace = v.findViewById(R.id.place);

        mApiClient = new ApiClient();
        mApiClient.setIsDebug(true);
        mProgress = new ProgressDialog(getContext());
        confirmDialog = new Dialog(mContext);

        v.findViewById(R.id.buttonSave).setOnClickListener(this);



        districtSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    district = parent.getItemAtPosition(position).toString();
                    String[] arr = Utils.initDataHashMap().get(district.toLowerCase());
                    ArrayAdapter<String> municipalityAdapter = new ArrayAdapter<>(mContext, android.R.layout.simple_dropdown_item_1line, arr);

                    municipalityAutoComplete.setAdapter(municipalityAdapter);
                    municipalityAutoComplete.setThreshold(1);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });




        return v;
    }

    private boolean validateInputs() {
        boolean valid = true;
        if (TextUtils.isEmpty(municipalityAutoComplete.getText().toString().trim())) {
            municipalityAutoComplete.setError("*Required");
            valid = false;
        } else {
            municipalityAutoComplete.setError(null);

        }

        if (TextUtils.isEmpty(mPlace.getText().toString().trim())) {
            mPlace.setError("*Required");
            valid = false;
        } else {
            mPlace.setError(null);
        }

        if (TextUtils.isEmpty(district)) {
            valid = false;
            Toast.makeText(mContext, "Please select a district", Toast.LENGTH_SHORT).show();
        }

        return valid;
    }


    @Override
    public void onClick(View v) {
        if (validateInputs()) {
            //do work here
            if (Utils.isConnected(mContext)) {
                showConfirmDialog(v);
            }else {
                Toast.makeText(mContext, "Internet connection not available please enable internet connection and try again later.", Toast.LENGTH_SHORT).show();
            }

        } else {
            Snackbar.make(v, "Fix the errors above", Snackbar.LENGTH_LONG).show();
        }
    }

    private void submitRecord(BloodDonor bloodDonor) {

        mProgress.setMessage("Submitting please wait...");
        mProgress.setCanceledOnTouchOutside(false);
        mProgress.show();

        mApiClient.bloodDonorService().createDonor(bloodDonor)
                .enqueue(new Callback<BloodDonorRequestResponse>() {
                    @Override
                    public void onResponse(Call<BloodDonorRequestResponse> call, Response<BloodDonorRequestResponse> response) {
                        if (mProgress.isShowing()) mProgress.dismiss();
                        if (response.isSuccessful()) {

                            BloodDonorRequestResponse requestResponse = response.body();
                            if (requestResponse != null) {
                                if (requestResponse.getStatusCode() == 200) {

                                    //we can add our own custom message feedback

                                    mContext.startActivity(new Intent(mContext, MainActivity.class));
                                    if (getActivity() != null) getActivity().finish();


                                    Toast.makeText(mContext, requestResponse.getMessage(), Toast.LENGTH_SHORT).show();

                                } else {
                                    Toast.makeText(mContext, requestResponse.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        } else {
                            Toast.makeText(mContext, "Error while processing your request. please try again later.", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onFailure(Call<BloodDonorRequestResponse> call, Throwable t) {
                        if (mProgress.isShowing()) mProgress.dismiss();

                    }
                });

    }

    public void showConfirmDialog(View view) {
        confirmDialog.setContentView(R.layout.confirm_dialog);
        TextView details = confirmDialog.findViewById(R.id.details);
        Button cancel = confirmDialog.findViewById(R.id.buttonCancel);
        Button buttonOk = confirmDialog.findViewById(R.id.buttonOK);

        Bundle bundle = getArguments();
        if (bundle != null) {
            final BloodDonor bloodDonor = new BloodDonor(
                    0,
                    bundle.getString("personName"),
                    bundle.getString("bloodGroup"),
                    bundle.getString("mobileNumber"),
                    district,
                    municipalityAutoComplete.getText().toString(),
                    mPlace.getText().toString()
            );

            String rawBloodGrp = bloodDonor.getBloodGroup();
            String bloodGroup = "";
            if (rawBloodGrp.equals("A_PLUS")) {
                bloodGroup = "A +ve";
            } else if (rawBloodGrp.equals("B_PLUS")) {
                bloodGroup = "B +ve";
            } else if (rawBloodGrp.equals("O_PLUS")) {
                bloodGroup = "O +ve";
            } else if (rawBloodGrp.equals("AB_PLUS")) {
                bloodGroup = "AB +ve";
            } else if (rawBloodGrp.equals("A_MINUS")) {
                bloodGroup = "A -ve";
            } else if (rawBloodGrp.equals("B_MINUS")) {
                bloodGroup = "B -ve";
            } else if (rawBloodGrp.equals("O_MINUS")) {
                bloodGroup = "0 -ve";
            } else if (rawBloodGrp.equals("AB_MINUS")) {
                bloodGroup = "AB -ve";
            }

            String confirmText = bloodDonor.getPersonName() + "\n" +
                    "" + bloodDonor.getDistrict() + "\n" +
                    "" + bloodDonor.getMunicipality() + "\n" +
                    "" + mPlace.getText().toString().trim() + "\n" +
                    "" + bloodDonor.getMobileNumber() + "\n" +
                    "" + bloodGroup + "\n";


            confirmDialog.setTitle("Confirm Details");
            details.setText(confirmText);

            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    confirmDialog.dismiss();
                }
            });

            buttonOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    submitRecord(bloodDonor);
                }
            });
        }

        confirmDialog.show();

    }
}
