package com.vebs.healthcare.fragment;

import android.app.ProgressDialog;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.vebs.healthcare.MainActivity;
import com.vebs.healthcare.R;
import com.vebs.healthcare.utils.Function;
import com.vebs.healthcare.utils.PrefsUtil;
import com.vebs.healthcare.utils.RestClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DoctorFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private int catWhich = 0, catId = 0, docWhich = 0, docId = 0;

    // TODO: Rename and change types of parameters

    private MaterialEditText edtPatientName, edtPatientNo, edtAge, edtRefer;
    private TextView txtDate, txtSelectCategory, txtSelectDoctor;
    private Button btnRefernce, btnEmergency;
    private RadioGroup rgGender;
    private String selectedGenderId, doctorname;

    public DoctorFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DoctorFragment.
     */
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
        View view = inflater.inflate(R.layout.fragment_doctor, container, false);
        init(view);
        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            Function.fetch_category(getActivity());
            /*new CountDownTimer(2000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {

                }

                @Override
                public void onFinish() {

                   // Toast.makeText(getActivity(), "call_doctor", Toast.LENGTH_SHORT).show();
                    Function.fetch_category(getActivity());
                    //callApi();
                }
            }.start();*/
        }
    }

    private void init(View view) {
        txtDate = (TextView) view.findViewById(R.id.txtDate);
        txtDate.setText(new SimpleDateFormat("EE, MM-dd-yyyy").format(new Date()));

        txtSelectCategory = (TextView) view.findViewById(R.id.txtSelectCategory);
        txtSelectDoctor = (TextView) view.findViewById(R.id.txtSelectDoctor);
        edtPatientName = (MaterialEditText) view.findViewById(R.id.edtPatientName);
        edtPatientNo = (MaterialEditText) view.findViewById(R.id.edtPatientNo);
        edtAge = (MaterialEditText) view.findViewById(R.id.edtAge);
        edtRefer = (MaterialEditText) view.findViewById(R.id.edtRefer);

        btnRefernce = (Button) view.findViewById(R.id.btnRefernce);
        btnEmergency = (Button) view.findViewById(R.id.btnEmergency);
        /*rdMale = (RadioButton) view.findViewById(R.id.rdMale);
        rdFemale = (RadioButton) view.findViewById(R.id.rdFemale);*/
        rgGender = (RadioGroup) view.findViewById(R.id.rgGender);
        selectedGenderId = "MALE";


        actionListener();

    }

    private void actionListener() {
        txtSelectCategory.setOnClickListener(this);
        txtSelectDoctor.setOnClickListener(this);
        btnRefernce.setOnClickListener(this);
        btnEmergency.setOnClickListener(this);

        rgGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rdMale) {
                    selectedGenderId = "MALE";

                } else {
                    selectedGenderId = "FEMALE";
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txtSelectCategory:
                txtSelectDoctor.setText(this.getString(R.string.select_doctor));
                new MaterialDialog.Builder(getActivity())
                        .title(this.getString(R.string.select_category))
                        .items(Function.cat_list)
                        .itemsCallbackSingleChoice(catWhich, new MaterialDialog.ListCallbackSingleChoice() {
                            @Override
                            public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                dialog.dismiss();
                                catWhich = which;
                                txtSelectCategory.setText(Function.cat_list.get(which));
                                catId = Function.cat_list_id.get(which);
                                //Log.e("id",Function.cat_list.get(which)+" || "+catId+"||"+which);
                                Function.fetch_doctor(getActivity(), catId);

                                return true;
                            }
                        })
                        .positiveText(android.R.string.ok)
                        .show();
                break;

            case R.id.txtSelectDoctor:
                if (Function.doc_list.size() == 0) {

                    new MaterialDialog.Builder(getActivity())
                            .title(this.getString(R.string.no_doctor))
                            .positiveText(android.R.string.ok)
                            .show();
                    //txtSelectDoctor.setText(getActivity().getString(R.string.no_doctor));

                }else {
                    new MaterialDialog.Builder(getActivity())
                            .title(this.getString(R.string.select_doctor))
                            .items(Function.doc_list)
                            .itemsCallbackSingleChoice(docWhich, new MaterialDialog.ListCallbackSingleChoice() {
                                @Override
                                public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                    dialog.dismiss();
                                    docWhich = which;
                                    txtSelectDoctor.setText(Function.doc_list.get(which));
                                    docId = Function.doc_list_id.get(which);
                                    doctorname = Function.doc_list.get(which);
                                    return true;
                                }
                            })
                            .positiveText(android.R.string.ok)
                            .show();
                }

                break;

            case R.id.btnRefernce:
                validateData(0);
                break;

            case R.id.btnEmergency:
                validateData(1);
                break;
        }
    }

    private void validateData(int flag) {
        if (edtPatientName.length() == 0) {
            edtPatientName.setError(this.getString(R.string.patient_error));
        } else if (edtPatientNo.length() == 0) {
            edtPatientNo.setError(this.getString(R.string.patient_no_error));
        } else if (edtAge.length() == 0) {
            edtAge.setError(this.getString(R.string.patient_age_error));
        } else if (Integer.parseInt(edtAge.getText().toString().trim()) <= 0
                || Integer.parseInt(edtAge.getText().toString().trim()) >= 110) {
            edtAge.setError(this.getString(R.string.patient_proper_age_error));
        } else if (txtSelectCategory.getText().equals(this.getString(R.string.select_category))) {
            txtSelectCategory.setError(this.getString(R.string.select_category));
        } else if (txtSelectDoctor.getText().equals(this.getString(R.string.select_doctor))) {
            txtSelectDoctor.setError(this.getString(R.string.select_doctor));
        } else if (edtRefer.length() == 0) {
            edtRefer.setError(this.getString(R.string.refer_error));
        } else {
            // send data call referdoctor
            Log.e("data", edtPatientName.getText() + " || " + edtPatientNo.getText() + " || " +
                    edtAge.getText() + " || " + selectedGenderId + " || " + txtSelectDoctor.getText() + " || " +
                    txtSelectCategory.getText() + " || " +
                    doctorname + " || " +
                    txtDate.getText() + " || " + edtRefer.getText());

            final RestClient client = new RestClient(Function.REFER_DOCTOR_URL);
            client.AddParam("user_id", String.valueOf(3));
            client.AddParam("patient_name", edtPatientName.getText().toString());
            client.AddParam("patient_mob_number", edtPatientNo.getText().toString());
            client.AddParam("gender", selectedGenderId);
            client.AddParam("age", edtAge.getText().toString());
            client.AddParam("date", txtDate.getText().toString());
            client.AddParam("city_id", String.valueOf(PrefsUtil.getCity(getActivity())));
            client.AddParam("category_id", String.valueOf(catId));
            client.AddParam("doctor_name", doctorname);
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

    private void showToast(final boolean str) {
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                if (str) {
                    Toast.makeText(getActivity(), "Doctor Refer Successfully", Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(getActivity(), "Some Problem is there, Plaese Try Again", Toast.LENGTH_SHORT).show();

            }
        });

    }

}
