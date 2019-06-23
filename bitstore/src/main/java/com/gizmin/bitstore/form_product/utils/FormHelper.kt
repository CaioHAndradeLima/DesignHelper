package com.gizmin.bitstore.form_product.utils

import android.content.Context
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.gizmin.bitstore.R
import com.gizmin.bitstore.form_product.FormAdapter
import com.gizmin.bitstore.form_product.FormFragment
import com.gizmin.bitstore.form_product.FormMethods


fun clickForm(fragment: Fragment, context: Context = fragment.requireContext()) {
    val viewPager = (context as FormMethods)
        .getViewPager()

    (fragment as? FormFragment)?.let {
        (viewPager.adapter as FormAdapter)
            .add(viewPager.currentItem, it.editText.text.toString())
    }

    if (viewPager.adapter!!.count != viewPager.currentItem - 1) {
        viewPager.setCurrentItem(viewPager.currentItem + 1, true)
    } else {
        (viewPager.context as FormMethods)
            .whenFinishedForm((viewPager.adapter as FormAdapter).map)
    }
}

fun updateButtonStatus(button: Button, isValid : Boolean) {
    if (isValid) {
        button.isEnabled = true
        button.setTextColor(ContextCompat.getColor(button.context, android.R.color.white))
        button.setBackgroundColor(ContextCompat.getColor(button.context, R.color.colorPrimary))
    } else {
        button.isEnabled = false
        button.setTextColor(ContextCompat.getColor(button.context, android.R.color.white))
        button.setBackgroundColor(ContextCompat.getColor(button.context, R.color.grey))
    }
}
