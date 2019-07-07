package com.gizmin.designhelper

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.gizmin.bitstore.custom_view.viewpager.ViewPagerCustomDuration
import com.gizmin.bitstore.form_product.*
import com.gizmin.bitstore.form_product.fragment.OptionsFormEntity
import com.gizmin.bitstore.form_product.fragment.OptionsFormFragment
import com.gizmin.bitstore.form_product.utils.CpfCnpjValidate.isValid
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.random.Random
import androidx.viewpager.widget.PagerAdapter



class MainActivity : AppCompatActivity(), FormMethods {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        isValid("caio@gmail.c")
        getViewPager().isSaveFromParentEnabled = false
        getViewPager().isSaveEnabled = false
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
        list.add(FormOptionView(1, "titulo titulo titulo", "continuar", options))
        list.add(FormProductView(0, "Qual o nome de quem você irá transferir?", "continuar", { true }))

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
}

class CustomAdapter(list: Array<FormView>, ac: AppCompatActivity) : FormAdapter(list, ac) {

    override fun getItem(position: Int): Fragment {
        if (position == 1) {
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


    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        return super.instantiateItem(container, position)
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