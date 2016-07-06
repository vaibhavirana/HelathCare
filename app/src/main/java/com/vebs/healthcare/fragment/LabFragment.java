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

import com.afollestad.materialdialogs.MaterialDialog;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.vebs.healthcare.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LabFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LabFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LabFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

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
        View view=inflater.inflate(R.layout.fragment_lab, container, false);
        init(view);
        return view;
    }

    private MaterialEditText edtPatientName,edtPatientNo,edtAge,edtRefer;
    private TextView txtDate;
    private Button btnRefernce,btnHomeCollected,btnCenterCollected;
    private RadioButton rdMale,rdFemale;
    private RadioGroup rgGender;
    private Spinner spnLab,spnLabTest;


    // TODO: Rename method, update argument and hook method into UI event

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void init(View view) {
        txtDate=(TextView) view.findViewById(R.id.txtDate);
        edtPatientName= (MaterialEditText) view.findViewById(R.id.edtPatientName);
        edtPatientNo=(MaterialEditText) view.findViewById(R.id.edtPatientNo);
        edtAge=(MaterialEditText) view.findViewById(R.id.edtAge);
        edtRefer=(MaterialEditText) view.findViewById(R.id.edtRefer);

        btnRefernce=(Button)view.findViewById(R.id.btnRefernce);
        btnHomeCollected=(Button)view.findViewById(R.id.btnHomeCollected);
        btnCenterCollected=(Button)view.findViewById(R.id.btnCenterCollected);
        rdMale= (RadioButton) view.findViewById(R.id.rdMale);
        rdFemale= (RadioButton) view.findViewById(R.id.rdFemale);
        rgGender= (RadioGroup) view.findViewById(R.id.rgGender);

        spnLab= (Spinner) view.findViewById(R.id.spnLab);
        spnLabTest= (Spinner) view.findViewById(R.id.spnLabTest);

        setData();

    }

    private void setData() {

        txtDate.setText(new SimpleDateFormat("EE, MM-dd-yyyy").format(new Date()));

        List<String> list = new ArrayList<String>();
        list.add("Select Lab");
        list.add("Lab 1");
        list.add("Lab 2");
        list.add("Lab 3");
        list.add("Lab 4");
        list.add("Lab 5");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnLab.setAdapter(dataAdapter);

        List<String> list1 = new ArrayList<String>();
        list1.add("Select Lab");
        list1.add("Lab Test 1");
        list1.add("Lab Test 2");
        list1.add("Lab Test 3");
        list1.add("Lab Test 4");
        list1.add("Lab Test 5");

        ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, list1);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnLabTest.setAdapter(dataAdapter1);

    }

}
