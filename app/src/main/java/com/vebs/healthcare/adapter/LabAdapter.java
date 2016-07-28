package com.vebs.healthcare.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class LabAdapter extends RecyclerView.Adapter<LabAdapter.DoctorHolder> {

    private Context context;
    //private RealmResults<Doctor> doctorList;
    ArrayList<HashMap<String, Object>> labTestList;
    public LabAdapter(Context context, ArrayList<HashMap<String, Object>> labTestList) {
        this.context = context;
        this.labTestList = labTestList;

    }


    @Override
    public DoctorHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new DoctorHolder(LayoutInflater.from(context).inflate(R.layout.item_lab, parent, false));
    }


    @Override
    public void onBindViewHolder(final DoctorHolder holder, final int position) {

        HashMap<String,Object> labDetail=labTestList.get(position);
        //holder.txtDrName.setText("Doctor Name : "+labDetail.get("name").toString());
        holder.txtDrName.setText("Doctor Name : "+labDetail.get("drname").toString());
        holder.txtEmail.setText("Doctor Email Id : "+labDetail.get("email").toString());
       // holder.txtHospName.setText("Doctor Hospital Name : "+labDetail.get("hosp_name").toString());
        holder.txtMobileNo.setText("Mobile No. : "+labDetail.get("mobile").toString());
        holder.txtLandLineNo.setText("Landline No. : "+labDetail.get("landline").toString());
        holder.txtAddress.setText("Address : "+labDetail.get("address").toString());
        holder.txtTime.setText("Time : "+labDetail.get("time").toString());
        holder.txtFees.setText("Fees : "+labDetail.get("fees").toString());
        holder.txtPatient.setText("No of Patients : "+labDetail.get("offer"));
        holder.txtNote.setText("Note : "+labDetail.get("note"));

    }


    @Override
    public int getItemCount() {
        return labTestList.size();
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
