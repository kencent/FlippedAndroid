package com.brzhang.fllipped.view

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.brzhang.fllipped.FlippedHelper
import com.brzhang.fllipped.R
import com.brzhang.fllipped.model.FlippedsResponse
import com.brzhang.fllipped.model.Flippedword
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
class MineSendFragment : SqureFragment() {

    var mflippedId: String = ""

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

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun showDistanceOrReadState(holder: ViewHolder, flippedword: Flippedword?) {
        holder.flippedDistance.setCompoundDrawablesRelativeWithIntrinsicBounds(ContextCompat.getDrawable(context, R.drawable.ic_hearing_black_18dp), null, null, null)
        holder.flippedDistance.text = FlippedHelper.getReadState(flippedword)
    }

    override fun askFlippedList() {
        LogUtil.dLoge("hoolly", "send fragment load data")
        fllippedNetService()
                .getMypubFlippedwords(mflippedId)
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
                            nomoreView(R.string.no_more_send)
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
                        nomoreView(R.string.no_more_send)
                        mflippedId = flippesResonse.flippedwords?.last()?.id.toString()
                    }
                })
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