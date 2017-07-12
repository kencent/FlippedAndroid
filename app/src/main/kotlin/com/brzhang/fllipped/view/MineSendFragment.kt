package com.brzhang.fllipped.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.brzhang.fllipped.FlippedHelper
import com.brzhang.fllipped.model.FlippedsResponse
import com.brzhang.fllipped.model.Flippedword
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
        holder.flippedDistance.text = FlippedHelper.getReadState(flippedword)
    }

    override fun askFlippedList() {
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
                            return
                        }
                        if (mflippedId.isEmpty()) {
                            showFlippedList(flippesResonse)
                        } else {
                            appendFlippedList(flippesResonse)
                        }
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