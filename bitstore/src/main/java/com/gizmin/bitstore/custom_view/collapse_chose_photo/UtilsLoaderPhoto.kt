package com.gizmin.bitstore.custom_view.collapse_chose_photo

import android.graphics.Bitmap
import android.widget.ImageView
import caiohenrique.auxphoto.AuxiliarPhoto
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.io.File
import java.lang.IllegalStateException

object UtilsLoaderPhoto {

    fun loadPhotoFromStorage(imageView: ImageView,
                             directory: String,
                             nameImage: String,
                             auxPhoto: AuxiliarPhoto =

                                     AuxiliarPhoto(imageView.context)
    ): Disposable {

        return Observable
                .just(auxPhoto)
                .map {
                    it.loadImageFromStorage(
                            directory,
                            nameImage
                    )
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.computation())
                .subscribe { imageView.setImageBitmap(it) }

    }

    fun loadBitmapFromInternalPath( path : String ,
                                    auxPhoto : AuxiliarPhoto) : Observable<Bitmap> {

        val file = File( path )

        if( !file.exists()  ) throw IllegalStateException()

        return Observable
                .just( file )
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn( Schedulers.computation() )
                .map {
                    modifyScale(
                        auxPhoto,
                        it,
                        270
                    )
                }
    }

    private fun modifyScale(auxPhoto : AuxiliarPhoto, file : File, scale : Int ) : Bitmap? {
        return auxPhoto.modifyScaleOfFile( file , scale )
    }

}