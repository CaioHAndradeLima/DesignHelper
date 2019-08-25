package com.gizmin.bitstore.util

open class DoubleClick {

    private var lastClick : Long = 0
    private var TIME_BETWEEN_SINGLE_CLICK_AND_DOUBLE_CLICK = 1100

    constructor()
    constructor(time : Int) {
        TIME_BETWEEN_SINGLE_CLICK_AND_DOUBLE_CLICK = time
    }


    fun isSingleClick(): Boolean {
        val now = System.currentTimeMillis()
        val result = now - lastClick > TIME_BETWEEN_SINGLE_CLICK_AND_DOUBLE_CLICK
        lastClick = now
        return result
    }

    fun isDoubleClick() : Boolean {
        return !isSingleClick()
    }

}
