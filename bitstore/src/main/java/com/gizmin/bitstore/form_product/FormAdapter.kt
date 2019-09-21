package com.gizmin.bitstore.form_product

import android.app.Activity
import android.os.Parcelable
import android.view.View
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.gizmin.bitstore.R
import com.gizmin.bitstore.custom_transform.AccordionTransformer
import com.gizmin.bitstore.custom_view.viewpager.ViewPagerCustomDuration
import com.gizmin.bitstore.custom_view.viewpager.ViewPagerUtils.getFragmentBySupportFragmentManager
import com.gizmin.bitstore.form_product.fragment.*
import com.gizmin.bitstore.form_product.utils.InputTypeUtils
import com.gizmin.bitstore.util.KeyboardUtils
import com.gizmin.bitstore.util.KeyboardUtils.forceCloseKeyboard
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.parcel.Parcelize
import java.lang.IllegalStateException
import java.lang.RuntimeException
import java.util.concurrent.TimeUnit


open class FormAdapter(
    protected var list: Array<FormView>,
    protected val ac: AppCompatActivity
) : FragmentPagerAdapter(
    ac.supportFragmentManager
), FormAdapterFunctions {
    var isShowingKeyboard = true

    val map = HashMap<Int, String>()

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

        fun <T : AppCompatActivity, Y : FormAdapter> init(activity: T, adapter: Y) {
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
                    (ac as FormMethods).getViewPager(),
                    position = position
                )

                if (fragment is LoadingFragment &&
                    position == (count - 1)
                ) {

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

                if(fragment is OptionsFormFragment ||
                        fragment is ListOptionsFormFragment)
                    hideKeyboardFrom(fragment.view!!.rootView)


                (fragment as? FormPageSelected)?.onPageSelected()
                (ac as? FormPageSelectedListener)?.onPageSelected(position)
            }
        }

        (ac as FormMethods)
            .getViewPager()
            .addOnPageChangeListener(onPageChangeListener)

        KeyboardUtils.addKeyboardToggleListener(
            (ac as FormMethods).getViewPager().context as Activity
        ) {
            isShowingKeyboard = it
        }

    }

    private fun hideKeyboardFrom(view: View) {
        KeyboardUtils.hideKeyboard(view)
    }

    override fun getItem(position: Int): Fragment {

        list.forEach {
            if (position == it.position)
                when (it) {
                    is FormListOptionView -> return ListOptionsFormFragment.newInstance(it)
                    is FormOptionView -> return OptionsFormFragment.newInstance(it)

                    is FormValueView -> return FormValueFragment.newInstance(it.position)
                    is FormProductView -> return FormFragment.newInstance(it.position)

                    is FormPhotoView -> return FormPhotoFragment.newInstance(it.position)
                    is FormShowPhotoView -> return FormShowPhotoFragment()

                    is FormFragmentView -> return it.fragment
                }
        }

        throw IllegalStateException("not implemented $position")
    }

    override fun getCount(): Int {
        return list.size
    }

    fun onBackPressed(): Boolean {
        val viewpager = (ac as FormMethods).getViewPager()
        if (viewpager.currentItem != 0) {


            viewpager.setCurrentItem(getPositionToBackWhenTouchedInBack(viewpager.currentItem), true)
            return true
        }

        KeyboardUtils.removeAllKeyboardToggleListeners()
        return false
    }

    override fun getPositionToBackWhenTouchedInBack(currentPosition: Int): Int {
        val fragment = getFragmentBySupportFragmentManager(
            (ac as FormMethods).getViewPager(),
            position = currentPosition - 1
        )

        if (fragment is LoadingFragment)
            return currentPosition - 2

        return currentPosition - 1
    }

    private fun showForcedKeyboard() {
        forceCloseKeyboard(ac)
    }

    fun add(currentItem: Int, text: String) {
        map[currentItem] = text
    }

    fun notifyListDataChanged(listForm: Array<FormView>) {
        this.list = listForm
        notifyDataSetChanged()
    }
}

interface FormAdapterFunctions {
    fun getPositionToBackWhenTouchedInBack(currentPosition: Int): Int
}

open class FormProductView(
    position: Int,
    title: CharSequence,
    nameButton: String,
    val validation: (sequence: String) -> Boolean,
    val typeKeyboard: Int = InputTypeUtils.TEXT,
    val mask: FormMask = FormMask.NONE,
    var textToEditText:String = ""
) : FormWithTitleAndButton(position, title, nameButton)

enum class FormMask {
    NONE, CPFAndCNPJ
}

class FormValueView(
    position: Int,
    title: String,
    nameButton: String,
    validation: (sequence: String) -> Boolean
) : FormProductView(position, title, nameButton, validation,InputTypeUtils.NUMBER)

open class FormOptionView(
    position: Int,
    title: String,
    nameButton: String,
    val listOptions: Array<OptionsFormEntity>
) : FormWithTitleAndButton(position, title, nameButton) {

    fun getOptionSelected(): OptionsFormEntity? {
        listOptions.forEach { if (it.isChecked) return it }

        return null
    }
}

class FormPhotoView(position: Int,
                    title: String,
                    nameButton: String,
                    @DrawableRes val photo: Int = R.drawable.ic_gallery) : FormWithTitleAndButton(position, title, nameButton)

class FormShowPhotoView(position: Int,
                    title: String,
                    nameButton: String) : FormWithTitleAndButton(position, title, nameButton)

class FormListOptionView(
    position: Int,
    title: String,
    nameButton: String,
    listOptions: Array<OptionsFormEntity>
) : FormOptionView(position, title, nameButton, listOptions)

open class FormWithTitleAndButton(
    position: Int,
    val title: CharSequence,
    val nameButton: String
) :FormView(position)

open class FormView(
    val position: Int
)

class FormFragmentView(
    position: Int,
    val fragment: Fragment
) : FormView(position)


interface FormMethods {
    fun getViewPager(): ViewPagerCustomDuration
    fun whenFinishedForm(map: HashMap<Int, String>)
    fun getListFormView(): Array<FormView>
}


