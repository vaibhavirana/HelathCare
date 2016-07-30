package com.vebs.healthcare.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.vebs.healthcare.R;
import com.vebs.healthcare.utils.Function;
import com.vebs.healthcare.utils.PrefsUtil;
import com.vebs.healthcare.utils.RestClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class PatientLabAdapter extends RecyclerView.Adapter<PatientLabAdapter.PatientHolder> {

    private Context context;
    //private RealmResults<Appointment> appointmentList;
    private ArrayList<String> pName,pId;
    private String str;
    private ArrayList<String> patient_list;
    //  private Appointment appointment;

    public PatientLabAdapter(Context context, ArrayList<String> pName, ArrayList<String> pId) {
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
            final RestClient client = new RestClient(Function.PATIENT_DETAIL_LAB_URL);
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    patient_list = new ArrayList<>();
                    progressDialog[0] = ProgressDialog.show(context, "Fetching Diagnostic Test", "Please wait...", false, false);
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
                                showPopup(jo_test.toString());
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


    private void showPopup(final String str) {
        ((Activity)context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                MaterialDialog dialog = new MaterialDialog.Builder(context)
                /*.customView(R.layout.activity_patient_detail,false)*/
                        .title("Patient Detail")
                        .content(str)
                        .positiveText("OK")
                        .items(R.array.yes_no)
                        .itemsCallbackSingleChoice(1, new MaterialDialog.ListCallbackSingleChoice() {
                            @Override
                            public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                dialog.dismiss();
                                //catWhich = which;
                       /* txtSelectCategory.setText(Function.cat_list.get(which));
                        catId = Function.cat_list_id.get(which);
                        //Log.e("id",Function.cat_list.get(which)+" || "+catId+"||"+which);
                        fetch_doctor(getActivity(), catId);*/
                                //setAdapter();
                                return true;
                            }
                        })
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
