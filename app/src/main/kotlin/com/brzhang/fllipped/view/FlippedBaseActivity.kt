package com.brzhang.fllipped.view

import android.content.DialogInterface
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.brzhang.fllipped.R
import com.brzhang.fllipped.api.FllippedService
import com.brzhang.fllipped.api.RetrofitClient
import kotlinx.android.synthetic.main.activity_flipped_base.*

/**
 *
 * Created by brzhang on 2017/7/6.
 * Description :
 */
abstract class FlippedBaseActivity : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flipped_base)
        initToolBar()
        setSubContent()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    private fun initToolBar() {

//        val toolbar = findViewById(R.id.toolbar) as Toolbar
//        toolbar.setNavigationOnClickListener({
//            finish()
//        })
//        setSupportActionBar(toolbar)
//        supportActionBar?.title = ""
//        showNavigationBack()

    }

    fun showFloatActionButton(onClickListener: View.OnClickListener){
        float_button.visibility = View.VISIBLE
        float_button.setOnClickListener(onClickListener)
    }

    fun showNavigationBack() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    fun hideNavigationBack() {
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            android.R.id.home ->{
                onOptionHomeClick()
                return true
            }
            else -> {
                return super.onOptionsItemSelected(item)
            }
        }
    }

    open fun onOptionHomeClick(){
        this.finish()
    }
    protected fun setSubContent() {
        val container = findViewById(R.id.activity_base_container) as FrameLayout
        val containerView = layoutInflater.inflate(getLayoutRes(), container, false)
        container.addView(containerView)
        setupView(containerView)
    }

    fun fllippedNetService(): FllippedService {
        return RetrofitClient.newInstance().create(FllippedService::class.java)
    }

    fun showProgressBar(){
        progress_bar.visibility = View.VISIBLE
        progress_bar.show()
    }

    fun hideProgressBar(){
        progress_bar.visibility = View.GONE
        progress_bar.hide()
    }

    abstract fun getLayoutRes(): Int

    abstract fun setupView(view: View)

}