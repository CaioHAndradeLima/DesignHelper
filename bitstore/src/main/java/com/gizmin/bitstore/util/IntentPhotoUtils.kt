package com.gizmin.bitstore.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory

object IntentPhotoUtils {


    const val RESULT_CODE_GALLERY = 43

    fun openGallery( activity : Activity) {
        UtilsGallery.openGallery(activity)
    }

    fun decodeIntentDataToBitmapAfterUserSelectPhoto(context : Context, data : Intent) : Bitmap {
        return UtilsGallery.decodeIntentDataToBitmapAfterUserSelectPhoto(context, data)
    }


    private object UtilsGallery {

        fun openGallery( activity : Activity) {
            val intent = Intent(Intent.ACTION_GET_CONTENT);
            intent.type = "image/*";
            activity.startActivityForResult( intent , RESULT_CODE_GALLERY);
        }

        fun decodeIntentDataToBitmapAfterUserSelectPhoto(context : Context, data : Intent) : Bitmap {
            val imInputStream = context
                    .contentResolver
                    .openInputStream(data.data!!)

            return BitmapFactory.decodeStream(imInputStream)

        }

    }
}
