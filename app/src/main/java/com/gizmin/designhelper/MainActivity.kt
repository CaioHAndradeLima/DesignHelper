package com.gizmin.designhelper

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.gizmin.bitstore.custom_view.viewpager.ViewPagerCustomDuration
import com.gizmin.bitstore.form_product.*
import com.gizmin.bitstore.form_product.fragment.OptionsFormEntity
import com.gizmin.bitstore.form_product.fragment.OptionsFormFragment
import com.gizmin.bitstore.form_product.utils.CpfCnpjValidate.isValid

class MainActivity : AppCompatActivity(), FormMethods {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        isValid("caio@gmail.c")
        FormAdapter.init(this, CustomAdapter(getListFormView(), this))
    }

    private val options = arrayOf(
        OptionsFormEntity("option 1"),
        OptionsFormEntity("option 2"),
        OptionsFormEntity("option 3"),
        OptionsFormEntity("option 4")
    )

    override fun getListFormView(): Array<FormView> {
        val list = mutableListOf<FormView>()
        list.add(FormOptionView(0, "titulo titulo titulo", "continuar", options))
        list.add(FormProductView(1, "Qual o nome de quem você irá transferir?", "continuar",{true}))
        return list.toTypedArray()
    }

    override fun getViewPager(): ViewPagerCustomDuration {
        return findViewById(R.id.viewpagercustomduration)
    }

    override fun whenFinishedForm(map: HashMap<Int, String>) {
    }

    override fun onBackPressed() {
        if (!(findViewById<ViewPager>(R.id.viewpagercustomduration).adapter as FormAdapter)
                .onBackPressed())
            super.onBackPressed()
    }
}

class CustomAdapter(list : Array<FormView> , ac : AppCompatActivity) : FormAdapter(list , ac) {

    override fun getItem(position: Int): Fragment {
        if(position == 0) {
            return CustomOption.newInstance(list[position] as FormOptionView)
        }
        return super.getItem(position)
    }

}

class CustomOption : OptionsFormFragment() {

    companion object {
        fun newInstance(formOptions : FormOptionView) : CustomOption {
            return OptionsFormFragment.newInstance(formOptions, CustomOption() )
        }
    }

    override fun onClick(v: View) {
        formOptions.getOptionSelected()?.let {
            Toast.makeText(requireContext(), it.toString() , 1).show()
            super.onClick(v)
        }
    }

}