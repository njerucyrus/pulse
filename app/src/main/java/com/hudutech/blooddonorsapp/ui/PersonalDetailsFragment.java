package com.hudutech.blooddonorsapp.ui;


import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.hbb20.CountryCodePicker;
import com.hudutech.blooddonorsapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PersonalDetailsFragment extends Fragment implements View.OnClickListener {


    private TextInputEditText mPersonName;
    private CountryCodePicker ccp;
    private EditText editTextCarrierNumber;
    private String phoneNumber = null;


    public PersonalDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_personal_details, container, false);

        ((MainActivity)getActivity()).mViewPager.setVisibility(View.GONE);

        mPersonName = v.findViewById(R.id.personName);
        ccp = v.findViewById(R.id.ccp);
        editTextCarrierNumber = v.findViewById(R.id.editText_carrierNumber);
        ccp.registerCarrierNumberEditText(editTextCarrierNumber);
        final Button next = v.findViewById(R.id.buttonNext);

        editTextCarrierNumber.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        next.setVisibility(View.GONE);
        ccp.setPhoneNumberValidityChangeListener(new CountryCodePicker.PhoneNumberValidityChangeListener() {
            @Override
            public void onValidityChanged(boolean isValidNumber) {
                // your code
                if (isValidNumber) {
                    Drawable icon = getResources().getDrawable(R.drawable.ic_check_green_24dp);
                    editTextCarrierNumber.setCompoundDrawablesWithIntrinsicBounds(
                            null, null, icon, null);
                    editTextCarrierNumber.setError(null);
                    next.setVisibility(View.VISIBLE);


                    phoneNumber = ccp.getFullNumber();

                } else {
                    next.setVisibility(View.GONE);
                    editTextCarrierNumber.setError("Invalid Mobile Number");
                    editTextCarrierNumber.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                }
            }
        });

        v.findViewById(R.id.buttonNext).setOnClickListener(this);




        return v;

    }

    @Override
    public void onClick(View v) {
        final int id = v.getId();
        if (id == R.id.buttonNext) {
            if (validateInput()) {
                Bundle bundle = getArguments();
                if (bundle != null) {
                    bundle.putString("personName", mPersonName.getText().toString());
                    bundle.putString("mobileNumber", ccp.getFullNumber());
                    Fragment fragment = new AddressFragment();
                    fragment.setArguments(bundle);
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.container, fragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            } else {
                Snackbar.make(v, "Fix the errors above", Snackbar.LENGTH_LONG).show();
            }
        }
    }

    private boolean validateInput() {
        boolean valid = true;
        if (TextUtils.isEmpty(mPersonName.getText().toString())) {
            valid = false;
            mPersonName.setError("*Required");
        } else {
            mPersonName.setError(null);
        }

        if (TextUtils.isEmpty(phoneNumber)) {
            valid = false;
            editTextCarrierNumber.setError("*Required");
        } else {
            editTextCarrierNumber.setError(null);
        }

        return valid;
    }



}
