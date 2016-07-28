package com.vebs.healthcare.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.vebs.healthcare.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by sagartahelyani on 06-06-2016.
 */

public class DoctorAdapter extends RecyclerView.Adapter<DoctorAdapter.DoctorHolder> {

    private Context context;
    //private RealmResults<Doctor> doctorList;
    ArrayList<HashMap<String, Object>> doctorList;
    private OnclickListner listner;
    private boolean isSelected=false;
    private int selectedPos=-1;

    public DoctorAdapter(Context context, ArrayList<HashMap<String, Object>> doctorList,OnclickListner listner) {
        this.context = context;
        this.doctorList = doctorList;
        this.listner=listner;
    }


    @Override
    public DoctorHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new DoctorHolder(LayoutInflater.from(context).inflate(R.layout.item_doctor, parent, false));
    }


    @Override
    public void onBindViewHolder(final DoctorHolder holder, final int position) {

        HashMap<String,Object> doctor=doctorList.get(position);
        holder.txtDrName.setText("Doctor Name : "+doctor.get("name").toString());
        holder.txtEmail.setText("Doctor Email Id : "+doctor.get("email").toString());
        holder.txtHospName.setText("Doctor Hospital Name : "+doctor.get("hosp_name").toString());
        holder.txtMobileNo.setText("Mobile No. : "+doctor.get("mobile").toString());
        holder.txtLandLineNo.setText("Landline No. : "+doctor.get("landline").toString());
        holder.txtAddress.setText("Address : "+doctor.get("address").toString());
        holder.txtTime.setText("Time : "+doctor.get("time").toString());
        holder.txtFees.setText("Fees : "+doctor.get("fees").toString());
        holder.txtPatient.setText("No of Patients : "+doctor.get("offer"));
        holder.txtNote.setText("Note : "+doctor.get("note"));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(position==selectedPos)
                {
                    new MaterialDialog.Builder(context)
                            .title(context.getString(R.string.alert_doctor_remove))
                            .positiveText(android.R.string.yes)
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    isSelected=false;
                                    selectedPos=-1;

                                    holder.llDoctor.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.bg_round_rect));
                                    listner.onItemClickListner(selectedPos);
                                }
                            })
                            .negativeText(android.R.string.no)
                            .onNegative(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    dialog.dismiss();
                                }
                            })
                            .show();
                }
                else if(!isSelected && position!=selectedPos) {
                    new MaterialDialog.Builder(context)
                            .title(context.getString(R.string.alert_doctor_selection))
                            .positiveText(android.R.string.yes)
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    holder.llDoctor.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.bg_round_rect_fill));
                                    isSelected=true;
                                    selectedPos=position;

                                    listner.onItemClickListner(selectedPos);

                                }
                            })
                            .negativeText(android.R.string.no)
                            .onNegative(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    dialog.dismiss();
                                }
                            })
                            .show();
                }
                else
                {
                    new MaterialDialog.Builder(context)
                            .title(context.getString(R.string.doctor_already_selected))
                            .positiveText(android.R.string.ok)
                            .show();
                }
                //holder.llDoctor.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.bg_round_rect_fill));
            }
        });
    }


    @Override
    public int getItemCount() {
        return doctorList.size();
    }

    public interface OnclickListner {
        public void onItemClickListner(int position);
    }

    public class DoctorHolder extends RecyclerView.ViewHolder {

        TextView txtDrName, txtEmail,txtHospName,txtMobileNo,txtLandLineNo,txtAddress,txtTime,txtFees,txtPatient,txtNote;
        LinearLayout llDoctor;

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        public DoctorHolder(View itemView) {
            super(itemView);

            llDoctor=(LinearLayout)itemView.findViewById(R.id.llDoctor);
            txtDrName = (TextView) itemView.findViewById(R.id.txtDrName);
            txtHospName = (TextView) itemView.findViewById(R.id.txtHospName);
            txtEmail = (TextView) itemView.findViewById(R.id.txtEmail);
            txtMobileNo = (TextView) itemView.findViewById(R.id.txtMobileNo);
            txtLandLineNo = (TextView) itemView.findViewById(R.id.txtLandLineNo);
            txtAddress = (TextView) itemView.findViewById(R.id.txtAddress);
            txtTime = (TextView) itemView.findViewById(R.id.txtTime);
            txtFees = (TextView) itemView.findViewById(R.id.txtFees);
            txtPatient = (TextView) itemView.findViewById(R.id.txtPatient);
            txtNote = (TextView) itemView.findViewById(R.id.txtNote);
            //txtSpeciality = (TextView) itemView.findViewById(R.id.accountRelation);

        }
    }


}
