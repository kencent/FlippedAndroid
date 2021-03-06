package com.brzhang.fllipped.view

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.brzhang.fllipped.App
import com.brzhang.fllipped.R
import com.brzhang.fllipped.model.FlippedsResponse
import com.brzhang.fllipped.model.Flippedword
import com.brzhang.fllipped.pref.UserPref
import com.brzhang.fllipped.utils.LogUtil
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 *
 * Created by brzhang on 2017/7/9.
 * Description :
 */
class MineReceiveFragment : SqureFragment() {

    var mflippedId = ""

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun initRefreshLayout(mRefreshLayout: TwinklingRefreshLayout) {
        //super.initRefreshLayout(mRefreshLayout)
        mRefreshLayout.setOnRefreshListener(object : RefreshListenerAdapter() {
            override fun onRefresh(refreshLayout: TwinklingRefreshLayout?) {
                super.onRefresh(refreshLayout)
                mflippedId = ""
                askData()
            }

            override fun onLoadMore(refreshLayout: TwinklingRefreshLayout?) {
                super.onLoadMore(refreshLayout)
                askData()
            }
        })
    }

    override fun showOtherThings(holder: ViewHolder, flippedword: Flippedword?) {
        super.showOtherThings(holder, flippedword)
        if (flippedword?.status == 0) {
            holder.flippedText.setTextColor(ContextCompat.getColor(context, R.color.text_color_h2))
        } else {
            holder.flippedText.setTextColor(ContextCompat.getColor(context, R.color.textColorTertiary))
        }
    }

    override fun askFlippedList() {
        LogUtil.dLoge("hoolly", "receiver fragment load data")
        var params = HashMap<String, String>()
        val latLng = UserPref.getUserLocation(App.ApplicationContext())
        if (latLng != null) {
            params.put("lat", latLng?.lat!!.toString())
            params.put("lng", latLng?.lng!!.toString())
        }
        if (mflippedId != null) {
            params.put("id", mflippedId!!.toString())
        }
        fllippedNetService()
                .getReceivesFlippedwords(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Subscriber<FlippedsResponse>() {
                    override fun onCompleted() {
                        hideLoadingView()
                    }

                    override fun onError(e: Throwable) {
                        Log.e("hoolly", "error", e)
                        hideLoadingView()
                    }

                    override fun onNext(flippesResonse: FlippedsResponse) {
                        if (flippesResonse.flippedwords == null || flippesResonse.flippedwords?.size == 0) {
                            nomoreView(R.string.no_more_receive)
                            return
                        }
                        if (mflippedId.isEmpty()) {
                            showFlippedList(flippesResonse)
                        } else {
                            appendFlippedList(flippesResonse)
                        }
                        if (canLoadMore(flippesResonse)) {
                            refreshView?.setEnableLoadmore(true)
                        } else {
                            refreshView?.setEnableLoadmore(false)
                        }
                        nomoreView(R.string.no_more_receive)
                        extractMaxFlippedId(flippesResonse)
                    }
                })
    }

    private fun extractMaxFlippedId(fllippesResonse: FlippedsResponse) {
        mflippedId = fllippesResonse.flippedwords?.last()?.id.toString()
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