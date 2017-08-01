package com.brzhang.fllipped.view

import android.os.Bundle
import android.support.annotation.IdRes
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.afollestad.materialdialogs.MaterialDialog
import com.brzhang.fllipped.FlippedHelper
import com.brzhang.fllipped.R
import com.brzhang.fllipped.model.FlippedsResponse
import com.brzhang.fllipped.model.Flippedword
import com.brzhang.fllipped.pref.UserPref
import com.brzhang.fllipped.utils.LogUtil
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout
import com.lcodecore.tkrefreshlayout.header.progresslayout.ProgressLayout
import okhttp3.ResponseBody
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.util.ArrayList

/**
 * Created by brzhang on 2017/7/3.
 * Description :
 */

open class SqureFragment : BaseFragment() {


    private var mAdapter: SqureFllippedAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.fragment_squre, container, false)
        setupView(view)
        showLoadingView()
//        askData() fixme 延迟加载
        return view
    }

    private var refreshView: TwinklingRefreshLayout? = null

    private var nomoreView: TextView? = null

    protected fun setupView(view: View?) {
        refreshView = view?.findViewById(R.id.fragment_squre_recycler_view_refresh) as TwinklingRefreshLayout
        refreshView?.setHeaderView(ProgressLayout(context))
//        refreshView?.setBottomView(LoadingView(context))
        initRefreshLayout(refreshView!!)
        var recyclerView = view?.findViewById(R.id.fragment_squre_recycler_view) as RecyclerView
        nomoreView = view?.findViewById(R.id.fragment_squre_no_more_view) as TextView
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        mAdapter = SqureFllippedAdapter()
        recyclerView.adapter = mAdapter
    }

    open fun nomoreView(@IdRes text: Int) {
        if (mAdapter?.itemCount == 0) {
            showNoMoreView(text)
        } else {
            hideNomoreView()
        }
    }

    private fun showNoMoreView(@IdRes text: Int) {
        nomoreView?.visibility = View.VISIBLE
        nomoreView?.text = getString(text)
    }

    private fun hideNomoreView() {
        nomoreView?.visibility = View.GONE
    }

    open fun initRefreshLayout(mRefreshLayout: TwinklingRefreshLayout) {
        mRefreshLayout.setEnableLoadmore(false)
        mRefreshLayout.setOnRefreshListener(object : RefreshListenerAdapter() {
            override fun onRefresh(refreshLayout: TwinklingRefreshLayout?) {
                super.onRefresh(refreshLayout)
                askData()
            }

            override fun onLoadMore(refreshLayout: TwinklingRefreshLayout?) {
                super.onLoadMore(refreshLayout)
            }
        })

    }

    protected fun askData() {
        askFlippedList()
    }

    protected open fun showDistanceOrReadState(holder: ViewHolder, flippedword: Flippedword?) {
        if (TextUtils.isEmpty(FlippedHelper.getDistance(flippedword))) {
            holder.flippedDistance.visibility = View.GONE
            holder.flippedDistanceDivider.visibility = View.GONE
        } else {
            holder.flippedDistanceDivider.visibility = View.VISIBLE
            holder.flippedDistance.visibility = View.VISIBLE
            holder.flippedDistance.text = "距离：${FlippedHelper.getDistance(flippedword)}"
        }
    }

    open fun askFlippedList() {
        LogUtil.dLoge("hoolly", "squre fragment load data")
        var params = HashMap<String, Double>()
        val latLng = UserPref.getUserLocation(activity)
        if (latLng != null) {
            params.put("lat", latLng?.lat!!)
            params.put("lng", latLng?.lng!!)
        }
        fllippedNetService()
                .getNearByFlippeds(params)
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
                        showFlippedList(flippesResonse)
                    }
                })
    }

    fun showFlippedList(fllippesResonse: FlippedsResponse) {
        mAdapter?.fllippeds = fllippesResonse.flippedwords
        mAdapter?.notifyDataSetChanged()
    }

    fun appendFlippedList(flippesResonse: FlippedsResponse) {
        var list = mutableListOf<Flippedword>()
        list.addAll(mAdapter?.fllippeds!!)
        list.addAll(flippesResonse.flippedwords!!)
        mAdapter?.fllippeds = list
        mAdapter?.notifyDataSetChanged()
    }


    protected fun hideLoadingView() {
//        if (activity is MainActivity) {
//            (activity as MainActivity).hideProgressBar()
//        }
        refreshView?.finishRefreshing()
        refreshView?.finishLoadmore()
    }

    protected fun showLoadingView() {
//        if (activity is MainActivity) {
//            (activity as MainActivity).showProgressBar()
//        }
        refreshView?.startRefresh()
    }


    override fun onResume() {
        super.onResume()
    }

    companion object {

        fun newInstance(): SqureFragment {

            val args = Bundle()

            val fragment = SqureFragment()
            fragment.arguments = args
            return fragment
        }
    }


    private inner class SqureFllippedAdapter : RecyclerView.Adapter<ViewHolder>() {


        var fllippeds: List<Flippedword>? = ArrayList()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_squre_item, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.flippedText.text = fllippeds?.get(position)?.contents?.get(0)?.text
            holder.flippedSento.text = "发送给：${fllippeds?.get(position)?.sendto.toString()}"
            showDistanceOrReadState(holder, fllippeds?.get(position))
            if (!FlippedHelper.getPic(fllippeds?.get(position)).isEmpty()) {
                holder.tagPic.visibility = View.VISIBLE
            } else {
                holder.tagPic.visibility = View.GONE
            }
            if (!FlippedHelper.getVoice(fllippeds?.get(position)).isEmpty()) {
                holder.tagVoice.visibility = View.VISIBLE
            } else {
                holder.tagVoice.visibility = View.GONE
            }
            if (!FlippedHelper.getVideo(fllippeds?.get(position)).isEmpty()) {
                holder.tagVideo.visibility = View.VISIBLE
            } else {
                holder.tagVideo.visibility = View.GONE
            }
            holder.itemView.tag = fllippeds?.get(position)?.id
            holder.itemView.setOnClickListener({
                view ->
                startDetailActivity(view.tag.toString())
            })
            val fliipped = fllippeds?.get(position)
            holder.itemView.setOnLongClickListener({
                if (canDelete(fliipped)) {
                    MaterialDialog.Builder(context)
                            .title("提示")
                            .content("确认删除该条心动的话？")
                            .positiveText("确定")
                            .negativeText("取消")
                            .onPositive { dialog, which ->
                                deleteFlippedWords(fliipped)
                            }
                            .show()
                }
                true
            })
        }

        override fun getItemCount(): Int {
            return if (fllippeds == null) 0 else fllippeds!!.size
        }
    }

    private fun canDelete(fliipped: Flippedword?): Boolean {
        if (fliipped?.links == null || fliipped?.links!!.isEmpty()) {
            return false
        }
        return fliipped.links!!.any { it.method.toUpperCase().equals("DELETE") }
    }

    private fun deleteFlippedWords(fliipped: Flippedword?) {
        showLoadingView()
        fllippedNetService().deleteFllipped(fliipped?.id ?: 0)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Subscriber<ResponseBody>() {
                    override fun onCompleted() {
                        hideLoadingView()
                    }

                    override fun onError(e: Throwable) {
                        Log.e("hoolly", "error", e)
                        hideLoadingView()
                    }

                    override fun onNext(responseBody: ResponseBody) {
                        //todo 这里处理删除逻辑，移除改条
                        toast("删除flipped成功")
                    }
                })
    }

    private fun startDetailActivity(flippedId: String) {
        DetailActivity.startMe(context, flippedId)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val flippedText: TextView = itemView.findViewById(R.id.flipped_item_tv_text) as TextView
        val flippedSento: TextView = itemView.findViewById(R.id.flipped_item_tv_send_to) as TextView
        val flippedDistance: TextView = itemView.findViewById(R.id.flipped_item_tv_send_distance) as TextView
        val flippedDistanceDivider: ImageView = itemView.findViewById(R.id.flipped_item_iv_divider) as ImageView
        val tagPic: ImageView = itemView.findViewById(R.id.flipped_item_iv_tag_pic) as ImageView
        val tagVoice: ImageView = itemView.findViewById(R.id.flipped_item_iv_tag_voice) as ImageView
        val tagVideo: ImageView = itemView.findViewById(R.id.flipped_item_iv_tag_video) as ImageView

    }

    fun fragmentSelected() {
        askData()
    }
}


