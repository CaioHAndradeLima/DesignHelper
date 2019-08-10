package com.gizmin.bitstore.form_product.fragment

import android.os.Bundle
import android.view.View
import com.gizmin.bitstore.form_product.FormFragment
import android.text.Editable
import android.text.TextWatcher
import android.util.TypedValue
import android.widget.EditText
import androidx.core.content.ContextCompat
import com.gizmin.bitstore.form_product.utils.updateButtonStatus
import java.lang.Double.parseDouble
import java.text.NumberFormat
import java.util.*


class FormValueFragment : FormFragment() {

    lateinit var textWatcher: ValueListenerTextWatcher

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 28F)
        editText.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.holo_green_dark))

        textWatcher = ValueListenerTextWatcher(editText)
        editText.addTextChangedListener(textWatcher)
    }

    override fun updateStatusButton(text: String) {
        updateButtonStatus(button, formView.validation.invoke(textWatcher.unmask(text)))
    }

    companion object {
        fun newInstance(position: Int): FormValueFragment {
            return newInstance(position, FormValueFragment())
        }
    }
}

class ValueListenerTextWatcher(private val editText: EditText) : TextWatcher {
    private var isChanging = false

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

    }

    @Synchronized
    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        if (isChanging) {
            isChanging = false
            return
        }

        val myLocale = Locale("pt", "BR")


        //Nesse bloco ele monta a maskara para money


        val cleanString = unmask(s.toString())

        if (cleanString.isEmpty()) return
        isChanging = true

        val parsed = parseDouble(cleanString.replace(" ", ""))
        val formatted = NumberFormat.getCurrencyInstance(myLocale).format(parsed / 100)
        editText.setText(formatted)
        editText.setSelection(formatted.length)

    }

    override fun afterTextChanged(s: Editable) {

    }

    fun unmask(text: String): String {
        val symbolCountry = "R$"

        return text
            .replace("[$symbolCountry,. ]".toRegex(), "")
    }

    fun getWithoutMask(text: String): Double {
        val cleanText = unmask(text)
        if (cleanText.isEmpty())
            return 0.0

        return cleanText.toDouble()
    }


}