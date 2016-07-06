package com.vebs.healthcare.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import com.vebs.healthcare.R;
import com.vebs.healthcare.adapter.PatientAdapter;

import java.sql.Ref;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ReferenceFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ReferenceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReferenceFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Button btnNotConsulted,btnConsulted;
    //private AutoCompleteTextView txtSearch;
    private TextView txtSearch;
    private RecyclerView rvPatientName;
    public ReferenceFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ReferenceFragment newInstance(String param1, String param2) {
        ReferenceFragment fragment = new ReferenceFragment();
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
        View view=inflater.inflate(R.layout.fragment_reference, container, false);
        init(view);
        return view;
        
       // return inflater.inflate(R.layout.fragment_reference, container, false);
    }

    private void init(View view) {

        btnConsulted=(Button)view.findViewById(R.id.btnConsulted);
        btnNotConsulted=(Button)view.findViewById(R.id.btnNotConsulted);
        txtSearch= (TextView) view.findViewById(R.id.txtSearch);
        rvPatientName=(RecyclerView)view.findViewById(R.id.rvPatientName);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rvPatientName.setLayoutManager(layoutManager);
       // rvPatientName.addItemDecoration(new VerticalSpaceItemDecoration(2));
        fetchPatientList();

    }

    private void fetchPatientList() {
        ArrayList<String> list = new ArrayList<String>();
        //list.add("Select Lab");
        for(int i=1;i<=5;i++) {
            list.add("("+i+") Patient " + i);
        }
        PatientAdapter adpt=new PatientAdapter(getActivity(),list);
        rvPatientName.setAdapter(adpt);


    }

    // TODO: Rename method, update argument and hook method into UI event

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
