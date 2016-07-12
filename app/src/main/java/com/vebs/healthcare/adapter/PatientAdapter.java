package com.vebs.healthcare.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.vebs.healthcare.PatientDetailActivity;
import com.vebs.healthcare.R;

import java.util.ArrayList;


public class PatientAdapter extends RecyclerView.Adapter<PatientAdapter.PatientHolder> {

    private Context context;
    //private RealmResults<Appointment> appointmentList;
    private ArrayList<String> pName;
  //  private Appointment appointment;

    public PatientAdapter(Context context, ArrayList<String> pName) {
        this.context = context;
        this.pName = pName;
       // this.listner=listner;
    }

    @Override
    public PatientHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PatientHolder(LayoutInflater.from(context).inflate(R.layout.item_patient, parent, false));
    }

    @Override
    public void onBindViewHolder(PatientHolder holder, final int position) {

       // appointment = appointmentList.get(position);
        holder.txtname.setText(pName.get(position));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context,"item selected"+pName.get(position),Toast.LENGTH_LONG).show();
                Intent intent=new Intent(context, PatientDetailActivity.class);
                context.startActivity(intent);

               // showPopup();
            }
        });
    }

    private void showPopup() {
        MaterialDialog dialog = new MaterialDialog.Builder(context)
                .customView(R.layout.activity_patient_detail,false)
                .title("Patient Detail")
                .positiveText("OK")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        /*if (saveListener != null) {
                            saveListener.onSave();
                        }*/
                    }
                })

                .show();
        dialog.setCancelable(true);
    }


    @Override
    public int getItemCount() {
        return pName.size();
    }

    public class PatientHolder extends RecyclerView.ViewHolder {

        TextView txtname;

        public PatientHolder(View itemView) {
            super(itemView);
            txtname = (TextView) itemView.findViewById(R.id.txtname);

        }
    }

}
