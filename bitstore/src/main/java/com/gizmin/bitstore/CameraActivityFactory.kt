package com.gizmin.bitstore

import android.app.Activity
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.yanzhenjie.album.Album
import com.yanzhenjie.album.AlbumConfig
import com.yanzhenjie.album.AlbumFile
import com.yanzhenjie.album.AlbumLoader

object CameraActivityFactory {


    inline fun startActivityPhoto(ac : Activity,
                                  noinline  listenerCancel : ( () -> Unit)? = null,
                                  crossinline listenerSuccess : ( it : ArrayList<AlbumFile> ) -> Unit) {

        Album.initialize(
                AlbumConfig
                        .newBuilder( ac )
                        .setAlbumLoader(MediaLoader())
                        .build()
        )

        Album
                .image( ac ) // Image selection.
                .singleChoice()
                .camera( true )
                .columnCount( 2 )
                //.checkedList(mAlbumFiles)
                //.filterSize() // Filter the file size.
                //.filterMimeType() // Filter file format.
                .afterFilterVisibility( true ) // Show the filtered files, but they are not available.
                .onResult {
                    listenerSuccess( it )
                }
                .onCancel {
                    listenerCancel?.invoke()
                }
                .start()
    }

}


class MediaLoader : AlbumLoader {

    override fun load(imageView: ImageView, albumFile: AlbumFile) {
        load(imageView, albumFile.path)
    }

    override fun load(imageView: ImageView, urlOrPath: String) {
        //its will recycler the memory
        Glide
                .with(imageView.context)
                .load(urlOrPath)
                //.error( R.drawable )
                //.placeholder( R.drawable... )
                .into(imageView)

    }
}

