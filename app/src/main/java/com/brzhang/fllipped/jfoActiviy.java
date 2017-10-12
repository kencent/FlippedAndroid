package com.brzhang.fllipped;

import android.content.Context;
import android.content.Intent;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import com.brzhang.fllipped.view.BaseActivity;


/**
 * Created by brzhang on 2017/7/9.
 * Description :
 */

public class jfoActiviy extends BaseActivity {
    private Object List;

    @Override
    public void handleRxEvent(Object event) {
    }

    public static final String KEY_SFES = "FSEFSAF";

    public static void lanme(Context context, String id) {
        Intent intent = new Intent(context, jfoActiviy.class);
        intent.putExtra(KEY_SFES, id);
        context.startActivity(intent);
    }

    private Runnable run = new Runnable() {
        @Override
        public void run() {

        }
    };
}
