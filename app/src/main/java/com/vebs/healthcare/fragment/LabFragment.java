package com.vebs.healthcare.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.vebs.healthcare.MainActivity;
import com.vebs.healthcare.R;
import com.vebs.healthcare.adapter.DoctorAdapter;
import com.vebs.healthcare.adapter.LabAdapter;
import com.vebs.healthcare.custom.EmptyLayout;
import com.vebs.healthcare.utils.Function;
import com.vebs.healthcare.utils.Prefs;
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

public class LabFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters


    private TextView txtSelectLab;

    private MaterialEditText edtPatientName, edtPatientNo, edtAge, edtRefer;
    private TextView txtDate;
    private Button btnRefernce, btnHomeCollected, btnCenterCollected;
    private RadioGroup rgGender;
    private int labId = 0, labWhich = 0;
    private View layoutLabDetail;
    private Integer[] testWhich = null;
    private String selectedGenderId, collected_type;
    private ArrayList<HashMap<String, Object>> lab_test_detail;
    private String date;
    //private LabAdapter adapter;

    public LabFragment() {
        // Required empty public constructor
    }

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
        // txtSelectTest = (TextView) view.findViewById(R.id.txtSelectTest);

        date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());

        txtDate = (TextView) view.findViewById(R.id.txtDate);
        txtDate.setText(date);

        edtPatientName = (MaterialEditText) view.findViewById(R.id.edtPatientName);
        edtPatientNo = (MaterialEditText) view.findViewById(R.id.edtPatientNo);
        edtAge = (MaterialEditText) view.findViewById(R.id.edtAge);
        edtRefer = (MaterialEditText) view.findViewById(R.id.edtRefer);

        layoutLabDetail = (View) view.findViewById(R.id.layoutLabDetail);
        btnRefernce = (Button) view.findViewById(R.id.btnRefernce);
        btnHomeCollected = (Button) view.findViewById(R.id.btnHomeCollected);
        btnCenterCollected = (Button) view.findViewById(R.id.btnCenterCollected);
        rgGender = (RadioGroup) view.findViewById(R.id.rgGender);
        selectedGenderId = Function.MALE;
        collected_type = "CENTER";

        actionListener();
    }

    private void actionListener() {
        //txtSelectTest.setOnClickListener(this);
        txtSelectLab.setOnClickListener(this);
        btnRefernce.setOnClickListener(this);
        btnHomeCollected.setOnClickListener(this);
        btnCenterCollected.setOnClickListener(this);

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
            case R.id.txtSelectLab:
                //  txtSelectTest.setText(this.getString(R.string.select_lab_test));
                if (PrefsUtil.getCity(getActivity()).isEmpty()) {
                    new MaterialDialog.Builder(getActivity())
                            .title(this.getString(R.string.select_city_first))
                            .positiveText(android.R.string.ok)
                            .show();
                    //showPopup();
                } else {
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
                                    fetch_lab_detail(getActivity(), labId);
                                    //Function.fetch_lab_test(getActivity(), labId);
                                    return true;
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
                collected_type = "HOME";
                break;

            case R.id.btnCenterCollected:
                collected_type = "CENTER";
                break;

        }

    }

    private void validateData() {
        //Log.e("test", test);
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
        }  else if (edtRefer.length() == 0) {
            edtRefer.setError(this.getString(R.string.refer_error));
        } else {
            // send data call referdoctor
           /* Log.e("data", edtPatientName.getText() + " || " + edtPatientNo.getText() + " || " +
                    edtAge.getText() + " || " + selectedGenderId + " || " + labId + " || " +
                    test + " || " + collected_type + " || " +
                    txtDate.getText() + " || " + edtRefer.getText());*/

            final RestClient client = new RestClient(Function.REFER_LAB_URL);
            client.AddParam("user_id", PrefsUtil.getDrID(getActivity()));
            client.AddParam("patient_name", edtPatientName.getText().toString());
            client.AddParam("patient_mob_number", edtPatientNo.getText().toString());
            client.AddParam("gender", selectedGenderId);
            client.AddParam("age", edtAge.getText().toString());
            client.AddParam("date", date);
            client.AddParam("city_id", String.valueOf(PrefsUtil.getCity(getActivity())));
            client.AddParam("lab_id", String.valueOf(labId));
            client.AddParam("lab_test_name", test);
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
        //txtSelectLab.setText("");
        txtSelectLab.setText(this.getString(R.string.select_lab));
        layoutLabDetail.setVisibility(View.GONE);
    }

    public void fetch_lab_detail(final Context mContext, final int labid) {
        final ProgressDialog[] progressDialog = new ProgressDialog[1];
        if (Function.isConnected(mContext)) {
            final RestClient client = new RestClient(Function.LAB_TEST_URL);
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    lab_test_detail = new ArrayList<>();
                    progressDialog[0] = ProgressDialog.show(mContext, "Fetching Lab Test", "Please wait...", false, false);
                    // progressDialog = ProgressDialog.show(MainActivity.this, "Fetching Data", "Please wait...", false, false);
                }

                @Override
                protected Void doInBackground(Void... params) {
                    try {
                        client.AddParam("id", String.valueOf(labid));
                        client.AddParam("cityID", String.valueOf(PrefsUtil.getCityID(mContext)));
                        client.Execute("get");
                        //Log.e("res")
                        JSONArray ja = new JSONArray(client.getResponse());
                        JSONObject jo_test = null;
                        for (int i = 0; i < ja.length(); i++) {

                            JSONObject object = ja.getJSONObject(i);
                            if (object.has("lab")) {
                                jo_test = ja.getJSONObject(i).getJSONObject("lab");
                                if (jo_test != null) {
                                    HashMap<String, Object> doc = new HashMap<String, Object>();
                                    doc.put("id", jo_test.getInt("labId"));
                                    doc.put("labName", jo_test.getString("labName"));
                                    doc.put("drname", jo_test.getString("drName"));
                                    doc.put("email", jo_test.getString("email"));
                                    doc.put("address", jo_test.getString("address"));
                                    doc.put("mobile", jo_test.getString("mobile"));
                                    doc.put("landline", jo_test.getString("ldNumber"));
                                    doc.put("time", jo_test.getString("time"));
                                    doc.put("test", jo_test.getString("test"));
                                    doc.put("price", jo_test.getString("price"));
                                    doc.put("ctype", jo_test.getString("ctype"));
                                    doc.put("offer", jo_test.getString("offere"));
                                    doc.put("note", jo_test.getString("note"));
                                    lab_test_detail.add(doc);
                                    /*lab_test_list.add(jo_test.getString("test"));
                                    lab_test_price.add(jo_test.getInt("price"));
                                    lab_test_type.add(jo_test.getInt("ctype"));*/

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
        if (lab_test_detail.size() > 0) {
            // inputSearch.setVisibility(View.GONE);
            layoutLabDetail.setVisibility(View.VISIBLE);
            setLabLayout();
            //  rvList.setVisibility(View.VISIBLE);
            //emptyLayout.setVisibility(View.GONE);
            //adapter = new LabAdapter(getActivity(), lab_test_detail);
            //rvList.setAdapter(adapter);

        } else {
            layoutLabDetail.setVisibility(View.GONE);
            // inputSearch.setVisibility(View.GONE);
            //emptyLayout.setVisibility(View.VISIBLE);
        }
    }

    TextView txtLabName, txtDrName, txtEmail, txtMobileNo, txtLandLineNo, txtAddress, txtTime, txtFees, txtPatient, txtNote, txtSelectTest,txtSelectTestPrice;
    LinearLayout llLab;
    public String test = "";

    private void setLabLayout() {
        // View v=LayoutInflater.from(getActivity()).
        llLab = (LinearLayout) layoutLabDetail.findViewById(R.id.llLab);
        //llTestList=(LinearLayout)layoutLabDetail.findViewById(R.id.llTestList);
        txtLabName = (TextView) layoutLabDetail.findViewById(R.id.txtLabName);
        txtDrName = (TextView) layoutLabDetail.findViewById(R.id.txtDrName);
        //txtHospName = (TextView) layoutLabDetail.findViewById(R.id.txtHospName);
        txtEmail = (TextView) layoutLabDetail.findViewById(R.id.txtEmail);
        txtMobileNo = (TextView) layoutLabDetail.findViewById(R.id.txtMobileNo);
        txtLandLineNo = (TextView) layoutLabDetail.findViewById(R.id.txtLandLineNo);
        txtAddress = (TextView) layoutLabDetail.findViewById(R.id.txtAddress);
        txtTime = (TextView) layoutLabDetail.findViewById(R.id.txtTime);
        txtFees = (TextView) layoutLabDetail.findViewById(R.id.txtFees);
        txtPatient = (TextView) layoutLabDetail.findViewById(R.id.txtPatient);
        txtNote = (TextView) layoutLabDetail.findViewById(R.id.txtNote);
        txtSelectTest = (TextView) layoutLabDetail.findViewById(R.id.txtSelectTest);
        txtSelectTestPrice = (TextView) layoutLabDetail.findViewById(R.id.txtSelectTestPrice);
        txtSelectTestPrice.setVisibility(View.GONE);
        txtSelectTestPrice.setText("");

        txtSelectTest.setText(getString(R.string.select_lab_test));

        HashMap<String, Object> labDetail = lab_test_detail.get(0);

        txtLabName.setText("Lab Name : " + labDetail.get("labName").toString());
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
        String str = lab_test_detail.get(0).get("test").toString();
        String str1 = lab_test_detail.get(0).get("price").toString();
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
                                        int pos=finalLab_test.indexOf(text[i]);
                                        Log.e("selected",text[i].toString() +" "+pos);
                                        sb1.append(text[i]+" ==> "+lab_price.get(pos)).append("\n");
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
