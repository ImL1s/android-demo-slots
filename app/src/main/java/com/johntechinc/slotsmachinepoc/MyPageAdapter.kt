package com.johntechinc.slotsmachinepoc

import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter

/**
 * Created by ImL1s on 22/08/2017.
 * Description: PagerAdapter for handling views in a ViewPager.
 */
class MyPageAdapter(private val mViewList: List<View>) : PagerAdapter() {

    override fun getCount(): Int {
        return mViewList.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        container.addView(mViewList[position])
        return mViewList[position]
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(mViewList[position])
    }
}
