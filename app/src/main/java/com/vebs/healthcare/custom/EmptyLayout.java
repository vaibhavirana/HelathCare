package com.vebs.healthcare.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vebs.healthcare.R;
import com.vebs.healthcare.utils.Function;


/**
 * Created by sagartahelyani on 20-06-2016.
 */
public class EmptyLayout extends LinearLayout {

    private Context context;
    private View rootView;
    private LayoutInflater inflater;

    private ImageView emptyImage;
    private TextView emptyTextView;

    public EmptyLayout(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public EmptyLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    private void init() {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        rootView = inflater.inflate(R.layout.layout_empty_view, this, true);

        emptyImage = (ImageView) rootView.findViewById(R.id.emptyImage);
        emptyTextView = (TextView) rootView.findViewById(R.id.emptyTextView);

        emptyTextView.setTypeface(Function.getRegularFont(context));
    }

    public void setType(int emptyType) {
        switch (emptyType) {
            case Function.NO_DOCTOR:
                emptyImage.setImageResource(R.drawable.ic_doctor);
                emptyTextView.setText(context.getString(R.string.no_doctor));
                break;

            case Function.NO_LAB:
                emptyImage.setImageResource(R.drawable.ic_doctor);
                emptyTextView.setText(context.getString(R.string.no_lab_test));
                break;

            case Function.NO_DIAG:
                emptyImage.setImageResource(R.drawable.ic_doctor);
                emptyTextView.setText(context.getString(R.string.no_diag_test));
                break;

            case Function.NO_PATIENT:
                emptyImage.setImageResource(R.drawable.ic_doctor);
                //emptyImage.setColorFilter(R.color.colorAccent, PorterDuff.Mode.SRC_ATOP);
                emptyTextView.setText(context.getString(R.string.no_patient_found));
                break;


        }
    }
}
