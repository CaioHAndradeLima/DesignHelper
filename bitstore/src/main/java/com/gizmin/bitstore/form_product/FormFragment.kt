package com.gizmin.bitstore.form_product

import android.os.Bundle
import android.text.Editable
import android.text.SpannableString
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.gizmin.bitstore.R
import com.gizmin.bitstore.form_product.mask_utils.CpfCnpjMask
import com.gizmin.bitstore.form_product.utils.clickForm
import com.gizmin.bitstore.form_product.utils.updateButtonStatus
import java.lang.IllegalStateException

open class FormFragment : Fragment(), TextWatcher, FormFragmentMethods {

    companion object {
        private const val EXTRA_POSITION = "EP"

        fun <T : FormFragment> newInstance(position: Int, fragment: T? = null): T {
            val fpf = fragment ?: FormFragment()
            val args = Bundle()
            args.putInt(EXTRA_POSITION, position)
            fpf.arguments = args
            fpf.focusEditText = position == 0
            return fpf as T
        }

    }

    lateinit var editText: EditText
    protected lateinit var textViewTitle: TextView
    protected lateinit var button: Button
    protected val formView: FormProductView
        get() {
            (activity as FormMethods).getListFormView().forEach {
                if (arguments!!.getInt(EXTRA_POSITION, 0) == it.position)
                    return it as FormProductView
            }

            throw IllegalStateException()
        }

    private var focusEditText = false


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_form_product, container, false)

        editText = view.findViewById(R.id.edittext)
        textViewTitle = view.findViewById(R.id.txt_title)
        button = view.findViewById(R.id.button)
        editText.inputType = formView.typeKeyboard

        button.setOnClickListener {
            if (button.isEnabled) {
                clickForm(this)
            }
        }

        if(formView.title is SpannableString) {
            textViewTitle.setText(formView.title, TextView.BufferType.SPANNABLE)
        } else {
            textViewTitle.text = formView.title
        }

        button.text = formView.nameButton

        if (focusEditText)
            editText.requestFocus()

        editText.addTextChangedListener(this)

        if(formView.mask == FormMask.CPFAndCNPJ) {
            CpfCnpjMask.insertTextWatcher(editText)
        }
        return view
    }

    override fun onResume() {
        super.onResume()
        updateStatusButton(editText.text.toString())
    }

    override fun afterTextChanged(s: Editable?) {
        val text = when(formView.mask) {
            FormMask.CPFAndCNPJ -> CpfCnpjMask.unmask(s.toString())
            else -> s.toString()
        }
        updateStatusButton(text)
    }

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
    }

    override fun updateStatusButton(text: String) {
        updateButtonStatus(button, formView.validation.invoke(text))
    }
}

interface FormFragmentMethods {
    fun updateStatusButton(text: String)
}