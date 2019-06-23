package com.gizmin.bitstore.form_product.fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.recyclerview.widget.RecyclerView
import com.gizmin.bitstore.R

open class OptionsFormAdapter(
    private val listOptions: Array<OptionsFormEntity>,
    private val recyclerView: RecyclerView,
    private val anySelectedListener: () -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as OptionsFormHolder).bind(listOptions[position])
    }

    override fun getItemCount(): Int {
        return listOptions.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return OptionsFormHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.holder_options_form, parent, false),
            this
        )
    }

    fun onClick(positionClick: Int) {
        for (position in 0 until itemCount) {
            val holder = recyclerView.findViewHolderForAdapterPosition(position) as OptionsFormHolder
            holder.setChecked(positionClick == position)
        }
        anySelectedListener()
    }

}

class OptionsFormHolder(
    view: View,
    private val adapter: OptionsFormAdapter
) : RecyclerView.ViewHolder(view), View.OnClickListener {


    init {
        itemView.setOnClickListener(this)
    }

    fun bind(optionsFormEntity: OptionsFormEntity) {
        (itemView as RadioButton).text = optionsFormEntity.optionName
    }

    override fun onClick(v: View) {
        adapter.onClick(adapterPosition)
    }

    fun setChecked(isChecked: Boolean) {
        (itemView as RadioButton).isChecked = isChecked
    }
}