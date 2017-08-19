package com.brzhang.fllipped;

import android.os.Bundle;

import com.brzhang.fllipped.view.BaseFragment;

/**
 * Created by brzhang on 2017/8/19.
 * Description :
 */

public class TestFragment extends BaseFragment {

    public static TestFragment newInstance() {

        Bundle args = new Bundle();

        TestFragment fragment = new TestFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
