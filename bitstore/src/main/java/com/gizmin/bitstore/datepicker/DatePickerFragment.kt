package com.gizmin.bitstore.datepicker

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.afollestad.date.DatePicker
import com.gizmin.bitstore.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.lang.IllegalStateException
import java.util.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import android.widget.FrameLayout


class DatePickerFragment: BottomSheetDialogFragment() {

    val datePickerResult by lazy {
        requireActivity() as DatePickerResult
    }

    private lateinit var dateSelected: Date

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_date_picker_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        view.viewTreeObserver.addOnGlobalLayoutListener {
            val dialog = dialog as BottomSheetDialog
            // androidx should use:             FrameLayout bottomSheet = (FrameLayout)
            val frame = dialog.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
            val behavior = BottomSheetBehavior.from(frame)
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
            behavior.setPeekHeight(0)
        }

        val datePicker = view as DatePicker

        datePickerResult.configureDatePicker()?.let {
            val calendarMaxDate = Calendar.getInstance()
            calendarMaxDate.time = it.maxDate
            datePicker.setMaxDate(calendarMaxDate)

            val calendarMinDate = Calendar.getInstance()
            calendarMinDate.time = it.minDate
            datePicker.setMinDate(calendarMinDate)
        }


        datePicker.addOnDateChanged { previous, date ->
            dateSelected = date.time
            datePicker.clearOnDateChanged()
            datePickerResult.whenConfirmDate(dateSelected)
            dismiss()
        }
    }

    companion object {
        fun newInstance(activity: Activity) : DatePickerFragment {
            if(activity !is DatePickerResult) {
                throw IllegalStateException("implement in your activity the interface DatePickerResult")
            }

            return DatePickerFragment()
        }
    }

}


interface DatePickerResult {
    fun configureDatePicker(): ConfigureDatePicker?
    fun whenConfirmDate(date: Date)
}

data class ConfigureDatePicker(
    var maxDate: Date,
    var minDate: Date
)