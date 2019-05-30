package com.gizmin.bitstore.form_product

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.gizmin.bitstore.R
import com.gizmin.bitstore.custom_view.viewpager.ViewPagerCustomDuration
import kotlinx.android.synthetic.main.activity_form_product.*

class FormProductActivity : AppCompatActivity(), FormMethods {
    private val listFormProductView by lazy {
        arrayOf(
            FormProductView(0, "Nome do produto", "Continuar", { it.length > 10 }),
            FormProductView(1, "Porcentagem de Cashback do cliente", "Finalizar", { true })
        )
    }

    override fun getViewPager(): ViewPagerCustomDuration {
        return viewpager as ViewPagerCustomDuration
    }

    override fun whenFinishedForm(map: HashMap<Int, String>) {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_product)

        FormAdapter
            .init(this)
    }

    override fun onBackPressed() {
        if (!((viewpager as ViewPager).adapter as FormAdapter)
                .onBackPressed()
        )
            super.onBackPressed()
    }

    override fun getListFormView(): Array<FormProductView> {
        return listFormProductView
    }
}