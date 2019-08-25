package com.gizmin.bitstore.form_product.mask_utils

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

object CpfCnpjMask {

    private const val maskCNPJ = "##.###.###/####-##"
    private const val maskCPF = "###.###.###-##"

    var isCPF = true
    private set

    fun unmask(s: String): String {
        return s.replace("[^0-9]*".toRegex(), "")
    }

    fun insertTextWatcher(editText: EditText, isCPF : Boolean = true) {
        this.isCPF = isCPF
        val txtWatcher = object : TextWatcher {
            var isUpdating: Boolean = false
            var old = ""

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val str = unmask(s.toString())
                val mask: String
                val defaultMask = getDefaultMask(str)
                mask = when (str.length) {
                    11 -> maskCPF
                    14 -> maskCNPJ

                    else -> defaultMask
                }

                var mascara = ""
                if (isUpdating) {
                    old = str
                    isUpdating = false
                    return
                }
                var i = 0
                for (m in mask.toCharArray()) {
                    if (m != '#' && str.length > old.length || m != '#' && str.length < old.length && str.length != i) {
                        mascara += m
                        continue
                    }

                    try {
                        mascara += str[i]
                    } catch (e: Exception) {
                        break
                    }

                    i++
                }
                isUpdating = true
                editText.setText(mascara)
                editText.setSelection(mascara.length)
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int, count: Int,
                after: Int
            ) {
            }

            override fun afterTextChanged(s: Editable) {}
        }

        editText.addTextChangedListener(txtWatcher)
    }


    private fun getDefaultMask(str: String): String {
        return if(str.length < 12) maskCPF else maskCNPJ
    }

    fun setMaskCPF(isCpf : Boolean) {
        this.isCPF = isCpf
    }

}
