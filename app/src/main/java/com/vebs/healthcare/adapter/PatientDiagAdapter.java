package com.vebs.healthcare.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vebs.healthcare.R;
import com.vebs.healthcare.custom.MDDialog;
import com.vebs.healthcare.utils.Function;
import com.vebs.healthcare.utils.PrefsUtil;
import com.vebs.healthcare.utils.RestClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class PatientDiagAdapter extends RecyclerView.Adapter<PatientDiagAdapter.PatientHolder> {

    private Context context;
    //private RealmResults<Appointment> appointmentList;
    private ArrayList<String> pName,pId;
    private String str;
    private ArrayList<String> patient_list;
    //  private Appointment appointment;

    public PatientDiagAdapter(Context context, ArrayList<String> pName, ArrayList<String> pId) {
        this.context = context;
        this.pName = pName;
        this.pId = pId;
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
        Function.setRegularFont(context,holder.txtname);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context,"item selected"+pName.get(position),Toast.LENGTH_LONG).show();
                //Intent intent=new Intent(context, PatientDetailActivity.class);
                //context.startActivity(intent);
                Log.e("patient selected",pName.get(position) +"||"+pId.get(position) +" || "+ PrefsUtil.getDrID(context));
                Fetch_patient_detail(pId.get(position));
            }
        });
    }

    private void Fetch_patient_detail(final String id) {
        final ProgressDialog[] progressDialog = new ProgressDialog[1];
        if (Function.isConnected(context)) {
            final RestClient client = new RestClient(Function.PATIENT_DETAIL_DIAG_URL);
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    patient_list = new ArrayList<>();
                    progressDialog[0] = ProgressDialog.show(context, "Fetching Patient Detail", "Please wait...", false, false);
                    // progressDialog = ProgressDialog.show(MainActivity.this, "Fetching Data", "Please wait...", false, false);
                }

                @Override
                protected Void doInBackground(Void... params) {
                    try {
                        client.AddParam("id", String.valueOf(id));
                        client.AddParam("user_id", String.valueOf(PrefsUtil.getDrID(context)));
                        client.Execute("get");
                        //Log.e("res")
                        JSONArray ja = new JSONArray(client.getResponse());
                        JSONObject jo_test = null;
                        for (int i = 0; i < ja.length(); i++) {

                            JSONObject object = ja.getJSONObject(i);
                            if (object.has("Patient Detail")) {
                                jo_test = ja.getJSONObject(i).getJSONObject("Patient Detail");
                                showPopup(jo_test);
                                /*if (jo_test != null) {
                                    HashMap<String,String> pDetail=new HashMap<String, String>();
                                    pDetail.put("p_name",object.getString("p_name"));
                                    pDetail.put("Mobile Number",object.getString("Mobile Number"));
                                    pDetail.put("gender",object.getString("gender"));
                                    pDetail.put("Age",object.getString("Age"));
                                    pDetail.put("Date",object.getString("Date"));
                                    pDetail.put("Doctor Name",object.getString("Doctor Name"));
                                    pDetail.put("Center Name",object.getString("Center Name"));
                                    pDetail.put("D Mobile Num",object.getString("D Mobile Num"));
                                    pDetail.put("test",object.getString("test"));
                                    pDetail.put("price",object.getString("price"));
                                    pDetail.put("Offere",object.getString("Offere"));
                                    pDetail.put("refer_note",object.getString("refer_note"));
                                    pDetail.put("time",object.getString("time"));

                                    patient_list.add(jo_test.getString("test"));

                                    Log.e("lab test", diag_test_list.get(i));
                                }*/
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("Webservice 1", e.toString());
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);
                    progressDialog[0].dismiss();
                }
            }.execute();

        }else
        {
            Function.showInternetPopup(context);
        }
    }


    private void showPopup(final JSONObject str) {
        ((Activity)context).runOnUiThread(new Runnable() {
            @Override
            public void run() {

                new MDDialog.Builder(context)
//				        .setContentView(ll)
                        .setContentView(R.layout.activity_patient_detail)
                        .setContentViewOperator(new MDDialog.ContentViewOperator() {
                            @Override
                            public void operate(View contentView) {
                               /* EditText et = (EditText)contentView.findViewById(R.id.edit0);
                                et.setHint("hint set in operator");*/
                                TextView txtPatientName=(TextView)contentView.findViewById(R.id.txtPatientName);
                                TextView txtPatientMobNo=(TextView)contentView.findViewById(R.id.txtPatientMobNo);
                                TextView txtPatientGender=(TextView)contentView.findViewById(R.id.txtPatientGender);
                                TextView txtPatientAge=(TextView)contentView.findViewById(R.id.txtPatientAge);
                                TextView txtDate=(TextView)contentView.findViewById(R.id.txtDate);
                                TextView txtDoctorName=(TextView)contentView.findViewById(R.id.txtDoctorName);
                                TextView txtLabName=(TextView)contentView.findViewById(R.id.txtLabName);
                                TextView txtDoctorMobileNo=(TextView)contentView.findViewById(R.id.txtDoctorMobileNo);
                                TextView txtDoctorFees=(TextView)contentView.findViewById(R.id.txtDoctorFees);
                                TextView txtDoctorTime=(TextView)contentView.findViewById(R.id.txtDoctorTime);
                                TextView txtDoctorOffer=(TextView)contentView.findViewById(R.id.txtDoctorOffer);
                                TextView txtDoctorNote=(TextView)contentView.findViewById(R.id.txtDoctorNote);

                                Function.setRegularFont(context,txtPatientName);
                                Function.setRegularFont(context,txtPatientMobNo);
                                Function.setRegularFont(context,txtPatientGender);
                                Function.setRegularFont(context,txtPatientAge);
                                Function.setRegularFont(context,txtDate);
                                Function.setRegularFont(context,txtDoctorName);
                                Function.setRegularFont(context,txtLabName);
                                Function.setRegularFont(context,txtDoctorMobileNo);
                                Function.setRegularFont(context,txtDoctorFees);
                                Function.setRegularFont(context,txtDoctorTime);
                                Function.setRegularFont(context,txtDoctorOffer);
                                Function.setRegularFont(context,txtDoctorNote);

                                try {
                                    if(str!=null) {
                                        txtPatientName.setText("Patient Name : " + str.getString("p_name").toString());
                                        txtPatientMobNo.setText("Patient Mobile No : " + str.getString("Mobile Number").toString());
                                        txtPatientGender.setText("Patient Gender : " + str.getString("gender").toString());
                                        txtPatientAge.setText("Patient Age : " + str.getString("Age").toString());
                                        txtDate.setText("Date : " + str.getString("Date").toString());
                                        txtDoctorName.setText("Doctor Name : " + str.getString("Doctor Name").toString());
                                        txtLabName.setText("Center Name : " + str.getString("Center Name").toString());
                                        txtDoctorMobileNo.setText("Doctor Mobile No : " + str.getString("D Mobile Num").toString());
                                        txtDoctorFees.setText("test : " + str.getString("test").toString());
                                        txtDoctorOffer.setText("Offere : " + str.getString("Offere").toString());
                                        txtDoctorNote.setText("Notes : " + str.getString("refer_note").toString());
                                        txtDoctorTime.setText("time : " + str.getString("time").toString());
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        })
//                      .setMessages(messages)
                        .setTitle("Patient Detail")
                        .setNegativeButton(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                            }
                        })
                        .setPositiveButton(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        })
                        .setWidthMaxDp(600)
//                      .setShowTitle(false)
                        .setShowButtons(true)
                        .create()
                        .show();

/*
                MaterialDialog dialog = new MaterialDialog.Builder(context)
                .customView(R.layout.activity_patient_detail,false)
                        .title("Patient Detail")

                        *//*.setContentView(R.layout.activity_patient_detail)
                        .setContentViewOperator(new dialog.ContentViewOperator() {
                            @Override
                            public void operate(View contentView) {
                               *//**//* EditText et = (EditText) contentView.findViewById(R.id.edit0);
                                et.setHint("hint set in operator");*//**//*
                            }
                        })*//*
                        .content(str)
                        .positiveText("OK")
                        .items(R.array.yes_no)
                        .show();
                dialog.setCancelable(true);*/
            }
        });

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
