package com.gizmin.bitstore.form_product

interface FormPageSelected {
    @Deprecated("now implement FormPageSelectedListener and use onPageSelected(position : Int)")
    fun onPageSelected()
}

interface FormPageSelectedListener {
    fun onPageSelected(position: Int)
}