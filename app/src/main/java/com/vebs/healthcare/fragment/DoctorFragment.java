package com.vebs.healthcare.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.vebs.healthcare.R;
import com.vebs.healthcare.utils.AdvancedSpannableString;
import com.vebs.healthcare.utils.Function;
import com.vebs.healthcare.utils.PrefsUtil;
import com.vebs.healthcare.utils.RestClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class DoctorFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private int catWhich = 0, catId = 0, drWhich = 0;
    private String docId;

    // TODO: Rename and change types of parameters

    private MaterialEditText edtPatientName, edtPatientNo, edtAge, edtRefer;

    private TextView txtDate, txtSelectCategory, txtSelectDoctor;
    private Button btnRefernce, btnEmergency;
    private RadioGroup rgGender;
    /* private EditText inputSearch;
     private RecyclerView rvList;
     private EmptyLayout emptyLayout;
     private DoctorAdapter adapter;*/
    private View layoutDoctorDetail;
    private String selectedGenderId, doctorname;
    private ArrayList<HashMap<String, Object>> doc_list;
    private String date;
    private View view;

    public DoctorFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static DoctorFragment newInstance(String param1, String param2) {
        DoctorFragment fragment = new DoctorFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //  mParam1 = getArguments().getString(ARG_PARAM1);
            // mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_doctor, container, false);
        init();
        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            // Log.e("uid",PrefsUtil.getDrID(getActivity()));
            Function.fetch_category(getActivity());
        }
    }

    private void init() {
        date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());

        txtDate = (TextView) view.findViewById(R.id.txtDate);
        txtDate.setText(date);
        txtSelectCategory = (TextView) view.findViewById(R.id.txtSelectCategory);
        txtSelectDoctor = (TextView) view.findViewById(R.id.txtSelectDoctor);
        edtPatientName = (MaterialEditText) view.findViewById(R.id.edtPatientName);
        edtPatientNo = (MaterialEditText) view.findViewById(R.id.edtPatientNo);
        edtAge = (MaterialEditText) view.findViewById(R.id.edtAge);
        edtRefer = (MaterialEditText) view.findViewById(R.id.edtRefer);
        layoutDoctorDetail = (View) view.findViewById(R.id.layoutDocDetail);

        btnRefernce = (Button) view.findViewById(R.id.btnRefernce);
        btnEmergency = (Button) view.findViewById(R.id.btnEmergency);
        /*rdMale = (RadioButton) view.findViewById(R.id.rdMale);
        rdFemale = (RadioButton) view.findViewById(R.id.rdFemale);*/
        rgGender = (RadioGroup) view.findViewById(R.id.rgGender);
        selectedGenderId = Function.MALE;

        setTypeFace();
        actionListener();

    }

    private void setTypeFace() {
        Function.setRegularFont(getActivity(), txtDate);
        Function.setRegularFont(getActivity(), txtSelectCategory);
        Function.setRegularFont(getActivity(), txtSelectDoctor);
        Function.setRegularFont(getActivity(), ((TextView)view.findViewById(R.id.txtNote)));
        Function.setRegularFont(getActivity(), edtPatientName);
        Function.setRegularFont(getActivity(), edtPatientNo);
        Function.setRegularFont(getActivity(), edtAge);
        Function.setRegularFont(getActivity(), edtRefer);
        Function.setRegularFont(getActivity(), btnRefernce);
        Function.setRegularFont(getActivity(), btnEmergency);
        Function.setRegularFont(getActivity(), ((RadioButton) view.findViewById(R.id.rdFemale)));
        Function.setRegularFont(getActivity(), ((RadioButton) view.findViewById(R.id.rdMale)));


    }

    private void actionListener() {
        txtSelectCategory.setOnClickListener(this);
        txtSelectDoctor.setOnClickListener(this);
        btnRefernce.setOnClickListener(this);
       /* btnEmergency.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                validateData(1);
                return false;
            }
        });*/


        final boolean[] isLongPress = {false};
        final int duration = 3000;
        final Handler someHandler = new Handler();
        final Runnable someCall = new Runnable() {
            @Override
            public void run() {
                if(isLongPress[0]) {
                    validateData(1);
                    //Log.e("long press","long press after 3 sec");
                    // your code goes here
                }
            }
        };

        btnEmergency.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int eventAction = event.getAction();
                if(eventAction == MotionEvent.ACTION_DOWN){
                    isLongPress[0] = true;
                    someHandler.postDelayed(someCall, duration);
                }
                else if (eventAction == MotionEvent.ACTION_UP) {
                    isLongPress[0] = false;
                    someHandler.removeCallbacks(someCall);
                }
                return false;
            }
        });


        rgGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rdMale) {
                    selectedGenderId = Function.MALE;

                } else {
                    selectedGenderId = Function.FEMALE;
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txtSelectCategory:
                if (PrefsUtil.getCity(getActivity()).isEmpty()) {
                    new MaterialDialog.Builder(getActivity())
                            .title(this.getString(R.string.select_city_first))
                            .typeface(Function.getRegularFont(getActivity()), Function.getRegularFont(getActivity()))
                            .positiveText(android.R.string.ok)
                            .show();
                    //showPopup();
                } else {
                    layoutDoctorDetail.setVisibility(View.GONE);
                    txtSelectDoctor.setText(this.getString(R.string.select_doctor));
                    new MaterialDialog.Builder(getActivity())
                            .title(this.getString(R.string.select_category))
                            .typeface(Function.getRegularFont(getActivity()), Function.getRegularFont(getActivity()))
                            .items(Function.cat_list)
                            .itemsCallbackSingleChoice(catWhich, new MaterialDialog.ListCallbackSingleChoice() {
                                @Override
                                public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                    dialog.dismiss();
                                    catWhich = which;
                                    txtSelectCategory.setText(Function.cat_list.get(which));
                                    catId = Function.cat_list_id.get(which);
                                    //Log.e("id",Function.cat_list.get(which)+" || "+catId+"||"+which);
                                    fetch_doctor(getActivity(), catId);
                                    //setAdapter();
                                    return true;
                                }
                            })
                            .positiveText(android.R.string.ok)
                            .show();
                }
                break;

            case R.id.txtSelectDoctor:
                if (catId==0) {
                    new MaterialDialog.Builder(getActivity())
                            .title(this.getString(R.string.select_category_first))
                            .typeface(Function.getRegularFont(getActivity()), Function.getRegularFont(getActivity()))
                            .positiveText(android.R.string.ok)
                            .show();
                } else {
                    if (doc_list.size() == 0) {
                        layoutDoctorDetail.setVisibility(View.GONE);
                        new MaterialDialog.Builder(getActivity())
                                .title(this.getString(R.string.no_doctor))
                                .typeface(Function.getRegularFont(getActivity()), Function.getRegularFont(getActivity()))
                                .positiveText(android.R.string.ok)
                                .show();
                        txtSelectDoctor.setText(getActivity().getString(R.string.no_doctor));

                    } else {
                        final ArrayList<String> listDr = new ArrayList<>();
                        for (int i = 0; i < doc_list.size(); i++) {
                            listDr.add(doc_list.get(i).get("name").toString());
                        }
                        //Log.e("doc list",listDr.toString());
                        new MaterialDialog.Builder(getActivity())
                                .title(this.getString(R.string.select_doctor))
                                .typeface(Function.getRegularFont(getActivity()), Function.getRegularFont(getActivity()))
                                .items(listDr)
                                .itemsCallbackSingleChoice(drWhich, new MaterialDialog.ListCallbackSingleChoice() {
                                    @Override
                                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                        dialog.dismiss();
                                        drWhich = which;
                                        txtSelectDoctor.setText(listDr.get(which));
                                        //docId=drWhich;
                                        setAdapter(drWhich);
                                        return true;
                                    }
                                })
                                .positiveText(android.R.string.ok)
                                .show();
                    }
                }

                break;

            case R.id.btnRefernce:
                validateData(0);
                break;

        }
    }

    private void validateData(int flag) {
        if (edtPatientName.length() == 0) {
            edtPatientName.setError(this.getString(R.string.patient_error));
            edtPatientName.requestFocus();
        } else if (edtPatientNo.length() == 0) {
            edtPatientNo.setError(this.getString(R.string.patient_no_error));
            edtPatientNo.requestFocus();
        } else if (edtAge.length() == 0) {
            edtAge.setError(this.getString(R.string.patient_age_error));
            edtAge.requestFocus();
        } else if (Integer.parseInt(edtAge.getText().toString().trim()) <= 0
                || Integer.parseInt(edtAge.getText().toString().trim()) >= 110) {
            edtAge.setError(this.getString(R.string.patient_proper_age_error));
            edtAge.requestFocus();
        } else if (txtSelectCategory.getText().equals(this.getString(R.string.select_category))) {
            txtSelectCategory.setError(this.getString(R.string.select_category));
            txtSelectCategory.requestFocus();
        } else if (txtSelectDoctor.getText().equals(this.getString(R.string.select_doctor))) {
            txtSelectDoctor.setError(this.getString(R.string.select_doctor));
        }
        /*else if (inputSearch.getText().equals(this.getString(R.string.search_doctor))) {
            inputSearch.setError(this.getString(R.string.search_doctor));
        }*/
        else if (edtRefer.length() == 0) {
            edtRefer.setError(this.getString(R.string.refer_error));
            edtRefer.requestFocus();
        } else {
            // send data call referdoctor
            Log.e("data", PrefsUtil.getCityID(getActivity()) + " || " + edtPatientName.getText() + " || " + edtPatientNo.getText() + " || " +
                    edtAge.getText() + " || " + selectedGenderId + " || " +
                    txtSelectCategory.getText() + " || " +
                    docId + " || " +
                    date + " || " + edtRefer.getText());

            final RestClient client = new RestClient(Function.REFER_DOCTOR_URL);
            client.AddParam("user_id", String.valueOf(PrefsUtil.getDrID(getActivity())));
            client.AddParam("patient_name", edtPatientName.getText().toString());
            client.AddParam("patient_mob_number", edtPatientNo.getText().toString());
            client.AddParam("gender", selectedGenderId);
            client.AddParam("age", edtAge.getText().toString());
            client.AddParam("date", date);
            client.AddParam("city_id", String.valueOf(PrefsUtil.getCityID(getActivity())));
            client.AddParam("category_id", String.valueOf(catId));
            client.AddParam("doctor_name", String.valueOf(docId));
            client.AddParam("is_emergency", String.valueOf(flag));
            client.AddParam("refer_note", edtRefer.getText().toString());
            new AsyncTask<Void, Void, Void>() {
                public ProgressDialog progressDialog;

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    progressDialog = ProgressDialog.show(getActivity(), "Refer Doctor", "Please wait...", false, false);
                }

                @Override
                protected Void doInBackground(Void... params) {
                    try {
                        client.Execute("post");
                        JSONArray ja = new JSONArray(client.getResponse());
                        for (int i = 0; i < ja.length(); i++) {
                            JSONObject object = ja.getJSONObject(i);

                            if (object.has("flag")) {
                                //Log.e("flag",object.getBoolean("flag")+"");
                                showToast(object.getBoolean("flag"));
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        showToast(false);
                        Log.e("Webservice 1", e.toString());
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);
                    progressDialog.dismiss();
                }
            }.execute();
        }


    }

    private void setAdapter(int pos) {
        // fetchDoctorList();
        if (doc_list.size() > 0) {
            // inputSearch.setVisibility(View.GONE);
            layoutDoctorDetail.setVisibility(View.VISIBLE);
            setLabLayout(pos);

        } else {
            layoutDoctorDetail.setVisibility(View.GONE);
        }
    }

    TextView txtHospName, txtDrName, txtEmail, txtMobileNo, txtLandLineNo, txtAddress, txtTime, txtFees, txtPatient, txtNote;

    private void setLabLayout(final int id) {

        txtDrName = (TextView) layoutDoctorDetail.findViewById(R.id.txtDrName);
        txtHospName = (TextView) layoutDoctorDetail.findViewById(R.id.txtHospName);
        txtEmail = (TextView) layoutDoctorDetail.findViewById(R.id.txtEmail);
        txtMobileNo = (TextView) layoutDoctorDetail.findViewById(R.id.txtMobileNo);
        txtLandLineNo = (TextView) layoutDoctorDetail.findViewById(R.id.txtLandLineNo);
        txtAddress = (TextView) layoutDoctorDetail.findViewById(R.id.txtAddress);
        txtTime = (TextView) layoutDoctorDetail.findViewById(R.id.txtTime);
        txtFees = (TextView) layoutDoctorDetail.findViewById(R.id.txtFees);
        txtPatient = (TextView) layoutDoctorDetail.findViewById(R.id.txtPatient);
        txtNote = (TextView) layoutDoctorDetail.findViewById(R.id.txtNote);

        Function.setRegularFont(getActivity(), txtDrName);
        Function.setRegularFont(getActivity(), txtHospName);
        Function.setRegularFont(getActivity(), txtEmail);
        Function.setRegularFont(getActivity(), txtMobileNo);
        Function.setRegularFont(getActivity(), txtLandLineNo);
        Function.setRegularFont(getActivity(), txtAddress);
        Function.setRegularFont(getActivity(), txtTime);
        Function.setRegularFont(getActivity(), txtFees);
        Function.setRegularFont(getActivity(), txtPatient);
        Function.setRegularFont(getActivity(), txtNote);

        HashMap<String, Object> doctor = doc_list.get(id);
        docId = doctor.get("id").toString();
       // txtDrName.setText("Doctor Name : " + doctor.get("name").toString());

        txtDrName.setText(doctor.get("name").toString());
        txtHospName.setText("" + doctor.get("hosp_name").toString());

        AdvancedSpannableString sp = new AdvancedSpannableString("Email : "+ doctor.get("email").toString());
        sp.setColor(getActivity().getResources().getColor(R.color.colorPrimary), "Email : ");
        txtEmail.setText(sp);
        sp = new AdvancedSpannableString("Mobile No. : "+ doctor.get("mobile").toString());
        sp.setColor(getActivity().getResources().getColor(R.color.colorPrimary), "Mobile No. : ");
        txtMobileNo.setText(sp);
        sp = new AdvancedSpannableString("Landline No. : "+ doctor.get("landline").toString());
        sp.setColor(getActivity().getResources().getColor(R.color.colorPrimary), "Landline No. : ");
        txtLandLineNo.setText(sp);
        sp = new AdvancedSpannableString("Address : "+ doctor.get("address").toString());
        sp.setColor(getActivity().getResources().getColor(R.color.colorPrimary), "Address : ");
        txtAddress.setText(sp);
       // txtAddress.setText("Address : " + doctor.get("address").toString());
        sp = new AdvancedSpannableString("Time \n"+ doctor.get("time").toString());
        sp.setColor(getActivity().getResources().getColor(R.color.colorPrimary), "Time \n");
        txtTime.setText(sp);
       // txtTime.setText("Time \n" + doctor.get("time").toString());
        sp = new AdvancedSpannableString("Fees \n"+ doctor.get("fees").toString());
        sp.setColor(getActivity().getResources().getColor(R.color.colorPrimary), "Fees \n");
        txtFees.setText(sp);
       // txtFees.setText("Fees \n" + doctor.get("fees").toString());
        sp = new AdvancedSpannableString("No of Patients \n"+ doctor.get("offer").toString());
        sp.setColor(getActivity().getResources().getColor(R.color.colorPrimary), "No of Patients \n");
        txtPatient.setText(sp);
        //txtPatient.setText("No of Patients \n" + doctor.get("offer"));
        sp = new AdvancedSpannableString("Note : "+ doctor.get("note").toString());
        sp.setColor(getActivity().getResources().getColor(R.color.colorPrimary), "Note : ");
        txtNote.setText(sp);
        //txtNote.setText("Note : " + doctor.get("note"));
    }

    private void showToast(final boolean str) {


        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                /*if (str) {
                    Toast.makeText(getActivity(), "Doctor Refer Successfully", Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(getActivity(), "Some Problem is there, Plaese Try Again", Toast.LENGTH_SHORT).show();*/

                if (str) {
                    new MaterialDialog.Builder(getActivity())
                            .title(getActivity().getString(R.string.doctor_refer_successfully))
                            .typeface(Function.getRegularFont(getActivity()), Function.getRegularFont(getActivity()))
                            .positiveText(android.R.string.ok)
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    resetAll();
                                }
                            })
                            .show();
                } else {
                    new MaterialDialog.Builder(getActivity())
                            .title(getActivity().getString(R.string.doc_refer_error))
                            .typeface(Function.getRegularFont(getActivity()), Function.getRegularFont(getActivity()))
                            .positiveText(android.R.string.ok)
                            .show();
                }
            }
        });

    }

    private void resetAll() {
        edtPatientName.setText("");
        edtPatientNo.setText("");
        edtAge.setText("");
        edtRefer.setText("");
        txtSelectCategory.setText(this.getString(R.string.select_category));
        txtSelectDoctor.setText(this.getString(R.string.select_doctor));
        selectedGenderId = Function.MALE;
        ((RadioButton) view.findViewById(R.id.rdMale)).setChecked(true);
        layoutDoctorDetail.setVisibility(View.GONE);
       /* rvList.setVisibility(View.GONE);
        inputSearch.setVisibility(View.GONE);
        emptyLayout.setVisibility(View.VISIBLE);*/
    }

    public void fetch_doctor(final Context mContext, final int catId) {
        final ProgressDialog[] progressDialog = new ProgressDialog[1];
        if (Function.isConnected(mContext)) {
            final RestClient client = new RestClient(Function.DOCTOR_URL);
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    doc_list = new ArrayList<>();
                    progressDialog[0] = ProgressDialog.show(mContext, "Fetching Doctor", "Please wait...", false, false);
                    // progressDialog = ProgressDialog.show(MainActivity.this, "Fetching Data", "Please wait...", false, false);
                }

                @Override
                protected Void doInBackground(Void... params) {
                    try {
                        Log.e("Req", catId + " ||" + PrefsUtil.getCityID(mContext));
                        client.AddParam("catId", String.valueOf(catId));
                        client.AddParam("cityID", String.valueOf(PrefsUtil.getCityID(mContext)));
                        client.Execute("get");
                        //Log.e("res")
                        if (client.getResponse() != null) {
                            JSONArray ja = new JSONArray(client.getResponse());
                            JSONObject jo_doctor = null;
                            for (int i = 0; i < ja.length(); i++) {

                                JSONObject object = ja.getJSONObject(i);
                                if (object.has("doctors")) {
                                    jo_doctor = ja.getJSONObject(i).getJSONObject("doctors");
                                    if (jo_doctor != null) {
                                        HashMap<String, Object> doc = new HashMap<String, Object>();
                                        doc.put("id", jo_doctor.getInt("id"));
                                        doc.put("name", jo_doctor.getString("Doc Name"));
                                        doc.put("email", jo_doctor.getString("email"));
                                        doc.put("address", jo_doctor.getString("address"));
                                        doc.put("hosp_name", jo_doctor.getString("Hospital name"));
                                        doc.put("mobile", jo_doctor.getString("mobile"));
                                        doc.put("landline", jo_doctor.getString("LDLine Number"));
                                        doc.put("time", jo_doctor.getString("time"));
                                        doc.put("fees", jo_doctor.getString("fees"));
                                        doc.put("offer", jo_doctor.getString("offere"));
                                        doc.put("note", jo_doctor.getString("note"));
                                        doc_list.add(doc);
                                    }
                                }
                            }
                        }
                        Log.e("doctor", doc_list.toString());

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
                    //setAdapter();
                }
            }.execute();

        } else {
            Function.showInternetPopup(mContext);
        }
    }

}
