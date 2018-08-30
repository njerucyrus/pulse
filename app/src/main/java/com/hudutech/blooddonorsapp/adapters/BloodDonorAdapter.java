package com.hudutech.blooddonorsapp.adapters;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hudutech.blooddonorsapp.R;
import com.hudutech.blooddonorsapp.api.ApiClient;
import com.hudutech.blooddonorsapp.models.BloodDonor;
import com.hudutech.blooddonorsapp.models.BloodDonorRequestResponse;
import com.hudutech.blooddonorsapp.util.Utils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BloodDonorAdapter extends RecyclerView.Adapter<BloodDonorAdapter.ViewHolder> {

    private List<BloodDonor> bloodDonorList;
    private Context mContext;
    private Dialog detailDialog;
    private ApiClient mApiClient;
    private ProgressDialog mProgress;


    public BloodDonorAdapter(Context mContext, List<BloodDonor> bloodDonorList) {
        this.bloodDonorList = bloodDonorList;
        this.mContext = mContext;

    }


    @Override
    public BloodDonorAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_blood_donor_layout, parent, false);
        return new BloodDonorAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final BloodDonor bloodDonor = bloodDonorList.get(position);
        holder.tvPersonName.setText(bloodDonor.getPersonName());
        holder.tvDistrict.setText(bloodDonor.getPlace());
        detailDialog = new Dialog(mContext);
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfirmDialog(v, bloodDonor);
            }
        });

        mApiClient = new ApiClient();
        mApiClient.setIsDebug(true);
        mProgress = new ProgressDialog(mContext);

        //DISPLAY THE DELETE BUTTON IF ADMIN OTHERWISE HIDE IT

        if (Utils.isLoggedIn(mContext)) {
            holder.controlLayout.setVisibility(View.VISIBLE);
            holder.mDelete.setVisibility(View.VISIBLE);
        }else if (!Utils.isLoggedIn(mContext)) {
            holder.controlLayout.setVisibility(View.GONE);
            holder.mDelete.setVisibility(View.GONE);

        }


        holder.mDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("Are you sure you want to delete?");
                builder.setMessage("the record will be permanently lost");
                builder.setCancelable(false);
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        holder.deleteDonor(bloodDonor);
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
        return bloodDonorList.size();
    }

    public void showConfirmDialog(View view, final BloodDonor bloodDonor) {
        detailDialog.setContentView(R.layout.details_dialog);
        TextView details = detailDialog.findViewById(R.id.details);
        TextView tvCall = detailDialog.findViewById(R.id.tvCall);


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
                "" + bloodDonor.getPlace() + "\n" +
                "" + bloodDonor.getMobileNumber() + "\n" +
                "" + bloodGroup + "\n";


        detailDialog.setTitle("Details");
        details.setText(confirmText);

        tvCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.makeCall(mContext, bloodDonor.getMobileNumber());
            }
        });


        detailDialog.show();

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private static final String TAG = "ViewHolder";
        TextView tvPersonName;
        TextView tvDistrict;
        TextView tvMore;
        View mView;
        Button mDelete;
        LinearLayout controlLayout;

        public ViewHolder(final View itemView) {
            super(itemView);
            mView = itemView;
            tvPersonName = itemView.findViewById(R.id.tvName);
            tvDistrict = itemView.findViewById(R.id.tvDistrict);
            tvMore = itemView.findViewById(R.id.tvMore);
            mDelete = itemView.findViewById(R.id.buttonDelete);
            controlLayout = itemView.findViewById(R.id.control_layout);

        }

        public void deleteDonor(final BloodDonor bloodDonor) {
            mProgress.setMessage("deleting please wait...");
            mProgress.show();

            mApiClient.bloodDonorService().deleteDonor(bloodDonor.getId()).enqueue(new Callback<BloodDonorRequestResponse>() {
                @Override
                public void onResponse(Call<BloodDonorRequestResponse> call, Response<BloodDonorRequestResponse> response) {
                    if (mProgress.isShowing()) mProgress.dismiss();
                    if (response.isSuccessful()) {
                        if (response.body().getStatusCode() == 204) {
                            Toast.makeText(mContext, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            bloodDonorList.remove(bloodDonor);
                            notifyDataSetChanged();
                        } else {
                            Toast.makeText(mContext, "Error occurred while deleting the record. please try again later", Toast.LENGTH_SHORT).show();

                        }
                    } else {
                        Toast.makeText(mContext, "Unable to process your request at this time. please try again later", Toast.LENGTH_SHORT).show();

                    }
                }

                @Override
                public void onFailure(Call<BloodDonorRequestResponse> call, Throwable t) {
                    if (mProgress.isShowing()) mProgress.dismiss();
                    Toast.makeText(mContext, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

    }
}
