package com.brzhang.fllipped.view

import android.os.Bundle
import android.support.v4.widget.ContentLoadingProgressBar
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.brzhang.fllipped.FlippedHelper
import com.brzhang.fllipped.R
import com.brzhang.fllipped.model.FlippedsResponse
import com.brzhang.fllipped.model.Flippedword
import com.wang.avi.AVLoadingIndicatorView
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.util.ArrayList

/**
 * Created by brzhang on 2017/7/3.
 * Description :
 */

class SqureFragment : BaseFragment() {


    private var mAdapter:SqureFllippedAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.fragment_squre, container, false)
        setupView(view)
        initData()
        return view
    }

    private fun setupView(view: View?) {
        var recyclerView = view?.findViewById(R.id.fragment_squre_recycler_view) as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
        mAdapter = SqureFllippedAdapter()
        recyclerView.adapter = mAdapter
    }

    private fun initData() {
                showLoadingView()
                fllippedNetService()
                .getNearByFlippeds(HashMap<String,String>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Subscriber<FlippedsResponse>() {
                    override fun onCompleted() {
                        hideLoadingView()
                    }

                    override fun onError(e: Throwable) {
                        Log.e("hoolly","error",e)
                        hideLoadingView()
                    }

                    override fun onNext(fllippesResonse: FlippedsResponse) {
                        mAdapter?.fllippeds = fllippesResonse.flippedwords
                        mAdapter?.notifyDataSetChanged()
                    }
                })
    }

    private fun hideLoadingView() {
        if (activity is MainActivity){
            (activity as MainActivity).hideProgressBar()
        }
    }

    private fun showLoadingView() {
        if (activity is MainActivity){
            (activity as MainActivity).showProgressBar()
        }
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
            holder.flippedSento.text = fllippeds?.get(position)?.sendto.toString()
            holder.flippedDistance.text = fllippeds?.get(position)?.distance?.toString()
            if (FlippedHelper.hasPic(fllippeds?.get(position))){
                holder.tagPic.visibility = View.VISIBLE
            }else{
                holder.tagPic.visibility = View.GONE
            }
            if (FlippedHelper.hasVoice(fllippeds?.get(position))){
                holder.tagVoice.visibility = View.VISIBLE
            }else{
                holder.tagVoice.visibility = View.GONE
            }
            if (FlippedHelper.hasVideo(fllippeds?.get(position))){
                holder.tagVoice.visibility = View.VISIBLE
            }else{
                holder.tagVideo.visibility  = View.GONE
            }
        }

        override fun getItemCount(): Int {
            return if (fllippeds == null) 0 else fllippeds!!.size
        }
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


