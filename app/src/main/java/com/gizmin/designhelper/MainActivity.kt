package com.gizmin.designhelper

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.gizmin.bitstore.custom_view.viewpager.ViewPagerCustomDuration
import com.gizmin.bitstore.datepicker.ConfigureDatePicker
import com.gizmin.bitstore.datepicker.DatePickerFragment
import com.gizmin.bitstore.datepicker.DatePickerResult
import com.gizmin.bitstore.form_product.*
import com.gizmin.bitstore.form_product.fragment.OptionsFormEntity
import com.gizmin.bitstore.form_product.fragment.OptionsFormFragment
import com.gizmin.bitstore.util.getSpannableString
import java.util.*
import kotlin.collections.HashMap


class MainActivity : AppCompatActivity(), FormMethods, DatePickerResult {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        FormAdapter.init(
            this, CustomAdapter(getListFormView(), this)
        )

        DatePickerFragment
            .newInstance(this)
            .show(supportFragmentManager, "TAG")
    }

    private val options = arrayOf(
        OptionsFormEntity("option 1"),
        OptionsFormEntity("option 2"),
        OptionsFormEntity("option 3"),
        OptionsFormEntity("option 4")
    )

    override fun getListFormView(): Array<FormView> {

        val title1 = "Qual o"
        val title2 = "nome"
        val title3 = "de quem você irá transferir?"

        val spannableString = getSpannableString(
            this,
            Pair(title1, R.style.textview1),
            Pair(title2, R.style.textview2),
            Pair(title3, R.style.textview1)
        )

        val list = mutableListOf<FormView>()
        list.add(FormProductView(0, spannableString, "Continuar", { true }))
        list.add(FormListOptionView(1, "Escolha a opcao?", "Continuar", options))
        list.add(FormPhotoView(3, "Agora precisamos de uma foto", "Continuar"))
        list.add(FormShowPhotoView(4, "a foto esta boa?", "Continuar"))
        list.add(FormProductView(5, "teste?", "Continuar", { true }))
        list.add(FormOptionView(6, "O que você está sentindo?", "Continuar", options))



        return list.toTypedArray()
    }

    override fun getViewPager(): ViewPagerCustomDuration {
        return findViewById(R.id.viewpagercustomduration)
    }

    override fun whenFinishedForm(map: HashMap<Int, String>) {
    }

    override fun onBackPressed() {
        if (!(findViewById<ViewPager>(R.id.viewpagercustomduration).adapter as FormAdapter)
                .onBackPressed()
        )
            super.onBackPressed()
    }

    override fun configureDatePicker(): ConfigureDatePicker? {
        val oneDay = 86400000
        val maxDate = Date(Date().time + (oneDay * 7))

        return ConfigureDatePicker(
            maxDate,
            Date()
        )
    }

    override fun whenConfirmDate(date: Date) {
    }


}

class CustomAdapter(list: Array<FormView>, ac: AppCompatActivity) : FormAdapter(list, ac) {

    override fun getItem(position: Int): Fragment {
        if (position == 6) {
            return CustomOption.newInstance(list[0] as FormOptionView)
        }

        if (position == 2)
            return if (twoOption) {
                CustomOption.newInstance(list[0] as FormOptionView)
            } else {
                FormFragment.newInstance(0)
            }

        return super.getItem(position)
    }

    private var twoOption = false

    fun enabledTwoOption() {
        twoOption = true
    }

    //this is called when notifyDataSetChanged() is called
    override fun getItemPosition(`object`: Any): Int {
        // refresh all fragments when data set changed
        return PagerAdapter.POSITION_NONE
    }

    override fun getItemId(position: Int): Long {
        if (position == 2) {
            if (twoOption)
                return 3535
            else
                return 23424
        }
        return super.getItemId(position)
    }

    override fun getCount(): Int {
        return super.getCount() + 1
    }

    fun disableTwoOption() {
        twoOption = false
    }

}

class CustomOption : OptionsFormFragment() {

    companion object {
        fun newInstance(formOptions: FormOptionView): CustomOption {
            return OptionsFormFragment.newInstance(formOptions, CustomOption())
        }
    }

    override fun onClick(v: View) {
        formOptions.getOptionSelected()?.let {
            val adapter = ((requireActivity() as FormMethods)
                .getViewPager()
                .adapter as CustomAdapter)

            if (formOptions.listOptions[0] == formOptions.getOptionSelected())
                adapter.enabledTwoOption()
            else
                adapter.disableTwoOption()

            adapter.notifyDataSetChanged()

            super.onClick(v)
        }
    }

}