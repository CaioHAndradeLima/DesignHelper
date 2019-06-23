package com.gizmin.designhelper

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.gizmin.bitstore.custom_view.viewpager.ViewPagerCustomDuration
import com.gizmin.bitstore.form_product.*
import com.gizmin.bitstore.form_product.fragment.OptionsFormEntity
import com.gizmin.bitstore.form_product.utils.CpfCnpjValidate.isValid

class MainActivity : AppCompatActivity(), FormMethods {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        isValid("caio@gmail.c")
        FormAdapter.init(this)
    }

    private val options = arrayOf(
        OptionsFormEntity("option 1"),
        OptionsFormEntity("option 2"),
        OptionsFormEntity("option 3"),
        OptionsFormEntity("option 4")
    )

    override fun getListFormView(): Array<FormView> {
        val list = mutableListOf<FormView>()
        list.add(FormProductView(1, "Qual o nome de quem você irá transferir?", "continuar",{true}))
        list.add(FormOptionView(0, "titulo titulo titulo", "continuar", options))
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
