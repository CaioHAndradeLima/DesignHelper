package com.gizmin.bitstore.custom_view.viewpager

import android.content.Context
import android.util.AttributeSet
import android.view.animation.Interpolator
import androidx.viewpager.widget.ViewPager
import android.view.MotionEvent


class ViewPagerCustomDuration : ViewPager {

    private var mScroller: ScrollerCustomDuration? = null
    private var isSwipingToRightSide = true
    private val directionControlManager by lazy { DirectionControlManager() }

    constructor(context: Context) : super(context) {
        postInitViewPager()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        postInitViewPager()
    }

    /**
     * Override the Scroller instance with our own class so we can change the
     * duration
     */
    private fun postInitViewPager() {
        try {
            val viewpager = ViewPager::class.java
            val scroller = viewpager.getDeclaredField("mScroller")
            scroller.isAccessible = true
            val interpolator = viewpager.getDeclaredField("sInterpolator")
            interpolator.isAccessible = true

            mScroller = ScrollerCustomDuration(context,
                    interpolator.get(null) as Interpolator)
            scroller.set(this, mScroller)
        } catch (e: Exception) {
        }
    }

    fun setSwipingToRightSide(isSwipingToRightSide: Boolean): ViewPagerCustomDuration {
        this.isSwipingToRightSide = isSwipingToRightSide
        return this
    }

    /**
     * Set the factor by which the duration will change
     */
    fun setScrollDurationFactor(scrollFactor: Double) {
        mScroller!!.setScrollDurationFactor(scrollFactor)
    }


    override fun onTouchEvent(event: MotionEvent): Boolean {
        return if ( isEnabledToMove(event)) {
            super.onTouchEvent(event)
        } else {
            false
        }
    }

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        return if (isEnabledToMove(event)) {
            super.onInterceptTouchEvent(event)
        } else {
            false
        }

    }

    fun isEnabledToMove(event: MotionEvent) : Boolean {
        /*
        val isMovingToLeft = directionControlManager.isMovingToLeft(event)
        return isMovingToLeft == null || isMovingToLeft
        */
        return false
    }

}

internal class DirectionControlManager {
    private var x1: Float = 0.toFloat()
    var x2: Float = 0.toFloat()
    val MIN_DISTANCE = 150

    fun isMovingToLeft(event: MotionEvent): Boolean? {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> x1 = event.x

            MotionEvent.ACTION_UP -> {
                x2 = event.x
                val deltaX = x2 - x1;

                return if (Math.abs(deltaX) > MIN_DISTANCE) {
                    //is moving to left
                    x2 > x1
                } else {
                    // consider as something else - a screen tap for example
                    null
                }
            }
        }
        return null
    }
}