package com.gizmin.bitstore.custom_view.viewpager

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager

object ViewPagerUtils {
    fun getRexexToGetFragmentInFragmentManager(viewPager : ViewPager, position : Int): String {
        return "android:switcher:${viewPager.id}:${(viewPager.adapter as FragmentPagerAdapter).getItemId(position)}"
    }

    fun getFragmentBySupportFragmentManager(viewPager : ViewPager, position : Int = viewPager.currentItem + 1) : Fragment? {
        return (viewPager.context as AppCompatActivity)
                .supportFragmentManager
                .findFragmentByTag(getRexexToGetFragmentInFragmentManager(viewPager, position))
    }
}