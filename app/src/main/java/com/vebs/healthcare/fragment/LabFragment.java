package com.vebs.healthcare.fragment;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
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
    private String mParam1;
    private String mParam2;
    ProgressDialog loading;
    private JSONArray jsonArray;

    private TextView txtSelectLab, txtSelectTest;

    private MaterialEditText edtPatientName, edtPatientNo, edtAge, edtRefer;
    private TextView txtDate;
    private Button btnRefernce, btnHomeCollected, btnCenterCollected;
    private RadioButton rdMale, rdFemale;
    private RadioGroup rgGender;

    // lab
    private ArrayList<String> lab_list;
    private ArrayList<Integer> lab_list_id;
    private int labId = 0, labWhich = 0;

    // test
    private ArrayList<String> test_list;
    private ArrayList<Integer> test_list_id;
    private int testId = 0;
    private Integer[] testWhich = null;

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
            loading = ProgressDialog.show(getActivity(), "Fetching Data", "Please wait...", false, false);
            Toast.makeText(getActivity(), "call_lab", Toast.LENGTH_SHORT).show();
            callApi();
        }
    }

    private void callApi() {

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                lab_list = new ArrayList<>();
                lab_list_id = new ArrayList<>();

                test_list = new ArrayList<>();
                test_list_id = new ArrayList<>();

            }

            @Override
            protected Void doInBackground(Void... params) {
                try {

                    jsonArray = ((MainActivity) getActivity()).getMainJSONArray();

                    JSONObject jo_lab = null, jo_test = null;

                    int p = 0;

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject object = jsonArray.getJSONObject(i);
                        if (object.has("lab")) {
                            jo_lab = jsonArray.getJSONObject(i).getJSONObject("lab");
                            if (jo_lab != null) {
                                lab_list.add(jo_lab.getString("labName"));
                                lab_list_id.add(jo_lab.getInt("labId"));
                            }
                        }

                        if (object.has("test")) {
                            jo_test = jsonArray.getJSONObject(i).getJSONObject("test");
                            if (jo_test != null) {
                                test_list.add(jo_test.getString("testName"));
                                test_list_id.add(jo_test.getInt("testId"));
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
                loading.dismiss();
            }
        }.execute();
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
        View view = inflater.inflate(R.layout.fragment_lab, container, false);
        init(view);
        return view;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.txtSelectLab:
                new MaterialDialog.Builder(getActivity())
                        .title("Select Lab")
                        .items(lab_list)
                        .itemsCallbackSingleChoice(labWhich, new MaterialDialog.ListCallbackSingleChoice() {
                            @Override
                            public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                dialog.dismiss();
                                labWhich = which;
                                txtSelectLab.setText(lab_list.get(which));
                                labId = lab_list_id.get(which);
                                return true;
                            }
                        })
                        .positiveText(android.R.string.ok)
                        .show();
                break;

            case R.id.txtSelectTest:
                final StringBuilder sb = new StringBuilder();
                new MaterialDialog.Builder(getActivity())
                        .title("Select Test")
                        .items(test_list)
                        .itemsCallbackMultiChoice(testWhich, new MaterialDialog.ListCallbackMultiChoice() {
                            @Override
                            public boolean onSelection(MaterialDialog dialog, Integer[] which, CharSequence[] text) {
                                if (text.length > 0) {
                                    for (int i = 0; i < text.length; i++) {
                                        sb.append(text[i]).append(", ");
                                        testWhich = which;
                                        txtSelectTest.setText(sb.toString().substring(0, sb.toString().length() - 2));
                                    }
                                } else {
                                    testWhich = null;
                                    txtSelectTest.setText("Select Test");
                                }
                                return false;
                            }
                        })
                        .positiveText(android.R.string.ok)
                        .show();
                break;
        }

    }

    private void init(View view) {
        txtSelectLab = (TextView) view.findViewById(R.id.txtSelectLab);
        txtSelectTest = (TextView) view.findViewById(R.id.txtSelectTest);

        txtDate = (TextView) view.findViewById(R.id.txtDate);
        edtPatientName = (MaterialEditText) view.findViewById(R.id.edtPatientName);
        edtPatientNo = (MaterialEditText) view.findViewById(R.id.edtPatientNo);
        edtAge = (MaterialEditText) view.findViewById(R.id.edtAge);
        edtRefer = (MaterialEditText) view.findViewById(R.id.edtRefer);

        btnRefernce = (Button) view.findViewById(R.id.btnRefernce);
        btnHomeCollected = (Button) view.findViewById(R.id.btnHomeCollected);
        btnCenterCollected = (Button) view.findViewById(R.id.btnCenterCollected);
        rdMale = (RadioButton) view.findViewById(R.id.rdMale);
        rdFemale = (RadioButton) view.findViewById(R.id.rdFemale);
        rgGender = (RadioGroup) view.findViewById(R.id.rgGender);

        setData();

        actionListener();
    }

    private void actionListener() {
        txtSelectTest.setOnClickListener(this);
        txtSelectLab.setOnClickListener(this);
    }

    private void setData() {

        txtDate.setText(new SimpleDateFormat("EE, MM-dd-yyyy").format(new Date()));

    }


}
