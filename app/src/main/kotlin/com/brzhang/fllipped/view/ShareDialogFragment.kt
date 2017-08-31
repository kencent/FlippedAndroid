package com.brzhang.fllipped.view

import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.brzhang.fllipped.R
import com.brzhang.fllipped.utils.ShareUtils

/**
 * Created by hoollyzhang on 2017/8/22.
 */
class ShareDialogFragment : BottomSheetDialogFragment(), View.OnClickListener {

    var url: String? = null
    var title: String? = null
    var description: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.share_fragment, container, false);
        setupView(view)
        url = arguments.getString(KEY_SHARE_URL)
        title = arguments.getString(KEY_SHARE_TITLE)
        description = arguments.getString(KEY_SHARE_DESCRIPTION)
        return view
    }

    private fun setupView(view: View?) {
        view?.findViewById(R.id.share_wx_friends)?.setOnClickListener(this)
        view?.findViewById(R.id.share_wx_moment)?.setOnClickListener(this)
        view?.findViewById(R.id.share_cancel)?.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.share_wx_friends -> {
                ShareUtils.shareFriends(context, url ?: "", title ?: "", description ?: "")
            }
            R.id.share_wx_moment -> {
                ShareUtils.shareMoment(context, url ?: "", description ?: "", title ?: "")
            }
            R.id.share_cancel -> {
                this.dismiss()
            }
        }
    }

    companion object {
        val KEY_SHARE_URL = "key_share_url"
        val KEY_SHARE_TITLE = "key_share_title"
        val KEY_SHARE_DESCRIPTION = "key_share_description"

        fun newInstance(url: String, title: String, description: String): ShareDialogFragment {

            val args = Bundle()
            args.putString(KEY_SHARE_URL, url)
            args.putString(KEY_SHARE_TITLE, title)
            args.putString(KEY_SHARE_DESCRIPTION, description)
            val fragment = ShareDialogFragment()
            fragment.arguments = args
            return fragment
        }
    }

}