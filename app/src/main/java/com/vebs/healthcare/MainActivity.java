package com.vebs.healthcare;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.gigamole.navigationtabbar.ntb.NavigationTabBar;
import com.vebs.healthcare.adapter.MyPagerAdapter;
import com.vebs.healthcare.fragment.DiagnosticFragment;
import com.vebs.healthcare.fragment.DoctorFragment;
import com.vebs.healthcare.fragment.LabFragment;
import com.vebs.healthcare.fragment.ReferenceFragment;
import com.vebs.healthcare.utils.Function;
import com.vebs.healthcare.utils.Prefs;
import com.vebs.healthcare.utils.PrefsUtil;
import com.vebs.healthcare.utils.RestClient;

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
import java.util.ArrayList;
import java.util.List;

import static com.vebs.healthcare.R.id.txtSelectCategory;


public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;
    private Toolbar toolbar;
    private int cityWhich = 0;
    private TextView txtCity;
    private TextView txtName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initToolbar();
        initUI();
    }

    private void initUI() {
        //Log.e("city",city_list.toString());
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        viewPager = (ViewPager) findViewById(R.id.pager);
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();
        viewPager.setOffscreenPageLimit(0);
    }

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setText(R.string.doctor);
        tabLayout.getTabAt(1).setText(R.string.labs);
        tabLayout.getTabAt(2).setText(R.string.diagnostic);
        tabLayout.getTabAt(3).setText(R.string.reference);
        /*ViewGroup vg = (ViewGroup) tabLayout.getChildAt(0);
        int tabsCount = vg.getChildCount();
        for (int j = 0; j < tabsCount; j++) {
            ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);
            int tabChildsCount = vgTab.getChildCount();
            for (int i = 0; i < tabChildsCount; i++) {
                View tabViewChild = vgTab.getChildAt(i);
                if (tabViewChild instanceof TextView) {
                    ((TextView) tabViewChild).setTypeface(Functions.getTypeFace(getActivity()));
                }
            }
        }*/
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(this.getSupportFragmentManager());
        adapter.addFrag(new DoctorFragment(), getResources().getString(R.string.doctor));
        adapter.addFrag(new LabFragment(), getResources().getString(R.string.labs));
        adapter.addFrag(new DiagnosticFragment(), getResources().getString(R.string.diagnostic));
        adapter.addFrag(new ReferenceFragment(), getResources().getString(R.string.reference));

        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);
    }

    class ViewPagerAdapter extends FragmentStatePagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
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

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setTitle(null);
        }

        txtCity = (TextView) toolbar.findViewById(R.id.txtCity);
        txtName = (TextView) toolbar.findViewById(R.id.txtName);
        txtName.setText(PrefsUtil.getDrName(this));
        if (!PrefsUtil.getCity(this).isEmpty()) {
            txtCity.setText(PrefsUtil.getCity(this));
        }else
        {
            showPopup();
        }
        txtCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup();
            }
        });
        setSupportActionBar(toolbar);
    }

    private void showPopup() {
        MaterialDialog dialog = new MaterialDialog.Builder(this)
                .title(this.getString(R.string.select_city))
                .items(Function.city_list)
                .itemsCallbackSingleChoice(cityWhich, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        dialog.dismiss();
                        cityWhich = which;
                        txtCity.setText(Function.city_list.get(which));
                        //cityId = Function.city_list_id.get(which);
                        Log.e("city",Function.city_list.get(which));
                        PrefsUtil.setCity(MainActivity.this, Function.city_list.get(which));
                        PrefsUtil.setCityID(MainActivity.this, Function.city_list_id.get(which));
                        return true;
                    }
                })
                // .itemsIds(R.array.itemIds)
                //.typeface(Functions.getBoldFont(context), Functions.getRegularFont(context))
                .positiveText(android.R.string.ok)
                .show();

        dialog.setCancelable(true);
    }




}
