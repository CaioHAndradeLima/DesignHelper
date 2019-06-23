package com.gizmin.bitstore.form_product.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gizmin.bitstore.R
import com.gizmin.bitstore.form_product.FormOptionView
import com.gizmin.bitstore.form_product.utils.clickForm
import com.gizmin.bitstore.form_product.utils.updateButtonStatus
import com.google.gson.Gson

open class OptionsFormFragment : Fragment(), View.OnClickListener {

    protected lateinit var formOptions: FormOptionView
    protected lateinit var recyclerView: RecyclerView
    protected lateinit var textViewTitle: TextView
    protected lateinit var button: Button

    companion object {

        private const val EXTRA_OPTIONS = "EXTRA_OPTIONS"

        fun newInstance(formOptions: FormOptionView): OptionsFormFragment {
            val options = OptionsFormFragment()
            val arguments = Bundle()
            arguments.putString(EXTRA_OPTIONS, Gson().toJson(formOptions))
            options.formOptions = formOptions
            options.arguments = arguments
            return options
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!::formOptions.isInitialized && arguments != null)
            formOptions = Gson().fromJson(arguments!!.getString(EXTRA_OPTIONS), FormOptionView::class.java)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_form_options, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.recyclerviewOptions)
        textViewTitle = view.findViewById(R.id.txt_title)
        button = view.findViewById(R.id.button)

        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        recyclerView.adapter = OptionsFormAdapter(formOptions.listOptions, recyclerView) {
            updateButton(true)
        }

        textViewTitle.text = formOptions.title
        button.text = formOptions.nameButton

        button.setOnClickListener(this)
        updateButton(false)
    }

    private fun updateButton(isValid: Boolean) {
        updateButtonStatus(button, isValid)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        arguments?.putString(EXTRA_OPTIONS, Gson().toJson(formOptions))
    }

    override fun onClick(v: View) {
        clickForm(this)
    }

}

data class OptionsFormEntity(
    val optionName: String
) {
    var isChecked: Boolean = false
    internal set
}
