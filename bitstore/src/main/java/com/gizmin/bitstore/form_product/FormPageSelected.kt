package com.gizmin.bitstore.form_product

interface FormPageSelected {
    fun onPageSelected()
}

interface FormPageSelectedListener {
    fun onPageSelected(position: Int)
}