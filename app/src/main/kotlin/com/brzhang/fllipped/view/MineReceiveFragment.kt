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
class MineReceiveFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.fragment_mine_receive, container, false)
        setupView(view)
        initData()
        return view
    }

    private fun initData() {

    }

    private fun setupView(view: View?) {

    }

    companion object {
        fun newInstance(): MineReceiveFragment {

            val args = Bundle()

            val fragment = MineReceiveFragment()
            fragment.arguments = args
            return fragment
        }
    }

}