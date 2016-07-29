package com.vebs.healthcare.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
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
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class DiagnosticFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private MaterialEditText edtPatientName, edtPatientNo, edtAge, edtRefer;
    private TextView txtDate;
    private Button btnRefernce;
    private RadioGroup rgGender;
    private TextView txtSelectCategory, txtSelectDiagCenter, txtSelectDiagTest;
    private String selectedGenderId, doctorname;
    private int catWhich = 0, catId = 0;
    private int diagWhich = 0, diagId = 0;
    private String diagTest;
    private Integer[] testWhich = null;
    private View layoutDiagDetail;
    private String date;
    private ArrayList<HashMap<String, Object>> diag_test_detail;

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
        View view = inflater.inflate(R.layout.fragment_diagnostic, container, false);
        init(view);
        return view;
        // return inflater.inflate(R.layout.fragment_diagnostic, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            Function.fetch_diag_category(getActivity());
        }
    }

    private void init(View view) {
        date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());

        txtDate = (TextView) view.findViewById(R.id.txtDate);
        txtDate.setText(date);

        edtPatientName = (MaterialEditText) view.findViewById(R.id.edtPatientName);
        edtPatientNo = (MaterialEditText) view.findViewById(R.id.edtPatientNo);
        edtAge = (MaterialEditText) view.findViewById(R.id.edtAge);
        edtRefer = (MaterialEditText) view.findViewById(R.id.edtRefer);

        txtSelectCategory = (TextView) view.findViewById(R.id.txtSelectCategory);
        layoutDiagDetail = (View) view.findViewById(R.id.layoutDiagDetail);

        txtSelectDiagCenter = (TextView) view.findViewById(R.id.txtSelectDiagCenter);
       /* txtSelectDiagTest = (TextView) view.findViewById(R.id.txtSelectDiagTest);*/

        btnRefernce = (Button) view.findViewById(R.id.btnRefernce);
        rgGender = (RadioGroup) view.findViewById(R.id.rgGender);
        selectedGenderId = Function.MALE;

        actionListener();
    }

    private void actionListener() {
        txtSelectCategory.setOnClickListener(this);
        txtSelectDiagCenter.setOnClickListener(this);
        btnRefernce.setOnClickListener(this);
        //txtSelectDiagTest.setOnClickListener(this);

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
                            .positiveText(android.R.string.ok)
                            .show();
                    //showPopup();
                } else {

                    //txtSelectDiagTest.setText(this.getString(R.string.select_diag_test));
                    txtSelectDiagCenter.setText(this.getString(R.string.select_diag));
                    new MaterialDialog.Builder(getActivity())
                            .title(this.getString(R.string.select_category))
                            .items(Function.diag_cat_list)
                            .itemsCallbackSingleChoice(catWhich, new MaterialDialog.ListCallbackSingleChoice() {
                                @Override
                                public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                    dialog.dismiss();
                                    catWhich = which;
                                    txtSelectCategory.setText(Function.diag_cat_list.get(which));
                                    catId = Function.diag_cat_list_id.get(which);
                                    //Log.e("id",Function.cat_list.get(which)+" || "+catId+"||"+which);
                                    layoutDiagDetail.setVisibility(View.GONE);
                                    Function.fetch_diag(getActivity(), catId);
                                    return true;
                                }
                            })
                            .positiveText(android.R.string.ok)
                            .show();
                }
                break;

            case R.id.txtSelectDiagCenter:
                if (Function.diag_list.size() == 0) {
                    new MaterialDialog.Builder(getActivity())
                            .title(this.getString(R.string.no_diag))
                            .positiveText(android.R.string.ok)
                            .show();
                } else {
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
                                    fetch_diag_detail(getActivity(), diagId);
                                    // Function.fetch_diag_test(getActivity(), diagId);
                                    // doctorname=Function.doc_list.get(which);
                                    return true;
                                }
                            })
                            .positiveText(android.R.string.ok)
                            .show();
                }
                break;

            /*case R.id.txtSelectDiagTest:
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
                break;*/

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
        } else if (txtSelectTest.getText().equals(this.getString(R.string.select_diag_test))) {
            txtSelectTest.setError(this.getString(R.string.select_diag_test));
        } else if (edtRefer.length() == 0) {
            edtRefer.setError(this.getString(R.string.refer_error));
        } else {
            // send data call referdoctor
           /* Log.e("data", edtPatientName.getText() + " || " + edtPatientNo.getText() + " || " +
                    edtAge.getText() + " || " + selectedGenderId + " || " + test + " || " + diagId +" || "+
                    txtSelectCategory.getText() + " || " +
                    date + " || " + edtRefer.getText());*/

            final RestClient client = new RestClient(Function.REFER_DIAG_URL);
            client.AddParam("user_id", PrefsUtil.getDrID(getActivity()));
            client.AddParam("patient_name", edtPatientName.getText().toString());
            client.AddParam("patient_mob_number", edtPatientNo.getText().toString());
            client.AddParam("gender", selectedGenderId);
            client.AddParam("age", edtAge.getText().toString());
            client.AddParam("date", date);
            client.AddParam("city_id", String.valueOf(PrefsUtil.getCity(getActivity())));
            client.AddParam("category_id", String.valueOf(catId));
            client.AddParam("diagnostic_center_id", String.valueOf(diagId));
            client.AddParam("diagnostic_test_name", test);
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

   /* private void showToast(final boolean str) {
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                if (str) {
                    Toast.makeText(getActivity(), "Doctor Refer Successfully", Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(getActivity(), "Some Problem is there, Plaese Try Again", Toast.LENGTH_SHORT).show();

            }
        });

    }*/

    private void showToast(final boolean str) {

        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                /*if (str) {
                    Toast.makeText(getActivity(), "Doctor Refer Successfully", Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(getActivity(), "Some Problem is there, Plaese Try Again", Toast.LENGTH_SHORT).show();*/

                if (str) {
                    new MaterialDialog.Builder(getActivity())
                            .title(getActivity().getString(R.string.lab_refer_successfully))
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
        txtSelectDiagCenter.setText(this.getString(R.string.select_diag));
        layoutDiagDetail.setVisibility(View.GONE);
    }

    public void fetch_diag_detail(final Context mContext, final int diagId) {
        final ProgressDialog[] progressDialog = new ProgressDialog[1];
        if (Function.isConnected(mContext)) {
            final RestClient client = new RestClient(Function.DIAG_TEST_URL);
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    diag_test_detail = new ArrayList<>();
                    progressDialog[0] = ProgressDialog.show(mContext, "Fetching Diagnostic Center Detail", "Please wait...", false, false);
                    // progressDialog = ProgressDialog.show(MainActivity.this, "Fetching Data", "Please wait...", false, false);
                }

                @Override
                protected Void doInBackground(Void... params) {
                    try {
                        client.AddParam("id", String.valueOf(diagId));
                        client.AddParam("cityID", String.valueOf(PrefsUtil.getCityID(mContext)));
                        client.Execute("get");
                        //Log.e("res")
                        JSONArray ja = new JSONArray(client.getResponse());
                        JSONObject jo_test = null;
                        for (int i = 0; i < ja.length(); i++) {

                            JSONObject object = ja.getJSONObject(i);
                            if (object.has("diagnostics")) {
                                jo_test = ja.getJSONObject(i).getJSONObject("diagnostics");
                                if (jo_test != null) {
                                    HashMap<String, Object> doc = new HashMap<String, Object>();
                                    doc.put("id", jo_test.getInt("diagId"));
                                    doc.put("diagName", jo_test.getString("diagName"));
                                    doc.put("drname", jo_test.getString("drName"));
                                    doc.put("email", jo_test.getString("email"));
                                    doc.put("address", jo_test.getString("address"));
                                    doc.put("mobile", jo_test.getString("mobile"));
                                    doc.put("landline", jo_test.getString("ldNumber"));
                                    doc.put("time", jo_test.getString("time"));
                                    doc.put("test", jo_test.getString("test"));
                                    doc.put("price", jo_test.getString("price"));
                                    //doc.put("ctype", jo_test.getString("ctype"));
                                    doc.put("offer", jo_test.getString("offere"));
                                    doc.put("note", jo_test.getString("note"));
                                    diag_test_detail.add(doc);


                                }
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
                    setAdapter();
                }
            }.execute();

        } else {
            Function.showInternetPopup(mContext);
        }
    }

    private void setAdapter() {
        // fetchDoctorList();
        if (diag_test_detail.size() > 0) {
            // inputSearch.setVisibility(View.GONE);
            layoutDiagDetail.setVisibility(View.VISIBLE);
            setLabLayout();

        } else {
            layoutDiagDetail.setVisibility(View.GONE);
        }
    }

    TextView txtLabName, txtDrName, txtEmail, txtMobileNo, txtLandLineNo, txtAddress, txtTime, txtFees, txtPatient, txtNote, txtSelectTest, txtSelectTestPrice;
    LinearLayout llLab;
    public String test = "";

    private void setLabLayout() {
        // View v=LayoutInflater.from(getActivity()).
        llLab = (LinearLayout) layoutDiagDetail.findViewById(R.id.llLab);
        //llTestList=(LinearLayout)layoutDiagDetail.findViewById(R.id.llTestList);
        txtLabName = (TextView) layoutDiagDetail.findViewById(R.id.txtLabName);
        txtDrName = (TextView) layoutDiagDetail.findViewById(R.id.txtDrName);
        //txtHospName = (TextView) layoutDiagDetail.findViewById(R.id.txtHospName);
        txtEmail = (TextView) layoutDiagDetail.findViewById(R.id.txtEmail);
        txtMobileNo = (TextView) layoutDiagDetail.findViewById(R.id.txtMobileNo);
        txtLandLineNo = (TextView) layoutDiagDetail.findViewById(R.id.txtLandLineNo);
        txtAddress = (TextView) layoutDiagDetail.findViewById(R.id.txtAddress);
        txtTime = (TextView) layoutDiagDetail.findViewById(R.id.txtTime);
        txtFees = (TextView) layoutDiagDetail.findViewById(R.id.txtFees);
        txtPatient = (TextView) layoutDiagDetail.findViewById(R.id.txtPatient);
        txtNote = (TextView) layoutDiagDetail.findViewById(R.id.txtNote);
        txtSelectTest = (TextView) layoutDiagDetail.findViewById(R.id.txtSelectTest);
        txtSelectTestPrice = (TextView) layoutDiagDetail.findViewById(R.id.txtSelectTestPrice);
        txtSelectTestPrice.setVisibility(View.GONE);
        txtSelectTestPrice.setText("");

        txtSelectTest.setText(getString(R.string.select_diag_test));

        HashMap<String, Object> labDetail = diag_test_detail.get(0);

        txtLabName.setText("Diagnostic Name : " + labDetail.get("diagName").toString());
        txtDrName.setText("Doctor Name : " + labDetail.get("drname").toString());
        txtEmail.setText("Doctor Email Id : " + labDetail.get("email").toString());
        txtMobileNo.setText("Mobile No. : " + labDetail.get("mobile").toString());
        txtLandLineNo.setText("Landline No. : " + labDetail.get("landline").toString());
        txtAddress.setText("Address : " + labDetail.get("address").toString());
        txtTime.setText("Time : " + labDetail.get("time").toString());
        txtPatient.setText("No of Patients : " + labDetail.get("offer"));
        txtNote.setText("Note : " + labDetail.get("note"));

        List<String> lab_test = new ArrayList<>();
        testWhich = null;

        //Log.e("lab test", lab_test_detail.get(0).get("test").toString());
        String str = diag_test_detail.get(0).get("test").toString();
        String str1 = diag_test_detail.get(0).get("price").toString();
        lab_test = Arrays.asList(str.split(","));
        final List<String> lab_price = Arrays.asList(str1.split(","));

        final List<String> finalLab_test = lab_test;
        txtSelectTest.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final StringBuilder sb = new StringBuilder();
                final StringBuilder sb1 = new StringBuilder();
                new MaterialDialog.Builder(getActivity())
                        .title(getActivity().getString(R.string.select_lab_test))
                        .items(finalLab_test)
                        .itemsCallbackMultiChoice(testWhich, new MaterialDialog.ListCallbackMultiChoice() {
                            @Override
                            public boolean onSelection(MaterialDialog dialog, Integer[] which, CharSequence[] text) {
                                if (text.length > 0) {
                                    txtSelectTestPrice.setVisibility(View.VISIBLE);
                                    txtSelectTestPrice.setText("Test Selected");
                                    for (int i = 0; i < text.length; i++) {
                                        sb.append(text[i]).append(", ");

                                        testWhich = which;
                                        int pos = finalLab_test.indexOf(text[i]);
                                        Log.e("selected", text[i].toString() + " " + pos);
                                        sb1.append(text[i] + " ==> " + lab_price.get(pos)).append("\n");
                                        test = sb.toString().substring(0, sb.toString().length() - 2);
                                        txtSelectTest.setText(sb.toString().substring(0, sb.toString().length() - 2));
                                        txtSelectTestPrice.setText(sb1.toString().substring(0, sb1.toString().length() - 1));
                                    }
                                } else {
                                    testWhich = null;
                                    txtSelectTest.setText(getString(R.string.select_lab_test));
                                    txtSelectTestPrice.setText("");
                                    txtSelectTestPrice.setVisibility(View.GONE);
                                }
                                return false;
                            }
                        })
                        .positiveText(android.R.string.ok)

                        .show();
            }
        });

    }
}
