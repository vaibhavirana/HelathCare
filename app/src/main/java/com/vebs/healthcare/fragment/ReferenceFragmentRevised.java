package com.vebs.healthcare.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vebs.healthcare.R;
import com.vebs.healthcare.utils.Function;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ReferenceFragmentRevised.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ReferenceFragmentRevised#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReferenceFragmentRevised extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private TabLayout tabLayout1;
    private ViewPager viewPager1;
    private ViewPagerAdapter1 adapter1;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View view;

    public ReferenceFragmentRevised() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ReferenceFragmentRevised newInstance(String param1, String param2) {
        ReferenceFragmentRevised fragment = new ReferenceFragmentRevised();
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
        view=inflater.inflate(R.layout.fragment_reference_revised, container, false);
        init();
        return view;
        
       // return inflater.inflate(R.layout.fragment_reference, container, false);
    }

    private void init() {
        tabLayout1 = (TabLayout) view.findViewById(R.id.tab_layout1);
        viewPager1 = (ViewPager) view.findViewById(R.id.pager1);
        setupViewPager(viewPager1);
        /*tabLayout1.post(new Runnable() {
            @Override
            public void run() {
                tabLayout1.setupWithViewPager(viewPager1);
            }
        });*/
        tabLayout1.setupWithViewPager(viewPager1);
        setupTabIcons();
        viewPager1.setOffscreenPageLimit(0);

    }

    private void setupTabIcons() {
        tabLayout1.getTabAt(0).setText(R.string.doctor);
        tabLayout1.getTabAt(1).setText(R.string.labs);
        tabLayout1.getTabAt(2).setText(R.string.diagnostic);
       // tabLayout.getTabAt(3).setText(R.string.reference);
        ViewGroup vg = (ViewGroup) tabLayout1.getChildAt(0);
        int tabsCount = vg.getChildCount();
        for (int j = 0; j < tabsCount; j++) {
            ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);
            int tabChildsCount = vgTab.getChildCount();
            for (int i = 0; i < tabChildsCount; i++) {
                View tabViewChild = vgTab.getChildAt(i);
                if (tabViewChild instanceof TextView) {
                    ((TextView) tabViewChild).setTypeface(Function.getRegularFont(getActivity()));
                }
            }
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter1 = new ViewPagerAdapter1(getActivity().getSupportFragmentManager());
        adapter1.addFrag(new PatientDoctorFragment(), getResources().getString(R.string.doctor));
        adapter1.addFrag(new PatientLabFragment(), getResources().getString(R.string.labs));
        adapter1.addFrag(new PatientDiagFragment(), getResources().getString(R.string.diagnostic));
        //adapter.addFrag(new ReferenceFragmentRevised(), getResources().getString(R.string.reference));

        viewPager.setAdapter(adapter1);
        viewPager.setCurrentItem(0);
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    class ViewPagerAdapter1 extends FragmentStatePagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter1(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
