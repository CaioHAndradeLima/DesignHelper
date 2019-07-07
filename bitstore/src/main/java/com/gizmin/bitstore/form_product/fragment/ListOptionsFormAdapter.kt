package com.gizmin.bitstore.form_product.fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.gizmin.bitstore.R

open class ListOptionsFormAdapter(
    private val listOptions: Array<OptionsFormEntity>,
    private val anySelectedListener: () -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ListOptionsFormHolder).bind(listOptions[position])
    }

    override fun getItemCount(): Int {
        return listOptions.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ListOptionsFormHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.holder_list_options_form, parent, false),
            this
        )
    }

    fun onClick(positionClick: Int) {
        for (position in 0 until itemCount) {
            val isChecked = positionClick == position
            listOptions[position].isChecked = isChecked
        }

        anySelectedListener()
    }

}

class ListOptionsFormHolder(
    view: View,
    private val adapter: ListOptionsFormAdapter
) : RecyclerView.ViewHolder(view), View.OnClickListener {

    private val textView: TextView

    init {
        itemView.setOnClickListener(this)
        textView = itemView.findViewById(R.id.textview)
    }

    fun bind(optionsFormEntity: OptionsFormEntity) {
        textView.text = optionsFormEntity.optionName
    }

    override fun onClick(v: View) {
        adapter.onClick(adapterPosition)
    }

}