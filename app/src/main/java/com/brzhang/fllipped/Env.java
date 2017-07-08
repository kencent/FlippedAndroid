package com.brzhang.fllipped;

import android.content.Context;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.support.v4.util.ArrayMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by hoolly on 16/7/1.
 */
public class Env {
    private static Resources sRes;

    public static void initEnv(Context context) {
        sRes = context.getResources();
    }

    public static String getHttpUrl() {

        return sRes.getString(R.string.http_url);
    }


}
