package com.brzhang.fllipped.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.brzhang.fllipped.R

/**
 *
 * Created by brzhang on 2017/8/19.
 * Description :
 */
class IntroduceFragment : BaseFragment() {

    var drawableRes: Int? = null
    var needShowPass: Boolean? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//        return super.onCreateView(inflater, container, savedInstanceState)
        drawableRes = arguments.getInt(KEY_DRAWABLE)
        needShowPass = arguments.getBoolean(KEY_NEED_SHOW_PASS)
        val view = inflater?.inflate(R.layout.fragment_introduce, container, false)
        val imageView: ImageView = view?.findViewById(R.id.fragment_introduct_iv) as ImageView
        if (drawableRes != null) {
            imageView.setImageResource(drawableRes!!)
        }

        if (needShowPass == true) {
            val nextTv = view.findViewById(R.id.fragment_introduct_tv_next) as TextView
            nextTv.visibility = View.VISIBLE
            nextTv.setOnClickListener {
                activity.finish()
            }
        }
        return view
    }

    companion object {
        val KEY_DRAWABLE = "KEY_DRAWABLE"
        val KEY_NEED_SHOW_PASS = "KEY_NEED_SHOW_PASS"

        fun newInstance(drawable: Int, needShowPass: Boolean): IntroduceFragment {

            val args = Bundle()
            args.putInt(KEY_DRAWABLE, drawable)
            args.putBoolean(KEY_NEED_SHOW_PASS, needShowPass)
            val fragment = IntroduceFragment()
            fragment.arguments = args
            return fragment
        }
    }

}