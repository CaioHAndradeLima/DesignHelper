package com.gizmin.bitstore.form_product

import java.util.*

object ValidationUtils {
    fun isGUID(text: String): Boolean = try {
        UUID.fromString(text)
        true
    } catch (e: IllegalArgumentException) {
        false
    }

}