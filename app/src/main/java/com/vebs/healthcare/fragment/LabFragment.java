package com.vebs.healthcare.fragment;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.vebs.healthcare.MainActivity;
import com.vebs.healthcare.R;
import com.vebs.healthcare.utils.Function;
import com.vebs.healthcare.utils.PrefsUtil;
import com.vebs.healthcare.utils.RestClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class LabFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters


    private TextView txtSelectLab, txtSelectTest;

    private MaterialEditText edtPatientName, edtPatientNo, edtAge, edtRefer;
    private TextView txtDate;
    private Button btnRefernce, btnHomeCollected, btnCenterCollected;
    private RadioGroup rgGender;
    private int labId = 0, labWhich = 0;

    private Integer[] testWhich = null;
    private String selectedGenderId,lab_test,collected_type;

    public LabFragment() {
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
    public static LabFragment newInstance(String param1, String param2) {
        LabFragment fragment = new LabFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            Function.fetch_lab(getActivity());
            /*new CountDownTimer(2000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {

                }

                @Override
                public void onFinish() {

                    // Toast.makeText(getActivity(), "call_doctor", Toast.LENGTH_SHORT).show();
                    Function.fetch_lab(getActivity());
                    //callApi();
                }
            }.start();*/
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            /*mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);*/
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_lab, container, false);
        init(view);
        return view;
    }

    private void init(View view) {
        txtSelectLab = (TextView) view.findViewById(R.id.txtSelectLab);
        txtSelectTest = (TextView) view.findViewById(R.id.txtSelectTest);

        txtDate = (TextView) view.findViewById(R.id.txtDate);
        txtDate.setText(new SimpleDateFormat("EE, MM-dd-yyyy").format(new Date()));

        edtPatientName = (MaterialEditText) view.findViewById(R.id.edtPatientName);
        edtPatientNo = (MaterialEditText) view.findViewById(R.id.edtPatientNo);
        edtAge = (MaterialEditText) view.findViewById(R.id.edtAge);
        edtRefer = (MaterialEditText) view.findViewById(R.id.edtRefer);

        btnRefernce = (Button) view.findViewById(R.id.btnRefernce);
        btnHomeCollected = (Button) view.findViewById(R.id.btnHomeCollected);
        btnCenterCollected = (Button) view.findViewById(R.id.btnCenterCollected);
        rgGender = (RadioGroup) view.findViewById(R.id.rgGender);
        selectedGenderId = "MALE";
        collected_type="CENTER";

        actionListener();
    }

    private void actionListener() {
        txtSelectTest.setOnClickListener(this);
        txtSelectLab.setOnClickListener(this);
        btnRefernce.setOnClickListener(this);
        btnHomeCollected.setOnClickListener(this);
        btnCenterCollected.setOnClickListener(this);

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
            case R.id.txtSelectLab:
                txtSelectTest.setText(this.getString(R.string.select_lab_test));
                new MaterialDialog.Builder(getActivity())
                        .title(this.getString(R.string.select_lab))
                        .items(Function.lab_list)
                        .itemsCallbackSingleChoice(labWhich, new MaterialDialog.ListCallbackSingleChoice() {
                            @Override
                            public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                dialog.dismiss();
                                labWhich = which;
                                txtSelectLab.setText(Function.lab_list.get(which));
                                labId = Function.lab_list_id.get(which);
                                Function.fetch_lab_test(getActivity(),labId);
                                return true;
                            }
                        })
                        .positiveText(android.R.string.ok)
                        .show();
                break;

            case R.id.txtSelectTest:
                if(Function.lab_test_list.size()==0)
                { new MaterialDialog.Builder(getActivity())
                        .title(this.getString(R.string.no_lab_test))
                        .positiveText(android.R.string.ok)
                        .show();
                   // txtSelectTest.setText(getActivity().getString(R.string.no_lab_test));

                }else {
                    final StringBuilder sb = new StringBuilder();
                    new MaterialDialog.Builder(getActivity())
                            .title(this.getString(R.string.select_lab_test))
                            .items(Function.lab_test_list)
                            .itemsCallbackMultiChoice(testWhich, new MaterialDialog.ListCallbackMultiChoice() {
                                @Override
                                public boolean onSelection(MaterialDialog dialog, Integer[] which, CharSequence[] text) {
                                    if (text.length > 0) {
                                        for (int i = 0; i < text.length; i++) {
                                            sb.append(text[i]).append(", ");
                                            testWhich = which;
                                            lab_test = sb.toString().substring(0, sb.toString().length() - 2);
                                            txtSelectTest.setText(sb.toString().substring(0, sb.toString().length() - 2));
                                        }
                                    } else {
                                        testWhich = null;
                                        txtSelectTest.setText(getString(R.string.select_lab_test));
                                    }
                                    return false;
                                }
                            })
                            .positiveText(android.R.string.ok)
                            .show();
                }
                break;

            case R.id.btnRefernce:
                validateData();
                break;

            case R.id.btnHomeCollected:
                collected_type="HOME";
                break;

            case R.id.btnCenterCollected:
                collected_type="CENTER";
                break;

        }

    }

    private void validateData() {
        if (edtPatientName.length() == 0) {
            edtPatientName.setError(this.getString(R.string.patient_error));
        } else if (edtPatientNo.length() == 0) {
            edtPatientNo.setError(this.getString(R.string.patient_no_error));
        } else if (edtAge.length() == 0) {
            edtAge.setError(this.getString(R.string.patient_age_error));
        } else if (Integer.parseInt(edtAge.getText().toString().trim()) <= 0
                || Integer.parseInt(edtAge.getText().toString().trim()) >= 110) {
            edtAge.setError(this.getString(R.string.patient_proper_age_error));
        } else if (txtSelectLab.getText().equals(this.getString(R.string.select_lab))) {
            txtSelectLab.setError(this.getString(R.string.select_lab));
        } else if (txtSelectTest.getText().equals(this.getString(R.string.select_lab_test))) {
            txtSelectTest.setError(this.getString(R.string.select_lab_test));
        } else if (edtRefer.length() == 0) {
            edtRefer.setError(this.getString(R.string.refer_error));
        } else {
            // send data call referdoctor
            Log.e("data", edtPatientName.getText() + " || " + edtPatientNo.getText() + " || " +
                    edtAge.getText() + " || " + selectedGenderId + " || " + txtSelectLab.getText() + " || " +
                    lab_test + " || " + collected_type + " || "+
                    txtDate.getText() + " || " + edtRefer.getText());

            final RestClient client = new RestClient(Function.REFER_LAB_URL);
            client.AddParam("user_id", String.valueOf(3));
            client.AddParam("patient_name", edtPatientName.getText().toString());
            client.AddParam("patient_mob_number", edtPatientNo.getText().toString());
            client.AddParam("gender", selectedGenderId);
            client.AddParam("age", edtAge.getText().toString());
            client.AddParam("date", txtDate.getText().toString());
            client.AddParam("city_id", String.valueOf(PrefsUtil.getCity(getActivity())));
            client.AddParam("lab_id", String.valueOf(labId));
            client.AddParam("lab_test_name", lab_test);
            client.AddParam("collected_type", collected_type);
            client.AddParam("refer_note", edtRefer.getText().toString());
            new AsyncTask<Void, Void, Void>() {
                public ProgressDialog progressDialog;

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    progressDialog = ProgressDialog.show(getActivity(), "Refer Lab", "Please wait...", false, false);
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
                if(str)
                {
                    Toast.makeText(getActivity(), "Doctor Refer Successfully", Toast.LENGTH_SHORT).show();
                }else
                    Toast.makeText(getActivity(), "Some Problem is there, Plaese Try Again", Toast.LENGTH_SHORT).show();

            }
        });

    }
}
