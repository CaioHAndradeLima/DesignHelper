package com.gizmin.bitstore.form_product

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.gizmin.bitstore.R
import com.gizmin.bitstore.custom_transform.AccordionTransformer
import com.gizmin.bitstore.custom_view.viewpager.ViewPagerCustomDuration
import com.gizmin.bitstore.custom_view.viewpager.ViewPagerUtils.getFragmentBySupportFragmentManager
import com.gizmin.bitstore.form_product.fragment.LoadingFragment
import com.gizmin.bitstore.form_product.utils.InputTypeUtils
import com.gizmin.bitstore.util.KeyboardUtils
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.lang.IllegalStateException
import java.lang.RuntimeException
import java.util.concurrent.TimeUnit


open class FormAdapter(protected val list: Array<FormProductView>,
                       protected val ac: AppCompatActivity) : FragmentPagerAdapter(
        ac.supportFragmentManager
) {

    var isShowingKeyboard = true

    val map = HashMap<Int,String>()

    companion object {
        fun <T : AppCompatActivity> init(activity: T) {
            if (activity is FormMethods) {
                activity.getViewPager().setPageTransformer(false, AccordionTransformer())
                activity.getViewPager().adapter = FormAdapter(
                        list = activity.getListFormView(),
                        ac = activity
                )
            } else {
                throw RuntimeException("implement FormMethods in your activity")
            }
        }

        fun <T : AppCompatActivity, Y: FormAdapter> init(activity: T, adapter : Y) {
            if (activity is FormMethods) {
                activity.getViewPager().setPageTransformer(false, AccordionTransformer())
                activity.getViewPager().adapter = adapter
            } else {
                throw RuntimeException("implement FormMethods in your activity")
            }
        }
    }

    init {
        val onPageChangeListener = object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                val fragment = getFragmentBySupportFragmentManager(
                        (ac as FormMethods).getViewPager().id,
                        (ac as FormMethods).getViewPager(),
                        position = position
                )

                if(fragment is LoadingFragment &&
                        position == (getCount() - 1)) {

                    hideKeyboardFrom(fragment.view!!.rootView)

                    (ac as FormMethods)
                            .whenFinishedForm(
                                    ((ac as FormMethods)
                                            .getViewPager()
                                            .adapter as FormAdapter).map
                            )


                    return
                }

                (fragment as? FormFragment)?.let {
                    val time = 200L

                    if (!isShowingKeyboard) {
                        hideKeyboardFrom(it.editText)
                        Single
                                .fromCallable { it }
                                .observeOn(Schedulers.newThread())
                                .delay(time, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
                                .subscribe({
                                    showForcedKeyboard()
                                }) { it.printStackTrace() }
                    } else {
                        it.editText.requestFocus()
                    }


                }
            }
        }

        (ac as FormMethods)
                .getViewPager()
                .addOnPageChangeListener(onPageChangeListener)

        KeyboardUtils.addKeyboardToggleListener(
                (ac as FormMethods).getViewPager().context as Activity) {
            isShowingKeyboard = it
        }

    }

    private fun hideKeyboardFrom(view: View) {
        val imm = view.context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    override fun getItem(position: Int): Fragment {

        list.forEach { if (position == it.position) return FormFragment.newInstance(it.position) }

        throw IllegalStateException("not implemented $position")
    }

    override fun getCount(): Int {
        return list.size
    }

    fun onBackPressed(): Boolean {
        val viewpager = (ac as FormMethods).getViewPager()
        if (viewpager.currentItem != 0) {
            viewpager.setCurrentItem(viewpager.currentItem - 1, true)
            return true
        }

        KeyboardUtils.removeAllKeyboardToggleListeners()
        return false
    }

    private fun showForcedKeyboard() {
        val inputMethodManager = ac.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.toggleSoftInputFromWindow(
                ac.window.decorView.applicationWindowToken,
                InputMethodManager.SHOW_FORCED, 0)
    }

    fun add(currentItem: Int, text: String) {
        map.put(currentItem, text)
    }

}

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
                    return it
            }

            throw IllegalStateException()
        }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.holder_form_product, container, false)

        editText = view.findViewById(R.id.edittext)
        textViewTitle = view.findViewById(R.id.txt_title)
        button = view.findViewById(R.id.button)
        editText.inputType = formView.typeKeyboard


        button.setOnClickListener {
            if (button.isEnabled) {

                val viewPager = (editText.context as FormMethods)
                        .getViewPager()

                (viewPager.adapter as FormAdapter)
                        .add(viewPager.currentItem, editText.text.toString())

                if (viewPager.adapter!!.count != viewPager.currentItem - 1) {
                    viewPager.setCurrentItem(viewPager.currentItem + 1, true)
                } else {
                    (editText.context as FormMethods)
                            .whenFinishedForm((viewPager.adapter as FormAdapter).map)
                }
            }
        }

        textViewTitle.text = formView.title
        button.text = formView.nameButton

        editText.addTextChangedListener(this)

        return view
    }

    override fun onResume() {
        super.onResume()
        updateButtonStatus(editText.text.toString())
    }

    override fun afterTextChanged(s: Editable?) {
        updateButtonStatus(s.toString())
    }

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
    }

    fun updateButtonStatus(text : String) {
        if (formView.validation.invoke(text)) {
            button.isEnabled = true
            button.setTextColor(ContextCompat.getColor(editText.context, android.R.color.white))
            button.setBackgroundColor(ContextCompat.getColor(editText.context, R.color.colorPrimary))
        } else {
            button.isEnabled = false
            button.setTextColor(ContextCompat.getColor(editText.context, android.R.color.white))
            button.setBackgroundColor(ContextCompat.getColor(editText.context, R.color.grey))
        }
    }
}

data class FormProductView(
        val position: Int,
        val title: String,
        val nameButton: String,
        val validation: (sequence: String) -> Boolean,
        val typeKeyboard: Int = InputTypeUtils.TEXT
)

interface FormMethods {
    fun getViewPager(): ViewPagerCustomDuration
    fun whenFinishedForm(map: HashMap<Int, String>)
    fun getListFormView(): Array<FormProductView>
}


