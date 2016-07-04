package com.vebs.healthcare.utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.StringRes;
import android.support.v7.app.AlertDialog;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

public class UIUtil {
    public static void setFont(Activity a, TextView txtView, String font) {
        Typeface type = Typeface.createFromAsset(a.getAssets(), font);
        txtView.setTypeface(type);
    }

    public static boolean hasJellyBean() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
    }

    public static boolean hasKitKat() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    }

    public static boolean hasLoliPop() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    public static void showAlertDialog(final Context context, String msg, final boolean isFinish) {
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setMessage(msg);

        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (isFinish) {
                    Activity activity = (Activity) context;
                    activity.finish();
                }
            }
        });
        alert.setCancelable(false);
        alert.show();
    }


    public static void showAlertDialog(final Context context, @StringRes int id, final boolean isFinish) {
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setMessage(context.getResources().getString(id));

        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (isFinish) {
                    Activity activity = (Activity) context;
                    activity.finish();
                }
            }
        });
        alert.setCancelable(false);
        alert.show();
    }

    public static void showAlertDialog(final Context context, String title, String msg, String negativeButtonName, final AlertDialogListener alertDialogListener) {
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setMessage(msg);
        alert.setTitle(title);
        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                alertDialogListener.positiveButtonClick();
            }
        });
        alert.setNegativeButton(negativeButtonName, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                alertDialogListener.negativeButtonClick();
            }
        });
        alert.setCancelable(false);
        alert.show();
    }


    public static void startActivity(Context activity, Class<?> cls, boolean isFlagAdd) {

        Intent intent = new Intent(activity, cls);
        if (isFlagAdd) {
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        activity.startActivity(intent);
    }

    public static void startActivity(Context activity, Intent intent, boolean isFinish) {

        if (intent == null) {
            return;
        }

        if (isFinish) {

            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        activity.startActivity(intent);
    }


    public static String editTextToString(EditText editText) {

        return editText.getText().toString().trim();

    }

    public static boolean emailValidation(String email) {
        if (TextUtils.isEmpty(email)) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
        }
    }

    public static void setActivityToFullScreen(Activity splashScreenActivity) {
        splashScreenActivity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        splashScreenActivity.requestWindowFeature(Window.FEATURE_NO_TITLE);
    }


    public interface AlertDialogListener {
        void positiveButtonClick();
        void negativeButtonClick();
    }

    public interface OnDeleteListener {
        void onDelete(int id);
    }


    public static DisplayMetrics getDeviceMetrics(Activity context) {
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        display.getMetrics(metrics);
        return metrics;
    }


}
