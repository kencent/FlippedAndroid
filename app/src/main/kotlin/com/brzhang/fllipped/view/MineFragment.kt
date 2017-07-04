package com.brzhang.fllipped.view

import android.os.Bundle
import android.support.v4.app.Fragment

/**
 *
 * Created by brzhang on 2017/7/3.
 * Description :
 */

class MineFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
    }

    companion object {

        fun newInstance(): MineFragment {

            val args = Bundle()

            val fragment = MineFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
