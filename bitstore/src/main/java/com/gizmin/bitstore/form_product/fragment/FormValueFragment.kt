package com.gizmin.bitstore.form_product.fragment

import android.os.Bundle
import android.view.View
import com.gizmin.bitstore.form_product.FormFragment
import android.text.Editable
import android.text.TextWatcher
import android.util.TypedValue
import androidx.core.content.ContextCompat
import java.text.NumberFormat
import java.util.*


class FormValueFragment : FormFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 28F)
        editText.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.holo_green_dark))
        editText.addTextChangedListener(object : TextWatcher {


            private var isChanging = false

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            @Synchronized
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if(isChanging) {
                    isChanging = false
                    return
                }

                val myLocale = Locale("pt", "BR")

                val symbolCountry = "R$"

                //Nesse bloco ele monta a maskara para money

                isChanging = true

                val cleanString = s.toString()

                    .replace("[$symbolCountry,. ]".toRegex(), "")

                val parsed = java.lang.Double.parseDouble(cleanString.replace(" ", ""))
                val formatted = NumberFormat.getCurrencyInstance(myLocale).format(parsed / 100)
                editText.setText(formatted)
                editText.setSelection(formatted.length)

            }

            override fun afterTextChanged(s: Editable) {

            }
        })


    }

    companion object {
        fun newInstance(position: Int): FormValueFragment {
            return FormFragment.newInstance(position, FormValueFragment())
        }
    }
}