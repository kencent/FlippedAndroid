package com.brzhang.fllipped.view

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.widget.Toast
import com.brzhang.fllipped.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

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

    private fun initToolBar() {

        val toolbar = findViewById(R.id.toolbar) as Toolbar
        toolbar.setNavigationOnClickListener({
            finish()
        })
        setSupportActionBar(toolbar)
        supportActionBar?.title = ""
        showNavigationBack()

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
                    Toast.makeText(this, "tab_nearby click double", Toast.LENGTH_SHORT).show()
                }
                R.id.tab_friends -> {
                    Toast.makeText(this, "tab_friends click double", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun showMineFragment() {
        val transAction = supportFragmentManager.beginTransaction()
        transAction.hide(mSqureFragment)
        transAction.show(mMineFragment)
        transAction.commitAllowingStateLoss()

    }

    private fun showSqureFragment() {
        val transAction = supportFragmentManager.beginTransaction()
        transAction.show(mSqureFragment)
        transAction.hide(mMineFragment)
        transAction.commitAllowingStateLoss()
    }

    private fun setupBottomBarFragments() {
        val transAction = supportFragmentManager.beginTransaction()
        transAction.add(R.id.activity_main_fragment_container, mSqureFragment)
        transAction.add(R.id.activity_main_fragment_container, mMineFragment)
        transAction.show(mSqureFragment)
        transAction.commitAllowingStateLoss()
    }


}
