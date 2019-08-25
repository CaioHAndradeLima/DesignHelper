package com.gizmin.bitstore.datepicker

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.github.florent37.singledateandtimepicker.SingleDateAndTimePicker
import com.gizmin.bitstore.BuildConfig
import com.gizmin.bitstore.R
import com.gizmin.bitstore.util.DoubleClick
import com.gizmin.bitstore.util.UtilsDate
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.util.*

class BottomSheetDatePicker : BottomSheetDialogFragment() {

    lateinit var viewPager: ViewPager
    private val doubleClick = DoubleClick(200)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val viewContainer = inflater.inflate(R.layout.fragment_bottom_sheet, container, false)
        val adapter = ViewPagerAdapter(childFragmentManager)
        adapter.addFragment(BottomChosseDateFragment.newInstance(Type.FIRST_DATE), "tab1")
        adapter.addFragment(BottomChosseDateFragment.newInstance(Type.SECOND_DATE), "tab2")

        viewPager = (viewContainer.findViewById(R.id.pager) as ViewPager)
        viewPager.adapter = adapter

        return viewContainer
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isCancelable = false
        viewPager.currentItem = 0
        dialog.setOnKeyListener { dialogInterface, keyCode, keyEvent ->
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                onBackPressed()
            } else
                false
        }
    }

    fun whenClickedNext(firstDate: Date) {
        val adapter = (viewPager.adapter as ViewPagerAdapter)
        viewPager.currentItem = 1
        val fragment = adapter.listFragments[1] as BottomChosseDateFragment
        fragment.setMinDate(firstDate)
    }

    fun whenClickedBack() {
        viewPager.currentItem = 0
    }

    fun onBackPressed(): Boolean {
        if (!isVisible) {
            return false
        }

        if (doubleClick.isDoubleClick())
            return true

        if (viewPager.currentItem == 1) {
            viewPager.setCurrentItem(0, true)
            return false
        } else {
            dismiss()
            return true
        }

    }

    companion object {
        fun newInstance() = BottomSheetDatePicker()
    }
}

class ViewPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    val listFragments = mutableListOf<Fragment>()
    private val titles = mutableListOf<String>()

    fun addFragment(fragment: Fragment, tituloAba: String) {
        listFragments.add(fragment)
        titles.add(tituloAba)
    }

    override fun getItem(position: Int): Fragment {
        return listFragments[position]
    }

    override fun getCount(): Int {
        return listFragments.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return titles[position]
    }
}

enum class Type { FIRST_DATE, SECOND_DATE }

class BottomChosseDateFragment : Fragment(), SingleDateAndTimePicker.OnDateChangedListener {
    lateinit var type: Type

    lateinit var button: Button
    lateinit var buttonLast: Button
    lateinit var dateView: SingleDateAndTimePicker
    lateinit var textViewTitle: TextView

    lateinit var dateSelected: Date

    private lateinit var maxDate: Date
    private lateinit var minDate: Date

    companion object {
        private const val KEY_TYPE = "KEY_TYPE"

        fun newInstance(type: Type): BottomChosseDateFragment {
            val instance = BottomChosseDateFragment()
            val bundle = Bundle()
            bundle.putString(KEY_TYPE, type.name)
            instance.type = type
            instance.arguments = bundle
            return instance
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState != null)
            type = Type.valueOf(arguments!!.getString(KEY_TYPE)!!)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return try {
            val view = inflater.inflate(R.layout.fragment_bottom_sheet_date, container, false)
            button = view.findViewById(R.id.button)
            buttonLast = view.findViewById(R.id.button_last)
            textViewTitle = view.findViewById(R.id.txt_title)
            dateView = view.findViewById(R.id.singledatapicker)
            view
        } catch (e: Exception) {
            null
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (type == Type.FIRST_DATE) {
            button.text = "Próximo"
            textViewTitle.text = "Data de início"
            buttonLast.visibility = View.GONE
            maxDate = UtilsDate.getDateBeforeDaysPassedByParam(1)
            dateView.maxDate = maxDate
            //dateView.minDate = UtilsDate.getDateBeforeDaysPassedByParam(365 * 5)
            dateView.setDefaultDate(maxDate)

        } else if (type == Type.SECOND_DATE) {
            button.text = "Confirmar"
            textViewTitle.text = "Data final"
            buttonLast.visibility = View.VISIBLE
            dateView.addOnDateChangedListener(this)
        } else if (BuildConfig.DEBUG) {
            throw NotImplementedError("not implemented type of date ${type.name}")
        }

        button.setOnClickListener {
            dateSelected = dateView.date

            if (dateSelected.after(maxDate) ||
                (type == Type.SECOND_DATE &&
                        ::minDate.isInitialized &&
                        dateSelected.before(minDate))
            ) {
                return@setOnClickListener
            }

            if (type == Type.FIRST_DATE) {
                (requireActivity() as DialogDateListener)
                    .whenClickedNext(dateSelected)
            } else if (type == Type.SECOND_DATE) {
                (requireActivity() as DialogDateListener)
                    .whenClickedFinish(dateSelected)
            } else if (BuildConfig.DEBUG) {
                throw NotImplementedError("not implemented type of date ${type.name}")
            }
        }

        buttonLast.setOnClickListener {
            (requireActivity() as DialogDateListener)
                .whenClickedBackDate()
        }
    }

    override fun onDateChanged(displayed: String?, date: Date?) {
        if (date != null && date.before(minDate)) {
            dateView.setDefaultDate(minDate)
        }
    }

    fun setMinDate(firstDate: Date) {
        maxDate = UtilsDate.getToday()
        minDate = UtilsDate.getDateAfterDaysPassedByParam(0.9F, firstDate)
        dateView.maxDate = maxDate
//        dateView.minDate = firstDate happens bug here
        dateView.setDefaultDate(maxDate)
    }
}

interface DialogDateListener {
    fun whenClickedNext(firstDate: Date)
    fun whenClickedFinish(secondDate: Date)
    fun whenClickedBackDate()
}