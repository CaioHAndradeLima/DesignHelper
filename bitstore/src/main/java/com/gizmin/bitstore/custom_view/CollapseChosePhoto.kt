package com.gizmin.bitstore.custom_view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Matrix
import android.util.AttributeSet
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.gizmin.bitstore.CameraActivityFactory
import com.gizmin.bitstore.R
import com.gizmin.bitstore.util.DoubleClick
import com.gizmin.bitstore.util.IntentPhotoUtils
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.yanzhenjie.album.AlbumFile
import kotlinx.android.synthetic.main.component_collapse_chose_photo.view.*
import java.io.File
import java.lang.IllegalStateException

class CollapseChosePhoto(
    context: Context,
    attrs: AttributeSet?
) : AppBarLayout(context, attrs),
    OnActivityResultListener {

    private lateinit var bitmap: Bitmap
    private lateinit var fab: FloatingActionButton
    private lateinit var listenerSuccess: (it: ArrayList<AlbumFile>) -> Unit

    private val doubleClick = DoubleClick()

    private val auxPhoto = caiohenrique
        .auxphoto
        .AuxiliarPhoto(context)

    init {
        LayoutInflater
            .from(context)
            .inflate(R.layout.component_collapse_chose_photo, this, true)

        setSettings()
    }

    fun setListenerSuccessPhoto(listenerSuccess: ((it: ArrayList<AlbumFile>) -> Unit)? = null) {
        this.listenerSuccess = { listAlbumFile ->

            val isTheFirstTimeThatUserChosePhoto = !userAlreadyChoseThePhoto()

            UtilsLoaderPhoto
                .loadBitmapFromInternalPath(listAlbumFile[0].path, auxPhoto)
                .doOnNext {

                    bitmap = it

                    decodeFile()
                }.subscribe {

                    if (isTheFirstTimeThatUserChosePhoto) {
                        imageview.layoutParams.height = imageview.width
                        imageview.layoutParams = imageview.layoutParams
                    }

                    imageview.setImageBitmap(bitmap)

                    if (isTheFirstTimeThatUserChosePhoto) {
                        fab.visibility = VISIBLE
                        txtheader.visibility = GONE
                    }

                    listenerSuccess?.invoke(listAlbumFile)
                }
        }
    }

    private fun setSettings() {
        imageview.setOnClickListener { _ ->

            if (doubleClick.isSingleClick()) {
                //IntentPhotoUtils.openGallery( getActivity() )
                CameraActivityFactory.startActivityPhoto(getActivity(), null, listenerSuccess)
            }

        }
    }

    private fun getActivity(): Activity {
        return if (context is ContextThemeWrapper) {
            (context as ContextThemeWrapper).baseContext as Activity
        } else {
            context as Activity
        }
    }

    private fun decodeFile(decode: Int = 270) {
        try {
            val auxPhoto = caiohenrique
                .auxphoto
                .AuxiliarPhoto(context)

            auxPhoto.saveToInternalStorage(DIRECTORY_TEMPORARY, bitmap, NAME_TEMPORARY, 100)

            val f = File("${auxPhoto.path}DIRECTORY_TEMPORARY", NAME_TEMPORARY)

            bitmap = auxPhoto.modifyScaleOfFile(f, decode)

            f.delete()
        } catch (e: Exception) {

        }
    }

    companion object {
        const val DIRECTORY_TEMPORARY = "TEMPORARY"
        const val NAME_TEMPORARY = DIRECTORY_TEMPORARY
    }

    @Deprecated("use now setListenerSuccessPhoto and doesn't call activity result ")
    override fun onActivtyResultPhotoGallery(data: Intent?, requestCode: Int) {

        if (requestCode != IntentPhotoUtils.RESULT_CODE_GALLERY ||
            data == null
        )
            return

        val isTheFirstTimeThatUserChosePhoto = !userAlreadyChoseThePhoto()

        bitmap = IntentPhotoUtils.decodeIntentDataToBitmapAfterUserSelectPhoto(context, data)

        decodeFile()

        if (isTheFirstTimeThatUserChosePhoto) {
            imageview.layoutParams.height = imageview.width
            imageview.layoutParams = imageview.layoutParams
        }

        imageview.setImageBitmap(bitmap)

        if (isTheFirstTimeThatUserChosePhoto) {
            fab.visibility = VISIBLE
            txtheader.visibility = GONE
        }
    }

    fun notifyUseFloatingButtonTurnLeft(container: CoordinatorLayout): CollapseChosePhoto {

        if (id == 0) {
            throw IllegalStateException("need some id on layout for inflate FloatingActionButton")
        }

        if (::fab.isInitialized) {
            throw IllegalStateException("FloatingActionButton already created")
        }

        fab =
            LayoutInflater
                .from(context)
                .inflate(
                    R.layout.component_collapse_floating_turn_left,
                    container,
                    true
                )
                .findViewById(R.id.fab)

        val p = fab.layoutParams as CoordinatorLayout.LayoutParams
        p.anchorId = id
        fab.layoutParams = p

        fab.setOnClickListener {
            whenFabButtonTurnLeftClicked()
        }

        if (userAlreadyChoseThePhoto()) {
            fab.visibility = VISIBLE
        }

        return this
    }

    private fun whenFabButtonTurnLeftClicked() {
        imageview.rotation = imageview.rotation + 90
    }

    fun createBitmap(): Bitmap {
        // recreate the new Bitmap
        if (imageview.getRotation() == 0f)
            return bitmap

        val matrix = Matrix()
        matrix.setRotate(imageview.rotation)

        return Bitmap.createBitmap(
            bitmap,
            0,
            0,
            bitmap.width,
            bitmap.height,
            matrix,
            true
        )
    }

    fun userAlreadyChoseThePhoto(): Boolean {
        return ::bitmap.isInitialized
    }

    fun getTolbar() = findViewById(R.id.toolbar) as androidx.appcompat.widget.Toolbar
}

interface OnActivityResultListener {

    fun onActivtyResultPhotoGallery(data: Intent?, requestCode: Int)
}