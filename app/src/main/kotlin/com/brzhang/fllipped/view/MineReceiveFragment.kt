package com.brzhang.fllipped.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cn.bingoogolapple.refreshlayout.BGARefreshLayout
import com.brzhang.fllipped.model.FlippedsResponse
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
    override fun onBGARefreshLayoutBeginLoadingMore(refreshLayout: BGARefreshLayout?): Boolean {
        initData()
        return true
    }

    override fun onBGARefreshLayoutBeginRefreshing(refreshLayout: BGARefreshLayout?) {
        mflippedId = ""
        initData()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun askFlippedList() {
        fllippedNetService()
                .getReceivesFlippedwords(mflippedId)
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
                        if (mflippedId.isEmpty()){
                            showFlippedList(flippesResonse)
                        }else{
                            appendFlippedList(flippesResonse)
                        }

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