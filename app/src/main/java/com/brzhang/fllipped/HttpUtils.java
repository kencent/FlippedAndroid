package com.brzhang.fllipped;

import com.brzhang.fllipped.utils.LogUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.adapter.rxjava.HttpException;

/**
 * 用于处理http请求相关的一些东西.
 * <p>
 * 放在这个package下不一定合适
 * <p>
 * Created by kingcmchen on 2017/3/23.
 */

public class HttpUtils {
    private static final String TAG = "HttpUtils";

    private HttpUtils() {
    }

    public static String getError(Throwable t) {
        return getError(t, "请稍候重试");
    }

    public static String getError(Throwable t, String defVal) {
        if (t instanceof HttpException) {
            HttpException e = (HttpException) t;
            String error = null;
            try {
                String message = e.response().errorBody().string();
                JSONObject json = new JSONObject(message);
                error = json.optString("err");
            } catch (JSONException e1) {
                LogUtil.dLoge(TAG, e1.getMessage());
            } catch (IOException e2) {
                LogUtil.dLoge(TAG, e2.getMessage());
            }
            if (error == null) {
                error = defVal;
            }
            return error;
        }
        return defVal;
    }
}
