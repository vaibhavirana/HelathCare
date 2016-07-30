package com.vebs.healthcare;

import android.app.Application;

import com.vebs.healthcare.utils.Function;

/**
 * Created by priyasindkar on 28-03-2016.
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

       /* CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                        .setDefaultFontPath(Function.fontFamilyPathThin)
                        .setFontAttrId(R.attr.fontPath)
                        .build()
        );
*/

    }

}
