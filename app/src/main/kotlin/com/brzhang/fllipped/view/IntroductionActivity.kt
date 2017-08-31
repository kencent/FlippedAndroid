package com.brzhang.fllipped.view

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager

import com.brzhang.fllipped.R
import com.brzhang.fllipped.pref.UserPref

class IntroductionActivity : AppCompatActivity() {

    var viewPager: ViewPager? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_introduction)
        viewPager = findViewById(R.id.activity_splash_view_pager) as ViewPager?
        viewPager?.adapter = MyPagerAdapter(supportFragmentManager)
    }

    override fun onResume() {
        super.onResume()
    }


    inner class MyPagerAdapter(fm: FragmentManager?) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> IntroduceFragment.newInstance(R.drawable.introduction_1, false)
                1 -> IntroduceFragment.newInstance(R.drawable.introduction_2, false)
                else -> IntroduceFragment.newInstance(R.drawable.introduction_3, true)
            }
        }

        override fun getCount(): Int = 3

    }

    companion object {
        fun lanuchMe(context: Context) {
            if (!UserPref.isIntroduced(context, false)) {
                var intent = Intent(context, IntroductionActivity::class.java)
                context.startActivity(intent)
//                UserPref.setIsIntroduced(context, true)
            }
        }
    }
}
