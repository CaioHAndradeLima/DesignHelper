package com.gizmin.bitstore.photo

import android.os.Bundle
import com.gizmin.bitstore.BaseActivity
import com.gizmin.bitstore.R
import com.gizmin.bitstore.custom_view.CollapseChosePhoto
import kotlinx.android.synthetic.main.photo_activity.*

class PhotoActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.photo_activity)
        val ccp = findViewById<CollapseChosePhoto>(R.id.ccp)
        ccp
                .notifyUseFloatingButtonTurnLeft( container)
                .setListenerSuccessPhoto()

        ccp.getTolbar().title = getString(R.string.activity_photo_title)

    }

}