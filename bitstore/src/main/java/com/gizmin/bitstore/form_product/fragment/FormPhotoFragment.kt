package com.gizmin.bitstore.form_product.fragment

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.gizmin.bitstore.R
import com.gizmin.bitstore.camera.Camera2Activity
import com.gizmin.bitstore.form_product.FormMethods
import com.gizmin.bitstore.form_product.FormPageSelected
import com.gizmin.bitstore.form_product.FormPhotoView
import com.gizmin.bitstore.form_product.utils.clickForm
import com.gizmin.bitstore.form_product.utils.updateButtonStatus
import com.gizmin.bitstore.util.KeyboardUtils

class FormPhotoFragment : Fragment(), View.OnClickListener, FormPageSelected {

    private lateinit var textViewTitle: TextView
    private lateinit var imageButton: ImageButton
    private lateinit var button: Button

    private lateinit var fpv: FormPhotoView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

            for (formView in (requireActivity() as FormMethods)
                .getListFormView()) {
                if(formView.position == it.getInt(KEY_ENTITY))
                    fpv = formView as FormPhotoView
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_form_photo, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        button = view.findViewById(R.id.button)
        textViewTitle = view.findViewById(R.id.textview_title)
        imageButton = view.findViewById(R.id.imagebutton)

        button.text = fpv.nameButton
        textViewTitle.text = fpv.title
        imageButton.setBackgroundResource(fpv.photo)

        updateButton(false)

        imageButton.setOnClickListener(this)
        button.setOnClickListener(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE_CAMERA && resultCode == RESULT_OK) {
            updateButton(true)
            clickForm(this)
        } else
            super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onClick(v: View) {

        startActivityForResult(Intent(requireActivity(), Camera2Activity::class.java), REQUEST_CODE_CAMERA)
    }

    private fun updateButton(isValid: Boolean) {
        updateButtonStatus(button, isValid)
    }

    override fun onPageSelected() {
        KeyboardUtils.forceCloseKeyboard(requireActivity())
    }

    companion object {
        const val REQUEST_CODE_CAMERA = 245
        private const val KEY_ENTITY = "KEY_E"

        fun newInstance(positionEntityInList: Int): FormPhotoFragment {
            val args = Bundle()
            args.putInt(KEY_ENTITY, positionEntityInList)
            val fragment = FormPhotoFragment()
            fragment.arguments = args
            return fragment
        }
    }

}