package com.brzhang.fllipped.view

import android.Manifest
import android.content.Context
import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.LinearSnapHelper
import android.support.v7.widget.RecyclerView
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import com.brzhang.fllipped.R
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import kotlinx.android.synthetic.main.activity_image_detail.*
import android.os.AsyncTask
import com.bumptech.glide.request.animation.GlideAnimation
import android.graphics.Bitmap
import android.os.Environment
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


/**
 * Created by hoollyzhang on 2017/8/4.
 */
class ImageDetailActivity : FlippedBaseActivity() {
    var mRecyclerView: RecyclerView? = null
    var mImageIndex: TextView? = null
    var mAdapter: ImageDetailAdapter? = null
    var imageList: ArrayList<String>? = null
    var mLinearLayoutManager: LinearLayoutManager? = null
    var mCurrentArenaPosition: Int = 0
    override fun getLayoutRes(): Int {
        return R.layout.activity_image_detail
    }

    override fun setupView(view: View) {
        showNavigationBack()
        imageList = intent.getStringArrayListExtra(KEY_IMAGE_LIST)
        mRecyclerView = flipped_image_detail_recycler_view
        mImageIndex = flipped_image_detail_index
        mLinearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        mRecyclerView?.layoutManager = mLinearLayoutManager
        var helper = LinearSnapHelper()
        helper.attachToRecyclerView(mRecyclerView)
        mRecyclerView?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    mCurrentArenaPosition = mLinearLayoutManager?.findFirstCompletelyVisibleItemPosition() ?: 0
                    mImageIndex?.text = "${mCurrentArenaPosition + 1}/${mAdapter?.itemCount}"
                }
            }
        })
        mAdapter = ImageDetailAdapter(this)
        mRecyclerView?.adapter = mAdapter
        mAdapter?.imageList = imageList
        mImageIndex?.text = "${1}/${mAdapter?.itemCount}"
        mAdapter?.notifyDataSetChanged()
    }

    override fun handleRxEvent(event: Any?) {

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_image_detail, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.op_save -> {
                rxPermissions?.request(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        ?.subscribe { grant ->
                            if (grant) {
                                saveBitMap(imageList?.get(mCurrentArenaPosition))
                            } else {
                                toast("请到权限中开启访问相册的授权")
                            }
                        }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun saveBitMap(url: String?) {
        var path = ""
        Glide
                .with(this)
                .load(url)
                .asBitmap()
                .toBytes(Bitmap.CompressFormat.JPEG, 100)
                .into(object : SimpleTarget<ByteArray>() {
                    override fun onResourceReady(resource: ByteArray, glideAnimation: GlideAnimation<in ByteArray>) {
                        object : AsyncTask<Void, Void, Void>() {
                            override fun doInBackground(vararg params: Void): Void? {
                                val sdcard = Environment.getExternalStorageDirectory()
                                val name = System.currentTimeMillis()
                                path = "$sdcard/flipped/$name.jpg"
                                val file = File(path)
                                val dir = file.parentFile
                                try {
                                    if (!dir.mkdirs() && (!dir.exists() || !dir.isDirectory())) {
                                        throw IOException("Cannot ensure parent directory for file " + file)
                                    }
                                    val s = BufferedOutputStream(FileOutputStream(file))
                                    s.write(resource)
                                    s.flush()
                                    s.close()
                                } catch (e: IOException) {
                                    e.printStackTrace()
                                }
                                return null
                            }

                            override fun onPostExecute(result: Void?) {
                                super.onPostExecute(result)
                                toast("已保存到本地[$path]")
                            }
                        }.execute()
                    }
                })
    }

    inner class ImageDetailAdapter(var activity: FlippedBaseActivity) : RecyclerView.Adapter<ImageDetailViewHolder>() {
        var imageList: ArrayList<String>? = arrayListOf()

        override fun getItemCount(): Int {
            return imageList?.size ?: 0
        }

        override fun onBindViewHolder(holder: ImageDetailViewHolder?, position: Int) {
            Glide.with(activity).load(imageList?.get(position)).into(holder?.image)
        }

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ImageDetailViewHolder {
            return ImageDetailViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.image_detail_item, parent, false))
        }

    }

    inner class ImageDetailViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        var image: ImageView = itemView as ImageView
    }

    companion object {
        val KEY_IMAGE_LIST = "KEY_IMAGE_LIST"
        fun lanuchMe(context: Context, imageList: ArrayList<String>) {
            var intent = Intent(context, ImageDetailActivity::class.java)
            intent.putExtra(KEY_IMAGE_LIST, imageList)
            context.startActivity(intent)
        }
    }
}