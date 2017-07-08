package com.brzhang.fllipped.view

import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.brzhang.fllipped.R
import com.brzhang.fllipped.api.FllippedService
import com.brzhang.fllipped.api.RetrofitClient

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

        val toolbar = findViewById(R.id.toolbar) as Toolbar
        toolbar.setNavigationOnClickListener({
            finish()
        })
        setSupportActionBar(toolbar)
        supportActionBar?.title = ""
        showNavigationBack()

    }

    fun setActTitle(titleStr: CharSequence) {
        val title = findViewById(R.id.title) as TextView
        title.text = titleStr
    }

    fun showNavigationBack() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    fun hideNavigationBack() {
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }

    fun showRightButton(res: Int = R.drawable.flipped_ic_add, listener: View.OnClickListener) {
        val rightButton = findViewById(R.id.right_button) as ImageView
        val rightText = findViewById(R.id.right_text) as TextView
        rightButton.visibility = View.VISIBLE
        rightText.visibility = View.GONE
        rightButton.setImageResource(res)
        rightButton.setOnClickListener(listener)
    }

    fun showRigthText(string: String = "完成", listener: View.OnClickListener) {
        val rightButton = findViewById(R.id.right_button) as ImageView
        val rightText = findViewById(R.id.right_text) as TextView
        rightButton.visibility = View.GONE
        rightText.visibility = View.VISIBLE
        rightText.text = string
        rightText.setOnClickListener(listener)
    }

    fun hideRight() {
        val rightButton = findViewById(R.id.right_button) as ImageView
        val rightText = findViewById(R.id.right_text) as TextView
        rightButton.visibility = View.GONE
        rightText.visibility = View.GONE
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

    abstract fun getLayoutRes(): Int

    abstract fun setupView(view: View)

}