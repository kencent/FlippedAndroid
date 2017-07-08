package com.brzhang.fllipped.utils;

import android.util.Log;

import com.brzhang.fllipped.BuildConfig;

/**
 * Created by brzhang on 2017/7/8.
 * Description :
 */

public class LogUtil {

    public static void dLoge(String tag, String message) {
        if (BuildConfig.DEBUG) {
            Log.e(tag, message);
        }
    }
}
