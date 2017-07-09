package com.brzhang.fllipped.view

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.brzhang.fllipped.R
import java.util.ArrayList

/**
 *
 * Created by brzhang on 2017/7/3.
 * Description :
 */

class MineFragment : BaseFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.fragment_mine, container, false)
        setupView(view)
        initData()
        return view
    }

    private fun initData() {

    }

    private fun setupView(view: View?) {
        val tabLayout = view?.findViewById(R.id.fragment_mine_tab_layout) as TabLayout
        var viewPager = view?.findViewById(R.id.fragment_mine_view_pager) as ViewPager
        val pageAdapter = MyPageAdatper(childFragmentManager)
        pageAdapter.fragments = arrayListOf(MineSendFragment.newInstance(),MineReceiveFragment.newInstance())
        pageAdapter.titles = arrayListOf("我发送的","我收到的")
        viewPager.adapter = pageAdapter
        tabLayout.setupWithViewPager(viewPager,true)
        tabLayout.tabMode = TabLayout.MODE_FIXED
        tabLayout.tabGravity = TabLayout.GRAVITY_FILL
    }

    override fun onResume() {
        super.onResume()
    }

    companion object {

        fun newInstance(): MineFragment {

            val args = Bundle()

            val fragment = MineFragment()
            fragment.arguments = args
            return fragment
        }
    }

    internal class MyPageAdatper(fm: FragmentManager?) : FragmentPagerAdapter(fm) {
        var titles = ArrayList<String>()
        var fragments = ArrayList<Fragment>()
        override fun getItem(position: Int): Fragment {
            return fragments[position]
        }

        override fun getCount(): Int {
            return fragments.size
        }

        override fun getPageTitle(position: Int): CharSequence {
//            return super.getPageTitle(position)
            return titles[position]
        }

    }
}
