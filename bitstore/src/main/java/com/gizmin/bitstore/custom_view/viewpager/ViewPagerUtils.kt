package com.gizmin.bitstore.custom_view.viewpager

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager

object ViewPagerUtils {
    fun getRexexToGetFragmentInFragmentManager(idViewPager : Int, position : Int): String {
        return "android:switcher:$idViewPager:$position"
    }

    fun getFragmentBySupportFragmentManager(idViewPager : Int, viewPager : ViewPager, position : Int = viewPager.currentItem + 1) : Fragment? {
        return (viewPager.context as AppCompatActivity)
                .supportFragmentManager
                .findFragmentByTag(getRexexToGetFragmentInFragmentManager(idViewPager, position))
    }
}