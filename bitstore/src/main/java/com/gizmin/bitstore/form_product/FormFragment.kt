package com.gizmin.bitstore.form_product

import android.os.Bundle
import android.text.Editable
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
import com.gizmin.bitstore.form_product.utils.clickForm
import com.gizmin.bitstore.form_product.utils.updateButtonStatus
import java.lang.IllegalStateException

class FormFragment : Fragment(), TextWatcher {

    companion object {
        private const val EXTRA_POSITION = "EP"

        fun newInstance(position: Int): FormFragment {
            val fpf = FormFragment()
            val args = Bundle()
            args.putInt(EXTRA_POSITION, position)
            fpf.arguments = args
            return fpf
        }
    }

    lateinit var editText: EditText
    private lateinit var textViewTitle: TextView
    private lateinit var button: Button
    private val formView: FormProductView
        get() {
            (activity as FormMethods).getListFormView().forEach {
                if (arguments!!.getInt(EXTRA_POSITION, 0) == it.position)
                    return it as FormProductView
            }

            throw IllegalStateException()
        }

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

        textViewTitle.text = formView.title
        button.text = formView.nameButton

        editText.addTextChangedListener(this)

        return view
    }

    override fun onResume() {
        super.onResume()
        updateButtonStatus(button, formView.validation.invoke(editText.text.toString()))
    }

    override fun afterTextChanged(s: Editable?) {
        updateButtonStatus(button, formView.validation.invoke(s.toString()))
    }

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
    }
}
