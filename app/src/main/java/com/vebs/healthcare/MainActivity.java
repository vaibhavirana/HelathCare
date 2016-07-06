package com.vebs.healthcare;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.gigamole.navigationtabbar.ntb.NavigationTabBar;
import com.vebs.healthcare.adapter.MyPagerAdapter;
import com.vebs.healthcare.fragment.DoctorFragment;

import java.util.ArrayList;
import java.util.Random;


public class MainActivity extends AppCompatActivity {

    private View rootView;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initToolbar();
        initUI();
        
    }

    private void initToolbar() {
        //rootView = findViewById(android.R.id.content);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setTitle(null);
        }

        TextView txtCity=(TextView)toolbar.findViewById(R.id.txtCity);
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
                .title("Select City")
                .items(R.array.cat_arrays)
               // .itemsIds(R.array.itemIds)
                //.typeface(Functions.getBoldFont(context), Functions.getRegularFont(context))
                .itemsCallbackSingleChoice(0, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                       /* if (listener != null) {
                            listener.onSelectDuration(which, text.toString());
                        }*/
                        dialog.dismiss();
                        return true;
                    }
                })
               /* .positiveText(R.string.save)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        if (selectSaveListener != null) {
                            selectSaveListener.onSave();
                        }
                    }
                })*/
                .show();
        dialog.setCancelable(true);
    }

    private void initUI() {
        final ViewPager viewPager = (ViewPager) findViewById(R.id.vp_horizontal_ntb);
        viewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));

        final NavigationTabBar navigationTabBar = (NavigationTabBar) findViewById(R.id.ntb_horizontal);
        final ArrayList<NavigationTabBar.Model> models = new ArrayList<>();
        models.add(
                new NavigationTabBar.Model
                        .Builder(
                        getResources().getDrawable(R.drawable.ic_action_doctor),0)
                        .title(getString(R.string.doctor))
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_second),
                        0)
                        .title(getString(R.string.labs))
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_third),
                        0)
                        .title(getString(R.string.diagnostic))
                        .build()
        );

        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_third),
                        0)
                        .title(getString(R.string.reference))
                        .build()
        );

        navigationTabBar.setModels(models);
        navigationTabBar.setViewPager(viewPager, 0);

        navigationTabBar.post(new Runnable() {
            @Override
            public void run() {
                final View viewPager = findViewById(R.id.vp_horizontal_ntb);
                ((ViewGroup.MarginLayoutParams) viewPager.getLayoutParams()).topMargin =
                        (int) -navigationTabBar.getBadgeMargin();
                viewPager.requestLayout();
            }
        });


    }


}
