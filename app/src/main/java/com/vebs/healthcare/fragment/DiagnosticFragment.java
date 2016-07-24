package com.vebs.healthcare.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.vebs.healthcare.R;
import com.vebs.healthcare.utils.Function;
import com.vebs.healthcare.utils.PrefsUtil;
import com.vebs.healthcare.utils.RestClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import static com.vebs.healthcare.R.id.txtSelectTest;

public class DiagnosticFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private MaterialEditText edtPatientName,edtPatientNo,edtAge,edtRefer;
    private TextView txtDate;
    private Button btnRefernce;
    private RadioGroup rgGender;
    private TextView txtSelectCategory,txtSelectDiagCenter,txtSelectDiagTest;
    private String selectedGenderId,doctorname;
    private int catWhich = 0, catId = 0;
    private int diagWhich=0,diagId=0;
    private String diagTest;
    private Integer[] testWhich = null;
    public DiagnosticFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static DiagnosticFragment newInstance(String param1, String param2) {
        DiagnosticFragment fragment = new DiagnosticFragment();
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
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_diagnostic, container, false);
        init(view);
        return view;
       // return inflater.inflate(R.layout.fragment_diagnostic, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event

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
                   // Function.fetch_diag(getActivity());
                    //callApi();
                }
            }.start();*/
        }
    }

    private void init(View view) {
        txtDate=(TextView) view.findViewById(R.id.txtDate);
        txtDate.setText(new SimpleDateFormat("EE, MM-dd-yyyy").format(new Date()));

        edtPatientName= (MaterialEditText) view.findViewById(R.id.edtPatientName);
        edtPatientNo=(MaterialEditText) view.findViewById(R.id.edtPatientNo);
        edtAge=(MaterialEditText) view.findViewById(R.id.edtAge);
        edtRefer=(MaterialEditText) view.findViewById(R.id.edtRefer);

        txtSelectCategory = (TextView) view.findViewById(R.id.txtSelectCategory);
        txtSelectDiagCenter = (TextView) view.findViewById(R.id.txtSelectDiagCenter);
        txtSelectDiagTest = (TextView) view.findViewById(R.id.txtSelectDiagTest);

        btnRefernce=(Button)view.findViewById(R.id.btnRefernce);
        rgGender= (RadioGroup) view.findViewById(R.id.rgGender);
        selectedGenderId = "MALE";

        actionListener();
    }

    private void actionListener() {
        txtSelectCategory.setOnClickListener(this);
        txtSelectDiagCenter.setOnClickListener(this);
        btnRefernce.setOnClickListener(this);
        txtSelectDiagTest.setOnClickListener(this);

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
                txtSelectDiagTest.setText(this.getString(R.string.select_diag_test));
                txtSelectDiagCenter.setText(this.getString(R.string.select_diag));
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
                                Function.fetch_diag(getActivity());
                                return true;
                            }
                        })
                        .positiveText(android.R.string.ok)
                        .show();
                break;

            case R.id.txtSelectDiagCenter:
                if(Function.diag_list.size()==0)
                {
                    new MaterialDialog.Builder(getActivity())
                            .title(this.getString(R.string.no_diag))
                            .positiveText(android.R.string.ok)
                            .show();
                }else {
                    new MaterialDialog.Builder(getActivity())
                            .title(this.getString(R.string.select_diag))
                            .items(Function.diag_list)
                            .itemsCallbackSingleChoice(diagWhich, new MaterialDialog.ListCallbackSingleChoice() {
                                @Override
                                public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                    diagWhich = which;
                                    dialog.dismiss();
                                    txtSelectDiagCenter.setText(Function.diag_list.get(which));
                                    diagId = Function.diag_list_id.get(which);
                                    Function.fetch_diag_test(getActivity(), diagId);
                                    // doctorname=Function.doc_list.get(which);
                                    return true;
                                }
                            })
                            .positiveText(android.R.string.ok)
                            .show();
                }
                break;

            case R.id.txtSelectDiagTest:
                if(Function.diag_test_list.size()==0)
                { new MaterialDialog.Builder(getActivity())
                        .title(this.getString(R.string.no_diag_test))
                        .positiveText(android.R.string.ok)
                        .show();
                    //txtSelectDiagTest.setText(getActivity().getString(R.string.no_diag_test));

                }else {
                    final StringBuilder sb = new StringBuilder();
                    new MaterialDialog.Builder(getActivity())
                            .title(this.getString(R.string.select_diag_test))
                            .items(Function.diag_test_list)
                            .itemsCallbackMultiChoice(testWhich, new MaterialDialog.ListCallbackMultiChoice() {
                                @Override
                                public boolean onSelection(MaterialDialog dialog, Integer[] which, CharSequence[] text) {
                                    if (text.length > 0) {
                                        for (int i = 0; i < text.length; i++) {
                                            sb.append(text[i]).append(", ");
                                            testWhich = which;
                                            diagTest = sb.toString().substring(0, sb.toString().length() - 2);
                                            txtSelectDiagTest.setText(sb.toString().substring(0, sb.toString().length() - 2));
                                        }
                                    } else {
                                        testWhich = null;
                                        txtSelectDiagTest.setText(getString(R.string.select_diag_test));
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
        } else if (txtSelectCategory.getText().equals(this.getString(R.string.select_category))) {
            txtSelectCategory.setError(this.getString(R.string.select_category));
        } else if (txtSelectDiagCenter.getText().equals(this.getString(R.string.select_diag))) {
            txtSelectDiagCenter.setError(this.getString(R.string.select_diag));
        } else if (txtSelectDiagTest.getText().equals(this.getString(R.string.select_diag_test))) {
            txtSelectDiagTest.setError(this.getString(R.string.select_diag_test));
        } else if (edtRefer.length() == 0) {
            edtRefer.setError(this.getString(R.string.refer_error));
        } else {
            // send data call referdoctor
            Log.e("data", edtPatientName.getText() + " || " + edtPatientNo.getText() + " || " +
                    edtAge.getText() + " || " + selectedGenderId + " || " + txtSelectDiagCenter.getText() + " || " +
                    txtSelectCategory.getText() + " || " +
                    diagTest + " || " +
                    txtDate.getText() + " || " + edtRefer.getText());

            final RestClient client = new RestClient(Function.REFER_DIAG_URL);
            client.AddParam("user_id", String.valueOf(3));
            client.AddParam("patient_name", edtPatientName.getText().toString());
            client.AddParam("patient_mob_number", edtPatientNo.getText().toString());
            client.AddParam("gender", selectedGenderId);
            client.AddParam("age", edtAge.getText().toString());
            client.AddParam("date", txtDate.getText().toString());
            client.AddParam("city_id", String.valueOf(PrefsUtil.getCity(getActivity())));
            client.AddParam("category_id", String.valueOf(catId));
            client.AddParam("diagnostic_center_id", String.valueOf(diagId));
            client.AddParam("diagnostic_test_name", diagTest);
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
                if(str)
                {
                    Toast.makeText(getActivity(), "Doctor Refer Successfully", Toast.LENGTH_SHORT).show();
                }else
                    Toast.makeText(getActivity(), "Some Problem is there, Plaese Try Again", Toast.LENGTH_SHORT).show();

            }
        });

    }

}
