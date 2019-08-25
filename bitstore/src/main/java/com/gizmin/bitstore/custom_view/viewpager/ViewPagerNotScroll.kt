package com.gizmin.bitstore.custom_view.viewpager

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager


class ViewPagerNotScroll : ViewPager {
    private var isPagingEnabled = false

    constructor(context: Context): super(context)
    constructor(context: Context, attrs: AttributeSet): super(context, attrs)

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return this.isPagingEnabled && super.onTouchEvent(event)
    }

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        return this.isPagingEnabled && super.onInterceptTouchEvent(event)
    }

    fun setPagingEnabled(b: Boolean) {
        this.isPagingEnabled = b
    }
}