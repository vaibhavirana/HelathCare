package com.vebs.healthcare.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.vebs.healthcare.R;
import com.vebs.healthcare.adapter.PatientAdapter;
import com.vebs.healthcare.custom.EmptyLayout;
import com.vebs.healthcare.utils.Function;
import com.vebs.healthcare.utils.PrefsUtil;
import com.vebs.healthcare.utils.RestClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by vraj on 7/30/2016.
 */

public class PatientDoctorFragment extends Fragment implements View.OnClickListener {
    private static ArrayList<String> patient_list;
    private static ArrayList<String> patient_referid_list;
    private Button btnNotConsulted, btnConsulted;
    //private AutoCompleteTextView txtSearch;
    private EditText inputSearch;
    private RecyclerView rvList;
    private EmptyLayout emptyLayout;

    public PatientDoctorFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static PatientDoctorFragment newInstance(String param1, String param2) {
        PatientDoctorFragment fragment = new PatientDoctorFragment();
       /* Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);*/
        return fragment;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            // Log.e("uid",PrefsUtil.getDrID(getActivity()));
            fetch_patient(getActivity(), Function.PATIENT_NC_DOC_URL);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
           /* mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);*/
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_patient, container, false);
        init(view);
        return view;
    }

    private void init(View view) {
        btnConsulted = (Button) view.findViewById(R.id.btnConsulted);
        btnNotConsulted = (Button) view.findViewById(R.id.btnNotConsulted);

        inputSearch = (EditText) view.findViewById(R.id.inputSearch);
        rvList = (RecyclerView) view.findViewById(R.id.RecyclerViewList);
        rvList.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvList.setHasFixedSize(true);
        emptyLayout = (EmptyLayout) view.findViewById(R.id.emptyLayout);
        changeUI(btnNotConsulted, btnConsulted);
        setOnClickListner();
    }

    private void setOnClickListner() {
        btnConsulted.setOnClickListener(this);
        btnNotConsulted.setOnClickListener(this);
    }

    private void setAdapater() {

        if (patient_list.size() > 0) {
            inputSearch.setVisibility(View.VISIBLE);
            rvList.setVisibility(View.VISIBLE);
            emptyLayout.setVisibility(View.GONE);
            inputSearch.setText(R.string.search_patient);
            PatientAdapter adpt = new PatientAdapter(getActivity(), patient_list, patient_referid_list);
            rvList.setAdapter(adpt);
        } else {
            rvList.setVisibility(View.GONE);
            inputSearch.setVisibility(View.GONE);
            emptyLayout.setVisibility(View.VISIBLE);
            emptyLayout.setType(Function.NO_PATIENT);
        }

    }

    public void fetch_patient(final Context mContext, String url) {
        final ProgressDialog[] progressDialog = new ProgressDialog[1];
        if (Function.isConnected(mContext)) {
            final RestClient client = new RestClient(url);
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    patient_list = new ArrayList<>();
                    patient_referid_list = new ArrayList<>();
                    progressDialog[0] = ProgressDialog.show(mContext, "Fetching Patient List", "Please wait...", false, false);
                    // progressDialog = ProgressDialog.show(MainActivity.this, "Fetching Data", "Please wait...", false, false);
                }

                @Override
                protected Void doInBackground(Void... params) {
                    try {
                        client.AddParam("user_id", String.valueOf(PrefsUtil.getDrID(mContext)));
                        client.AddParam("city_id", String.valueOf(PrefsUtil.getCityID(mContext)));
                        client.Execute("get");
                        //Log.e("res")
                        JSONArray ja = new JSONArray(client.getResponse());
                        JSONObject jo_test = null;
                        for (int i = 0; i < ja.length(); i++) {

                            JSONObject object = ja.getJSONObject(i);
                            if (object.has("patient Name")) {
                                jo_test = ja.getJSONObject(i).getJSONObject("patient Name");
                                if (jo_test != null) {
                                    patient_list.add(jo_test.getString("Patient Name"));
                                    patient_referid_list.add(jo_test.getString("Refere ID"));

                                    Log.e("lab test", patient_list.get(i));
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
                    setAdapater();

                }
            }.execute();

        } else {
            Function.showInternetPopup(mContext);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnConsulted:
                changeUI(btnConsulted, btnNotConsulted);
                fetch_patient(getActivity(), Function.PATIENT_C_DOC_URL);
                break;

            case R.id.btnNotConsulted:
                changeUI(btnNotConsulted, btnConsulted);
                fetch_patient(getActivity(), Function.PATIENT_NC_DOC_URL);
                break;

        }

    }

    private void changeUI(Button btn, Button btn1) {
        btn.setBackgroundColor(getActivity().getResources().getColor(R.color.colorPrimary));
        btn.setTextColor(getActivity().getResources().getColor(R.color.colorWhite));

        btn1.setBackgroundColor(getActivity().getResources().getColor(R.color.color_light_green));
        btn1.setTextColor(getActivity().getResources().getColor(R.color.colorPrimary));
    }
}
