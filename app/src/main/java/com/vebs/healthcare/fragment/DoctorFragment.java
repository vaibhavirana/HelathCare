package com.vebs.healthcare.fragment;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
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

import com.rengwuxian.materialedittext.MaterialEditText;
import com.vebs.healthcare.MainActivity;
import com.vebs.healthcare.R;
import com.vebs.healthcare.utils.Function;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DoctorFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    InputStream is = null;
    String line = null;
    String result = null;

    // category
    private ArrayList<String> cat_list;
    private ArrayList<Integer> cat_list_id;

    // doctor
    private ArrayList<String> doc_list;
    private ArrayList<Integer> doc_list_id;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ProgressDialog loading;

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
        if(isVisibleToUser){
            callApi();
        }
    }

    private MaterialEditText edtPatientName, edtPatientNo, edtAge, edtRefer;
    private TextView txtDate;
    private Button btnRefernce, btnEmergency;
    private RadioButton rdMale, rdFemale;
    private RadioGroup rgGender;
    private Spinner spnCategory, spnDoctor;

    private void init(View view) {
        txtDate = (TextView) view.findViewById(R.id.txtDate);
        edtPatientName = (MaterialEditText) view.findViewById(R.id.edtPatientName);
        edtPatientNo = (MaterialEditText) view.findViewById(R.id.edtPatientNo);
        edtAge = (MaterialEditText) view.findViewById(R.id.edtAge);
        edtRefer = (MaterialEditText) view.findViewById(R.id.edtRefer);

        btnRefernce = (Button) view.findViewById(R.id.btnRefernce);
        btnEmergency = (Button) view.findViewById(R.id.btnEmergency);
        rdMale = (RadioButton) view.findViewById(R.id.rdMale);
        rdFemale = (RadioButton) view.findViewById(R.id.rdFemale);
        rgGender = (RadioGroup) view.findViewById(R.id.rgGender);

        spnCategory = (Spinner) view.findViewById(R.id.spnCategory);
        spnDoctor = (Spinner) view.findViewById(R.id.spnDoctor);

        loading = ProgressDialog.show(getActivity(), "Fetching Data", "Please wait...", false, false);
        cat_list = new ArrayList<>();
        cat_list_id = new ArrayList<>();

        doc_list = new ArrayList<>();
        doc_list_id = new ArrayList<>();


        setData();

    }

    private void setData() {

        txtDate.setText(new SimpleDateFormat("EE, MM-dd-yyyy").format(new Date()));

    }

    private void callApi() {

        if (Function.isConnected(getActivity())) {

            new AsyncTask<Void, Void, Void>() {

                StringBuilder sb = new StringBuilder();

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();

                }

                @Override
                protected Void doInBackground(Void... params) {
                    try {
                        HttpClient httpClient = new DefaultHttpClient();
                        HttpPost httpPost = new HttpPost(Function.ROOT_URL);
                        HttpResponse response = httpClient.execute(httpPost);
                        HttpEntity entity = response.getEntity();
                        is = entity.getContent();

                        BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);

                        while ((line = reader.readLine()) != null) {
                            sb.append(line + "\n");
                        }

                        is.close();

                        String result = sb.toString();
                        Log.e("result", result.toString());
                        JSONArray ja = new JSONArray(result);
                        JSONObject jo_cat = null, jo_doc = null;
                        int p = 0;
                        for (int i = 0; i < ja.length(); i++) {

                            JSONObject object = ja.getJSONObject(i);
                            if (object.has("category")) {
                                jo_cat = ja.getJSONObject(i).getJSONObject("category");
                                if (jo_cat != null) {
                                    cat_list.add(jo_cat.getString("catName"));
                                    cat_list_id.add(jo_cat.getInt("catId"));

                                }
                            }

                            if (object.has("doctors")) {
                                jo_doc = ja.getJSONObject(i).getJSONObject("doctors");
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
                    setCategoryData();
                }
            }.execute();
        }
    }

    private void setCategoryData() {
        ArrayAdapter<String> catAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, cat_list);
        // dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnCategory.setAdapter(catAdapter);

        ArrayAdapter<String> docAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, doc_list);
        // dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnDoctor.setAdapter(docAdapter);

    }


}
