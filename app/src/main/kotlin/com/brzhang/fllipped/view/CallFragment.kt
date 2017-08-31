package com.brzhang.fllipped.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.brzhang.fllipped.R

/**
 *
 * Created by brzhang on 2017/8/31.
 * Description :
 */
open class CallFragment : BaseFragment() {

    var callTv:TextView? = null

    override fun onResume() {
        super.onResume()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.fragment_call, container, false)
        callTv = view?.findViewById(R.id.call_fragment_tv_call) as TextView
        callTv?.setOnClickListener {
            val intent = Intent(context,ContactActivity::class.java)
            startActivity(intent)
        }
        return view
    }

    companion object {

        fun newInstance(): CallFragment {

            val args = Bundle()

            val fragment = CallFragment()
            fragment.arguments = args
            return fragment
        }
    }
}