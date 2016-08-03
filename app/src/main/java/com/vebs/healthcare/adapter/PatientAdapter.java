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

import com.afollestad.materialdialogs.MaterialDialog;
import com.vebs.healthcare.R;
import com.vebs.healthcare.custom.MDDialog;
import com.vebs.healthcare.utils.Function;
import com.vebs.healthcare.utils.PrefsUtil;
import com.vebs.healthcare.utils.RestClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class PatientAdapter extends RecyclerView.Adapter<PatientAdapter.PatientHolder> {

    private Context context;
    private ArrayList<String> pName, pId;
    private ArrayList<HashMap<String, String>> patient_list;

    public PatientAdapter(Context context, ArrayList<String> pName, ArrayList<String> pId) {
        this.context = context;
        this.pName = pName;
        this.pId = pId;
    }

    @Override
    public PatientHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PatientHolder(LayoutInflater.from(context).inflate(R.layout.item_patient, parent, false));
    }

    @Override
    public void onBindViewHolder(PatientHolder holder, final int position) {

        // appointment = appointmentList.get(position);
        holder.txtname.setText(pName.get(position));
        Function.setRegularFont(context, holder.txtname);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("patient selected", pName.get(position) + "||" + pId.get(position) + " || " + PrefsUtil.getDrID(context));
                Fetch_patient_detail(pId.get(position));
            }
        });
    }

    private void Fetch_patient_detail(final String id) {
        final ProgressDialog[] progressDialog = new ProgressDialog[1];
        if (Function.isConnected(context)) {
            final RestClient client = new RestClient(Function.PATIENT_DETAIL_DOC_URL);
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
                            if (object.has("Patient Name")) {
                                jo_test = ja.getJSONObject(i).getJSONObject("Patient Name");
                                HashMap<String,String> pDetail=new HashMap<String, String>();
                                pDetail.put("p_name",jo_test.getString("p_name"));
                                pDetail.put("Mobile Number",jo_test.getString("Mobile Number"));
                                pDetail.put("gender",jo_test.getString("gender,"));
                                pDetail.put("Age",jo_test.getString("Age"));
                                pDetail.put("Date",jo_test.getString("Date"));
                                pDetail.put("Doctor Name",jo_test.getString("Doctor Name"));
                                //pDetail.put("Lab Name",jo_test.getString("Lab Name"));
                                pDetail.put("D Mobile Num",jo_test.getString("D Mobile Num"));
                                // pDetail.put("test",jo_test.getString("test"));
                                pDetail.put("Fees",jo_test.getString("Fees"));
                                pDetail.put("Offere",jo_test.getString("Offere"));
                                pDetail.put("Note",jo_test.getString("Note"));
                               // pDetail.put("time",jo_test.getString("time"));

                                patient_list.add(pDetail);
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
                    showPopup();
                }
            }.execute();

        } else {
            Function.showInternetPopup(context);
        }
    }

    private void showPopup() {
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

                                setLayoutDetail(contentView);

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

            }
        });

    }

    private void setLayoutDetail(View contentView) {
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

        txtLabName.setVisibility(View.GONE);
        txtDoctorTime.setVisibility(View.GONE);
        if(patient_list.get(0)!=null) {
            txtPatientName.setText("Patient Name : " + patient_list.get(0).get("p_name").toString());
            txtPatientMobNo.setText("Patient Mobile No : " + patient_list.get(0).get("Mobile Number").toString());
            txtPatientGender.setText("Patient Gender : " + patient_list.get(0).get("gender").toString());
            txtPatientAge.setText("Patient Age : " + patient_list.get(0).get("Age").toString());
            txtDate.setText("Date : " + patient_list.get(0).get("Date").toString());
            txtDoctorName.setText("Doctor Name : " + patient_list.get(0).get("Doctor Name").toString());
           // txtLabName.setText("Lab Name : " +patient_list.get(0).get("Lab Name").toString());
            txtDoctorMobileNo.setText("Doctor Mobile No : " + patient_list.get(0).get("D Mobile Num").toString());
            //txtDoctorFees.setText("Fees : " + patient_list.get(0).get("Fees").toString() + "\nPrice : "+patient_list.get(0).get("price").toString() );
            txtDoctorFees.setText("Fees : " + patient_list.get(0).get("Fees").toString() );
            txtDoctorOffer.setText("Offere : " + patient_list.get(0).get("Offere").toString());
            txtDoctorNote.setText("Notes : " + patient_list.get(0).get("Note").toString());
          //  txtDoctorTime.setText("time : " + patient_list.get(0).get("time").toString());
        }else
        {
            new MaterialDialog.Builder(context)
                    .title(context.getString(R.string.doc_refer_error))
                    .typeface(Function.getRegularFont(context), Function.getRegularFont(context))
                    .positiveText(android.R.string.ok)
                    .show();
        }

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
