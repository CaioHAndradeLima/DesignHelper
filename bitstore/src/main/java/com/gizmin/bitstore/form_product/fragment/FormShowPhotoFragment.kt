package com.gizmin.bitstore.form_product.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.gizmin.bitstore.R
import com.gizmin.bitstore.camera.Camera2Activity.Companion.DIRECTORY_FILE
import com.gizmin.bitstore.camera.Camera2Activity.Companion.NAME_FILE
import com.gizmin.bitstore.form_product.FormPageSelected
import com.gizmin.bitstore.form_product.utils.updateButtonStatus

class FormShowPhotoFragment : Fragment(), FormPageSelected {
    lateinit var imageView: ImageView
    lateinit var button: Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_form_show_photo, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        imageView = view.findViewById(R.id.imageview)
        button = view.findViewById(R.id.button)

        button.text = "Continuar"
    }

    override fun onPageSelected() {
        imageView = view!!.findViewById(R.id.imageview)
        val bitmap =             caiohenrique
            .auxphoto
            .AuxiliarPhoto(requireContext())
            .loadImageFromStorage(DIRECTORY_FILE,NAME_FILE)

        imageView.setImageBitmap(
            bitmap
        )

        updateButtonStatus(button, true)
    }
}