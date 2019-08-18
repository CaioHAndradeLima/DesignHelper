package com.gizmin.bitstore.util

import android.content.Context
import android.text.Spannable
import android.text.SpannableString
import android.text.style.TextAppearanceSpan


/**
 * pair with string res and style's id
 */
fun getSpannableString(context: Context, vararg pairTextAndStyle: Pair<String, Int>): SpannableString {

    var allText = ""
    for (pair in pairTextAndStyle) {
        allText += "${pair.first} "
    }
    allText = allText.removeSuffix(" ")

    val spannableString = SpannableString(allText)

    for (pair in pairTextAndStyle) {

        val index = allText.indexOf(pair.first)

        spannableString.setSpan(
            TextAppearanceSpan(context, pair.second),
            index,
            index + pair.first.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }

    return spannableString
}