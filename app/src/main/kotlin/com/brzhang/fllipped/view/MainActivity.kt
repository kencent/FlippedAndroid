package com.brzhang.fllipped.view

import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.Toast
import com.brzhang.fllipped.R
import com.brzhang.fllipped.pref.UserPref
import kotlinx.android.synthetic.main.activity_main.*

/*因为底部有tab栏，因此没有继承至FlippedBaseActivity ，而是直接继承至BaseActivity*/
class MainActivity : BaseActivity() {

    val TAG = "MainActivity"

    val mSqureFragment = SqureFragment.newInstance()
    val mMineFragment = MineFragment.newInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loginCheck()
        setupView()
        initToolBar()
    }

    override fun onResume() {
        super.onResume()
        if (!UserPref.isUserLogin(this)) {
            gotoLoginActivity()
        }
    }

    private fun initToolBar() {

        val toolbar = findViewById(R.id.toolbar) as Toolbar
        toolbar.setNavigationOnClickListener({
            finish()
        })
        setSupportActionBar(toolbar)
        supportActionBar?.title = ""
        //showNavigationBack()
    }

    fun showNavigationBack() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    /*检查登录态是否过期*/
    private fun loginCheck() {

    }

    private fun setupView() {

        setupBottomBarFragments()
        registeBottomBarCallBacks()
    }

    fun showProgressBar(){
        progress_bar.visibility = View.VISIBLE
        progress_bar.show()
    }

    fun hideProgressBar(){
        progress_bar.visibility = View.GONE
        progress_bar.hide()
    }

    private fun registeBottomBarCallBacks() {
        bottomBar.setOnTabSelectListener {
            tabId: Int ->
            when (tabId) {
                R.id.tab_nearby -> {
                    showSqureFragment()
                }
                R.id.tab_friends -> {
                    showMineFragment()
                }
            }
        }

        bottomBar.setOnTabReselectListener {
            tabId: Int ->
            when (tabId) {
                R.id.tab_nearby -> {
                    dToast("tab_nearby click double")
                }
                R.id.tab_friends -> {
                    dToast("tab_friends click double")
                }
            }
        }
    }

    private fun showMineFragment() {
        val transAction = supportFragmentManager.beginTransaction()
        transAction.hide(mSqureFragment)
        transAction.show(mMineFragment)
        transAction.commitAllowingStateLoss()
        activity_main_title.text = "我的"

    }

    private fun showSqureFragment() {
        val transAction = supportFragmentManager.beginTransaction()
        transAction.show(mSqureFragment)
        transAction.hide(mMineFragment)
        transAction.commitAllowingStateLoss()
        activity_main_title.text = "广场"
    }

    private fun setupBottomBarFragments() {
        val transAction = supportFragmentManager.beginTransaction()
        transAction.add(R.id.activity_main_fragment_container, mSqureFragment)
        transAction.add(R.id.activity_main_fragment_container, mMineFragment)
        transAction.show(mSqureFragment)
        transAction.commitAllowingStateLoss()
    }

    /***
     * 这里可以处理总线事件
     */
    override fun handleRxEvent(event: Any?) {
    }
}
