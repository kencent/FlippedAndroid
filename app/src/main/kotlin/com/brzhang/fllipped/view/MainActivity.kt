package com.brzhang.fllipped.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.Theme
import com.brzhang.fllipped.*
import com.brzhang.fllipped.pref.UserPref
import com.tencent.ilivesdk.ILiveSDK
import kotlinx.android.synthetic.main.content_main.*
import java.util.ArrayList

class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {
    val TAG = "MainActivity"
    val mSqureFragment = SqureFragment.newInstance()
    val mMineFragment = MineFragment.newInstance()
    var mCallFragment = CallFragment.newInstance()

    override fun handleRxEvent(event: Any?) {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        needLocation = true
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupView()
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

//        val fab = findViewById(R.id.fab) as FloatingActionButton
//        fab.setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                    .setAction("Action", null).show()
//        }

        val drawer = findViewById(R.id.drawer_layout) as DrawerLayout
        val toggle = ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.setDrawerListener(toggle)
        toggle.syncState()

        val navigationView = findViewById(R.id.nav_view) as NavigationView
        navigationView.setNavigationItemSelectedListener(this)
        IntroductionActivity.lanuchMe(this)
        ILiveSDK.getInstance().initSdk(App.ApplicationContext(), Config.HU_DOND_VIDEO_APP_ID, Config.VIDEO_APP_ACCOUNT_TYPE)
        weatherGogoRandomCallFragment()
        showOpenNotificationTips()

    }

    private fun showOpenNotificationTips() {
        if (Build.MANUFACTURER.equals("vivo") || Build.MANUFACTURER.equals("OPPO")) {
            if (!NotificationUtils.isNotificationEnabled(this)) {
                //未获得通知权限，此时把用户引向系统的设置界面，使用户手动打开通知权限
                //代码不上了，总之在这里谈提示框让用户打开设置界面就好了～
                MaterialDialog.Builder(this)
                        .theme(Theme.DARK)
                        .title("开启通知可以有更大机会匹配到随机聊的人")
                        .positiveText("去开启")
                        .neutralText("取消")
                        .onPositive { dialog, which ->
                            // TODO
                            NotificationUtils.toSettingPage(this)
                        }
                        .onNeutral { dialog, which ->
                        }
                        .onNegative { dialog, which ->
                        }
                        .onAny { dialog, which ->
                        }
                        .show()

            }
        }
    }

    private fun weatherGogoRandomCallFragment() {
        val index = intent.getIntExtra(KEY_FRAGMENT_INDEX, 0);
        if (index == 1) {
            bottomBar.selectTabAtPosition(1, true)
            mCallFragment.makeCallTvClick()
        }
    }


    private fun startNotificationService() {
        val startIntent = Intent(this, NotificationService::class.java)
        var componentName = startService(startIntent)
        if (componentName != null) {
        }
    }

    override fun onResume() {
        super.onResume()
        loginCheck()
    }

    override fun onBackPressed() {
        val drawer = findViewById(R.id.drawer_layout) as DrawerLayout
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        val id = item.itemId

        if (id == R.id.nav_feed_back) {
            if (UserPref.isUserLogin(applicationContext)) {
                ReportActivity.startReport(this)
            } else {
                gotoLoginActivity()
            }
        } else if (id == R.id.nav_quite) {
            if (UserPref.isUserLogin(applicationContext)) {
                confirmLoginout()
            } else {
                toast("当前并没有登录")
            }
        } else if (id == R.id.nav_help) {
            AboutActivity.startMe(this)
        }

        val drawer = findViewById(R.id.drawer_layout) as DrawerLayout
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    private fun confirmLoginout() {
        MaterialDialog.Builder(this)
                .theme(Theme.DARK)
                .title("确认退出登录")
                .positiveText("确认")
                .neutralText("取消")
                .onPositive { dialog, which ->
                    // TODO
                    UserPref.setUserLogin(applicationContext, false)
                    UserPref.setUserName(applicationContext, "")
                    gotoLoginActivity()
                }
                .onNeutral { dialog, which ->
                }
                .onNegative { dialog, which ->
                }
                .onAny { dialog, which ->
                }
                .show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)
        return true
    }

    private var mAddBtnNeedShow: Boolean = true

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        menu?.findItem(R.id.op_add)?.isVisible = mAddBtnNeedShow
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.op_add -> {
                if (UserPref.isUserLogin(applicationContext)) {
                    startPostActivity()
                } else {
                    gotoLoginActivity()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun startPostActivity() {
        val intent = Intent(this, PostActivity::class.java)
        startActivity(intent)
    }

    /*检查登录态是否过期*/
    private fun loginCheck() {
        /**
         * 登录检测还是放在后台校验
         */
        if (UserPref.isUserLogin(this, false) && !NotificationService.sMsdkLogined) {
            //通知用户，有新的消息到来
            startNotificationService()
        }
    }

    private fun setupView() {

        setupBottomBarFragments()
        registeBottomBarCallBacks()
    }

    private fun registeBottomBarCallBacks() {
        bottomBar.setOnTabSelectListener { tabId: Int ->
            when (tabId) {
                R.id.tab_nearby -> {
                    showSqureFragment()
                    showAddFeedBtn()
                }
                R.id.tab_call -> {
                    checkPermission()
                    showCallFragment()
                    hideAddFeedsBtn()
                }
                R.id.tab_mine -> {
                    showMineFragment()
                    showAddFeedBtn()
                }
            }
        }

        bottomBar.setOnTabReselectListener { tabId: Int ->
            when (tabId) {
                R.id.tab_nearby -> {
                    dToast("tab_nearby click double")
                }
                R.id.tab_mine -> {
                    dToast("tab_mine click double")
                }
            }
        }
        title = "广场"
    }

    private fun showAddFeedBtn() {
        mAddBtnNeedShow = true
        mCallFragment.cancelCall()
        supportInvalidateOptionsMenu()
    }

    private fun hideAddFeedsBtn() {
        mAddBtnNeedShow = false
        supportInvalidateOptionsMenu()
    }

    private fun showMineFragment() {
        val transAction = supportFragmentManager.beginTransaction()
        transAction.hide(mSqureFragment)
        transAction.hide(mCallFragment)
        transAction.show(mMineFragment)
        transAction.commitAllowingStateLoss()
        title = "我的"
        mMineFragment.load()

    }

    private fun showCallFragment() {
        val transAction = supportFragmentManager.beginTransaction()
        transAction.hide(mSqureFragment)
        transAction.show(mCallFragment)
        transAction.hide(mMineFragment)
        transAction.commitAllowingStateLoss()
        title = "配聊"
    }

    private fun showSqureFragment() {
        val transAction = supportFragmentManager.beginTransaction()
        transAction.show(mSqureFragment)
        transAction.hide(mMineFragment)
        transAction.hide(mCallFragment)
        transAction.commitAllowingStateLoss()
        title = "广场"
    }

    private fun setupBottomBarFragments() {
        val transAction = supportFragmentManager.beginTransaction()
        transAction.add(R.id.activity_main_fragment_container, mSqureFragment)
        transAction.add(R.id.activity_main_fragment_container, mCallFragment)
        transAction.add(R.id.activity_main_fragment_container, mMineFragment)
        transAction.show(mSqureFragment)
        transAction.commitAllowingStateLoss()
    }

    internal fun checkPermission() {
        val REQUEST_PHONE_PERMISSIONS = 200
        val permissionsList = ArrayList<String>()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                permissionsList.add(Manifest.permission.CAMERA)
            if (checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED)
                permissionsList.add(Manifest.permission.RECORD_AUDIO)
            if (checkSelfPermission(Manifest.permission.WAKE_LOCK) != PackageManager.PERMISSION_GRANTED)
                permissionsList.add(Manifest.permission.WAKE_LOCK)
            if (checkSelfPermission(Manifest.permission.MODIFY_AUDIO_SETTINGS) != PackageManager.PERMISSION_GRANTED)
                permissionsList.add(Manifest.permission.MODIFY_AUDIO_SETTINGS)
            if (permissionsList.size != 0) {
                requestPermissions(permissionsList.toTypedArray(),
                        REQUEST_PHONE_PERMISSIONS)
            } else {
//                initData()
            }
        }
    }

    companion object {
        val KEY_FRAGMENT_INDEX: String = "key_fragment_index"
    }

}


