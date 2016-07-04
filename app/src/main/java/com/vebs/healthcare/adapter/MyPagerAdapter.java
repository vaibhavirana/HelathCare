package com.vebs.healthcare.adapter;

import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.vebs.healthcare.fragment.DiagnosticFragment;
import com.vebs.healthcare.fragment.DoctorFragment;
import com.vebs.healthcare.fragment.LabFragment;

/**
 * Created by vraj on 7/2/2016.
 */

public class MyPagerAdapter extends FragmentPagerAdapter {

    public MyPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int pos) {
        switch(pos) {

            case 0: return DoctorFragment.newInstance("FirstFragment", "Instance 1");
            case 1: return LabFragment.newInstance("SecondFragment", "Instance 1");
            case 2: return DiagnosticFragment.newInstance("ThirdFragment", "Instance 1");
            case 3: return DiagnosticFragment.newInstance("ThirdFragment", "Instance 1");
        }
        return null;
    }

    @Override
    public int getCount() {
        return 4;
    }
}
