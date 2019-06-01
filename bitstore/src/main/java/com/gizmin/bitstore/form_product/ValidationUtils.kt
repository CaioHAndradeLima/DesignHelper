package com.gizmin.bitstore.form_product

import android.text.TextUtils
import com.gizmin.bitstore.form_product.utils.CpfCnpjValidate
import java.util.*

object ValidationUtils {
    fun isGUID(text: String): Boolean = try {
        UUID.fromString(text)
        true
    } catch (e: IllegalArgumentException) {
        false
    }

    fun isEmail(target: CharSequence) : Boolean = !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches()

    fun isCpfOrCnpj(text : String) = CpfCnpjValidate.isValidCPF(text)
}