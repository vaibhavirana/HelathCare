package com.vebs.healthcare.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.vebs.healthcare.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DiagnosticFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private MaterialEditText edtPatientName,edtPatientNo,edtAge,edtRefer;
    private TextView txtDate;
    private Button btnRefernce,btnEmergency;
    private RadioButton rdMale,rdFemale;
    private RadioGroup rgGender;
    private Spinner spnDiagCat,spnDiagCenter,spnDiagTest;

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

    private void init(View view) {
        txtDate=(TextView) view.findViewById(R.id.txtDate);
        edtPatientName= (MaterialEditText) view.findViewById(R.id.edtPatientName);
        edtPatientNo=(MaterialEditText) view.findViewById(R.id.edtPatientNo);
        edtAge=(MaterialEditText) view.findViewById(R.id.edtAge);
        edtRefer=(MaterialEditText) view.findViewById(R.id.edtRefer);

        btnRefernce=(Button)view.findViewById(R.id.btnRefernce);
        btnEmergency=(Button)view.findViewById(R.id.btnEmergency);
        rdMale= (RadioButton) view.findViewById(R.id.rdMale);
        rdFemale= (RadioButton) view.findViewById(R.id.rdFemale);
        rgGender= (RadioGroup) view.findViewById(R.id.rgGender);

        spnDiagCat= (Spinner) view.findViewById(R.id.spnDiagCat);
        spnDiagCenter= (Spinner) view.findViewById(R.id.spnDiagCenter);
        spnDiagTest= (Spinner) view.findViewById(R.id.spnDiagTest);


        setData();

    }

    private void setData() {

        txtDate.setText(new SimpleDateFormat("EE, MM-dd-yyyy").format(new Date()));

        List<String> list = new ArrayList<String>();
        list.add("Select Diagnostic center");
        list.add("Diagnostic center 1");
        list.add("Diagnostic center 2");
        list.add("Diagnostic center 3");
        list.add("Diagnostic center 4");
        list.add("Diagnostic center 5");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnDiagCenter.setAdapter(dataAdapter);

        List<String> list1 = new ArrayList<String>();
        list1.add("Select Diagnostic Test");
        list1.add("Diagnostic Test 1");
        list1.add("Diagnostic Test 2");
        list1.add("Diagnostic Test 3");
        list1.add("Diagnostic Test 4");
        list1.add("Diagnostic Test 5");

        ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, list1);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnDiagTest.setAdapter(dataAdapter1);
    }


}
