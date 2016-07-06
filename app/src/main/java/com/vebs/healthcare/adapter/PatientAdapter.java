package com.vebs.healthcare.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.vebs.healthcare.R;

import java.util.ArrayList;


public class PatientAdapter extends RecyclerView.Adapter<PatientAdapter.PatientHolder> {

    private Context context;
    //private RealmResults<Appointment> appointmentList;
    private ArrayList<String> pName;
    private int tempPosition = 0, appId;
  //  private Appointment appointment;

    public PatientAdapter(Context context, ArrayList<String> pName) {
        this.context = context;
        this.pName = pName;
    }

    @Override
    public PatientHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PatientHolder(LayoutInflater.from(context).inflate(R.layout.item_patient, parent, false));
    }

    @Override
    public void onBindViewHolder(PatientHolder holder, final int position) {

       // appointment = appointmentList.get(position);
        holder.txtname.setText(pName.get(position));

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
