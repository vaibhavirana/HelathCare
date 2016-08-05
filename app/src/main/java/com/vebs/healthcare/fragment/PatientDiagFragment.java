package com.vebs.healthcare.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.vebs.healthcare.R;
import com.vebs.healthcare.adapter.PatientDiagAdapter;
import com.vebs.healthcare.custom.EmptyLayout;
import com.vebs.healthcare.custom.SimpleDividerItemDecoration;
import com.vebs.healthcare.utils.Function;
import com.vebs.healthcare.utils.PrefsUtil;
import com.vebs.healthcare.utils.RestClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by vraj on 7/30/2016.
 */

public class PatientDiagFragment extends Fragment implements View.OnClickListener
{
    private static ArrayList<String> patient_list;
    private static ArrayList<String> patient_referid_list;
    private Button btnNotConsulted,btnConsulted;
    //private AutoCompleteTextView txtSearch;
    private EditText inputSearch;
    private RecyclerView rvList;
    private EmptyLayout emptyLayout;
    private boolean is_consulted;
    private View view;
    private PatientDiagAdapter adpt;

    public PatientDiagFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static PatientDiagFragment newInstance(String param1, String param2) {
        PatientDiagFragment fragment = new PatientDiagFragment();
        return fragment;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            // Log.e("uid",PrefsUtil.getDrID(getActivity()));
           // init();
            fetch_patient(getActivity(), Function.PATIENT_NC_DIAG_URL);
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
        view=inflater.inflate(R.layout.fragment_patient, container, false);
        init();
        return view;
    }

    private void init() {
        btnConsulted=(Button)view.findViewById(R.id.btnConsulted);
        btnNotConsulted=(Button)view.findViewById(R.id.btnNotConsulted);
        inputSearch = (EditText) view.findViewById(R.id.inputSearch);
        rvList = (RecyclerView) view.findViewById(R.id.RecyclerViewList);
        rvList.addItemDecoration(new SimpleDividerItemDecoration(getActivity().getResources()));
        rvList.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvList.setItemAnimator(new DefaultItemAnimator());
        rvList.setHasFixedSize(true);
        emptyLayout = (EmptyLayout) view.findViewById(R.id.emptyLayout);

        changeUI(btnNotConsulted, btnConsulted);
        setTypeFace();
        setOnClickListner();
        addTextListener();
    }

    private void setTypeFace() {
        Function.setBoldFont(getActivity(),btnConsulted);
        Function.setBoldFont(getActivity(),btnNotConsulted);
        Function.setRegularFont(getActivity(),inputSearch);

    }

    private void setOnClickListner() {
        btnConsulted.setOnClickListener(this);
        btnNotConsulted.setOnClickListener(this);
    }

    private void setAdapater() {

        if(patient_list.size()>0) {
            inputSearch.setVisibility(View.VISIBLE);
            rvList.setVisibility(View.VISIBLE);
            emptyLayout.setVisibility(View.GONE);
            adpt = new PatientDiagAdapter(getActivity(), patient_list,patient_referid_list,is_consulted);
            rvList.setAdapter(adpt);
        }else
        {
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
                        //client.AddParam("user_id", String.valueOf(3));
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
                is_consulted=true;
                changeUI(btnConsulted, btnNotConsulted);
                fetch_patient(getActivity(), Function.PATIENT_C_DIAG_URL);
                break;

            case R.id.btnNotConsulted:
                is_consulted=false;
                changeUI(btnNotConsulted, btnConsulted);
                fetch_patient(getActivity(), Function.PATIENT_NC_DIAG_URL);
                break;

        }

    }

    private void changeUI(Button btn, Button btn1) {
        inputSearch.setText("");
        btn.setBackgroundColor(getActivity().getResources().getColor(R.color.color_light_green));
        btn.setTextColor(getActivity().getResources().getColor(R.color.colorWhite));

        btn1.setBackgroundColor(getActivity().getResources().getColor(R.color.trans_blue));
        btn1.setTextColor(getActivity().getResources().getColor(R.color.colorWhite));
    }

    public void addTextListener(){

        inputSearch.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {}

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence query, int start, int before, int count) {

                query = query.toString().toLowerCase();
                Log.e("Text",query.toString());
                final ArrayList<String> filteredList = new ArrayList<>();
                final ArrayList<String> filteredIdList = new ArrayList<>();

                for (int i = 0; i < patient_list.size(); i++) {

                    final String text = patient_list.get(i).toLowerCase();
                    if (text.contains(query)) {
                        filteredList.add(patient_list.get(i));
                        filteredIdList.add(patient_referid_list.get(i));
                        Log.e("arraylist",filteredList.toString());
                    }
                }

                adpt = new PatientDiagAdapter(getActivity(), filteredList, filteredIdList,is_consulted);
                rvList.setAdapter(adpt);
                adpt.notifyDataSetChanged();  // data set changed
            }
        });
    }

}
