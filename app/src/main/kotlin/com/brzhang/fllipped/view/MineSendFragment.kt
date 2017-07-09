package com.brzhang.fllipped.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.brzhang.fllipped.R

/**
 *
 * Created by brzhang on 2017/7/9.
 * Description :
 */
class MineSendFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.fragment_mine_send, container, false)
        setupView(view)
        initData()
        return view
    }

    private fun initData() {

    }

    private fun setupView(view: View?) {

    }


    companion object {

        fun newInstance(): MineSendFragment {

            val args = Bundle()

            val fragment = MineSendFragment()
            fragment.arguments = args
            return fragment
        }
    }

}