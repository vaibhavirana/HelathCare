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

    // category
    private ArrayList<String> cat_list;
    private ArrayList<Integer> cat_list_id;
    private int catWhich = 0, catId = 0, docWhich = 0, docId = 0;

    // doctor
    private ArrayList<String> doc_list;
    private ArrayList<Integer> doc_list_id;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ProgressDialog loading;

    private MaterialEditText edtPatientName, edtPatientNo, edtAge, edtRefer;
    private TextView txtDate, txtSelectCategory, txtSelectDoctor;
    private Button btnRefernce, btnEmergency;
    private RadioButton rdMale, rdFemale;
    private RadioGroup rgGender;
    private JSONArray jsonArray;

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
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
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
            new CountDownTimer(2000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {

                }

                @Override
                public void onFinish() {
                    loading = ProgressDialog.show(getActivity(), "Fetching Data", "Please wait...", false, false);
                    Toast.makeText(getActivity(), "call_doctor", Toast.LENGTH_SHORT).show();
                    callApi();
                }
            }.start();
        }
    }

    private void init(View view) {
        txtDate = (TextView) view.findViewById(R.id.txtDate);
        txtSelectCategory = (TextView) view.findViewById(R.id.txtSelectCategory);
        txtSelectDoctor = (TextView) view.findViewById(R.id.txtSelectDoctor);
        edtPatientName = (MaterialEditText) view.findViewById(R.id.edtPatientName);
        edtPatientNo = (MaterialEditText) view.findViewById(R.id.edtPatientNo);
        edtAge = (MaterialEditText) view.findViewById(R.id.edtAge);
        edtRefer = (MaterialEditText) view.findViewById(R.id.edtRefer);

        btnRefernce = (Button) view.findViewById(R.id.btnRefernce);
        btnEmergency = (Button) view.findViewById(R.id.btnEmergency);
        rdMale = (RadioButton) view.findViewById(R.id.rdMale);
        rdFemale = (RadioButton) view.findViewById(R.id.rdFemale);
        rgGender = (RadioGroup) view.findViewById(R.id.rgGender);

        setData();

        actionListener();

    }

    private void actionListener() {
        txtSelectCategory.setOnClickListener(this);
        txtSelectDoctor.setOnClickListener(this);
    }

    private void setData() {
        txtDate.setText(new SimpleDateFormat("EE, MM-dd-yyyy").format(new Date()));

    }

    private void callApi() {

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                cat_list = new ArrayList<>();
                cat_list_id = new ArrayList<>();

                doc_list = new ArrayList<>();
                doc_list_id = new ArrayList<>();

            }

            @Override
            protected Void doInBackground(Void... params) {
                try {

                    jsonArray = ((MainActivity) getActivity()).getMainJSONArray();

                    JSONObject jo_cat = null, jo_doc = null;

                    int p = 0;

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject object = jsonArray.getJSONObject(i);
                        if (object.has("category")) {
                            jo_cat = jsonArray.getJSONObject(i).getJSONObject("category");
                            if (jo_cat != null) {
                                cat_list.add(jo_cat.getString("catName"));
                                cat_list_id.add(jo_cat.getInt("catId"));
                            }
                        }

                        if (object.has("doctors")) {
                            jo_doc = jsonArray.getJSONObject(i).getJSONObject("doctors");
                            if (jo_doc != null) {
                                doc_list.add(jo_doc.getString("drName"));
                                doc_list_id.add(jo_doc.getInt("drId"));
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txtSelectCategory:
                new MaterialDialog.Builder(getActivity())
                        .title("Select Category")
                        .items(cat_list)
                        .itemsCallbackSingleChoice(catWhich, new MaterialDialog.ListCallbackSingleChoice() {
                            @Override
                            public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                dialog.dismiss();
                                catWhich = which;
                                txtSelectCategory.setText(cat_list.get(which));
                                catId = cat_list_id.get(which);
                                return true;
                            }
                        })
                        .positiveText(android.R.string.ok)
                        .show();
                break;

            case R.id.txtSelectDoctor:
                new MaterialDialog.Builder(getActivity())
                        .title("Select Doctor")
                        .items(doc_list)
                        .itemsCallbackSingleChoice(docWhich, new MaterialDialog.ListCallbackSingleChoice() {
                            @Override
                            public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                dialog.dismiss();
                                docWhich = which;
                                txtSelectDoctor.setText(doc_list.get(which));
                                docId = doc_list_id.get(which);
                                return true;
                            }
                        })
                        .positiveText(android.R.string.ok)
                        .show();
                break;
        }
    }
}
