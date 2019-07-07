package com.gizmin.bitstore.form_product.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gizmin.bitstore.R
import com.gizmin.bitstore.form_product.FormListOptionView
import com.gizmin.bitstore.form_product.utils.clickForm
import com.google.gson.Gson
import androidx.recyclerview.widget.DividerItemDecoration



open class ListOptionsFormFragment : Fragment(), View.OnClickListener {
    protected lateinit var recyclerView: RecyclerView
    protected lateinit var textViewTitle: TextView

    private lateinit var formOptions: FormListOptionView

    companion object {

        private const val EXTRA_LIST_OPTIONS = "EXTRA_LIST_OPTIONS"

        fun newInstance(formOptions: FormListOptionView): ListOptionsFormFragment {
            return newInstance(formOptions, ListOptionsFormFragment())
        }

        fun <T : ListOptionsFormFragment> newInstance(formOptions: FormListOptionView, fragment: T): T {
            val arguments = if (fragment.arguments == null) Bundle() else fragment.arguments!!

            arguments.putString(EXTRA_LIST_OPTIONS, Gson().toJson(formOptions))
            fragment.formOptions = formOptions
            fragment.arguments = arguments
            return fragment
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!::formOptions.isInitialized && arguments != null)
            formOptions = Gson().fromJson(arguments!!.getString(EXTRA_LIST_OPTIONS), FormListOptionView::class.java)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_list_options, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.recyclerviewOptions)
        textViewTitle = view.findViewById(R.id.txt_title)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val dividerItemDecoration = DividerItemDecoration(
            recyclerView.context,
            (recyclerView.layoutManager as LinearLayoutManager).orientation
        )

        recyclerView.addItemDecoration(dividerItemDecoration)

        recyclerView.adapter = ListOptionsFormAdapter(formOptions.listOptions) {
            onClick(recyclerView)
        }

        textViewTitle.text = formOptions.title
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        arguments?.putString(EXTRA_LIST_OPTIONS, Gson().toJson(formOptions))
    }

    override fun onClick(v: View) {
        clickForm(this)
    }

}
