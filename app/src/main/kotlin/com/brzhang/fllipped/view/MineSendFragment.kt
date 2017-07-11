package com.brzhang.fllipped.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cn.bingoogolapple.refreshlayout.BGARefreshLayout
import com.brzhang.fllipped.FlippedHelper
import com.brzhang.fllipped.model.FlippedsResponse
import com.brzhang.fllipped.model.Flippedword
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 *
 * Created by brzhang on 2017/7/9.
 * Description :
 */
class MineSendFragment : SqureFragment() {

    var lastId: String = ""
    override fun onBGARefreshLayoutBeginLoadingMore(refreshLayout: BGARefreshLayout?): Boolean {
        initData()
        return true
    }

    override fun onBGARefreshLayoutBeginRefreshing(refreshLayout: BGARefreshLayout?) {
        lastId = ""
        initData()
    }


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun showDistanceOrReadState(holder: ViewHolder, flippedword: Flippedword?) {
        holder.flippedDistance.text = FlippedHelper.getReadState(flippedword)
    }

    override fun askFlippedList() {
        fllippedNetService()
                .getMypubFlippedwords(lastId)
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
                        if (lastId.isEmpty()) {
                            showFlippedList(flippesResonse)
                        } else {
                            appendFlippedList(flippesResonse)
                        }
                        lastId = flippesResonse.flippedwords?.last()?.id.toString()
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