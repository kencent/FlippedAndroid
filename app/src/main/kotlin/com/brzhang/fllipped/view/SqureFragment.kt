package com.brzhang.fllipped.view

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import cn.bingoogolapple.refreshlayout.BGARefreshLayout
import cn.bingoogolapple.refreshlayout.BGARefreshViewHolder
import cn.bingoogolapple.refreshlayout.BGAStickinessRefreshViewHolder
import com.brzhang.fllipped.FlippedHelper
import com.brzhang.fllipped.R
import com.brzhang.fllipped.model.FlippedsResponse
import com.brzhang.fllipped.model.Flippedword
import com.brzhang.fllipped.pref.UserPref
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.util.ArrayList

/**
 * Created by brzhang on 2017/7/3.
 * Description :
 */

open class SqureFragment : BaseFragment(), BGARefreshLayout.BGARefreshLayoutDelegate {
    override fun onBGARefreshLayoutBeginLoadingMore(refreshLayout: BGARefreshLayout?): Boolean {
        return false
    }

    override fun onBGARefreshLayoutBeginRefreshing(refreshLayout: BGARefreshLayout?) {
        initData()
    }


    private var mAdapter: SqureFllippedAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.fragment_squre, container, false)
        setupView(view)
        initData()
        return view
    }

    private var refreshView: BGARefreshLayout? = null

    protected fun setupView(view: View?) {
        refreshView = view?.findViewById(R.id.fragment_squre_recycler_view_refresh) as BGARefreshLayout
        initRefreshLayout(refreshView!!)
        var recyclerView = view?.findViewById(R.id.fragment_squre_recycler_view) as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        mAdapter = SqureFllippedAdapter()
        recyclerView.adapter = mAdapter
    }

    fun initRefreshLayout(mRefreshLayout: BGARefreshLayout) {
        // 为BGARefreshLayout 设置代理
        mRefreshLayout.setDelegate(this)
        // 设置下拉刷新和上拉加载更多的风格     参数1：应用程序上下文，参数2：是否具有上拉加载更多功能
        var refreshViewHolder = BGAStickinessRefreshViewHolder(context, true)
        refreshViewHolder.setRotateImage(R.drawable.ic_refresh)
        refreshViewHolder.setStickinessColor(R.color.colorAccent)
        // 设置下拉刷新和上拉加载更多的风格
        mRefreshLayout.setRefreshViewHolder(refreshViewHolder)


        // 为了增加下拉刷新头部和加载更多的通用性，提供了以下可选配置选项  -------------START
        // 设置正在加载更多时不显示加载更多控件
        // mRefreshLayout.setIsShowLoadingMoreView(false);
        // 设置正在加载更多时的文本
        refreshViewHolder.setLoadingMoreText("正在加载数据")
        // 设置整个加载更多控件的背景颜色资源 id
//        refreshViewHolder.setLoadMoreBackgroundColorRes(loadMoreBackgroundColorRes)
        // 设置整个加载更多控件的背景 drawable 资源 id
//        refreshViewHolder.setLoadMoreBackgroundDrawableRes(loadMoreBackgroundDrawableRes)
        // 设置下拉刷新控件的背景颜色资源 id
//        refreshViewHolder.setRefreshViewBackgroundColorRes(refreshViewBackgroundColorRes)
        // 设置下拉刷新控件的背景 drawable 资源 id
//        refreshViewHolder.setRefreshViewBackgroundDrawableRes(refreshViewBackgroundDrawableRes)
        // 设置自定义头部视图（也可以不用设置）     参数1：自定义头部视图（例如广告位）， 参数2：上拉加载更多是否可用
//        mRefreshLayout.setCustomHeaderView(mBanner, false)
        // 可选配置  -------------END
    }

    protected fun initData() {
        showLoadingView()
        askFlippedList()
    }

    open fun askFlippedList() {

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
        refreshView?.endRefreshing()
        //refreshView?.endLoadingMore()
    }

    protected fun showLoadingView() {
//        if (activity is MainActivity) {
//            (activity as MainActivity).showProgressBar()
//        }
        refreshView?.beginRefreshing()
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
            holder.flippedDistance.text = "距离：${FlippedHelper.getDistance(fllippeds?.get(position))}"
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
        }

        override fun getItemCount(): Int {
            return if (fllippeds == null) 0 else fllippeds!!.size
        }
    }

    private fun startDetailActivity(flippedId: String) {
        DetailActivity.startMe(context, flippedId)
    }

    private inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val flippedText: TextView = itemView.findViewById(R.id.flipped_item_tv_text) as TextView
        val flippedSento: TextView = itemView.findViewById(R.id.flipped_item_tv_send_to) as TextView
        val flippedDistance: TextView = itemView.findViewById(R.id.flipped_item_tv_send_distance) as TextView
        val tagPic: ImageView = itemView.findViewById(R.id.flipped_item_iv_tag_pic) as ImageView
        val tagVoice: ImageView = itemView.findViewById(R.id.flipped_item_iv_tag_voice) as ImageView
        val tagVideo: ImageView = itemView.findViewById(R.id.flipped_item_iv_tag_video) as ImageView

    }
}


