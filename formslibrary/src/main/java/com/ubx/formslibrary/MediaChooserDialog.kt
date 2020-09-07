package com.ubx.formslibrary

import android.Manifest
import android.app.Dialog
import android.os.Bundle
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import com.ubx.formslibrary.view.activity.UpdateFormActivity

class MediaChooserDialog(var activity: UpdateFormActivity) : Dialog(activity) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.cardview_media_chooser)
        addListener()
    }

    override fun show() {
        super.show()
        this.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        this.window?.setBackgroundDrawableResource(android.R.color.transparent)
        this.window?.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        this.window?.setDimAmount(0.5F)
        this.setCanceledOnTouchOutside(true)
        this.setCancelable(true)
    }

    /**
     * Add button onClickListener
     */
    private fun addListener() {
        val tvGallery = findViewById<TextView>(R.id.tv_from_gallery)
        tvGallery.setOnClickListener {
            this.dismiss()
            activity.checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, PERMISSION_CODE_STORAGE)
        }

        val tvCamera = findViewById<TextView>(R.id.tv_from_camera)
        tvCamera.setOnClickListener {
            this.dismiss()
            activity.checkPermission(Manifest.permission.CAMERA, PERMISSION_CODE_CAMERA)
        }
    }

    companion object {
        private const val PERMISSION_CODE_CAMERA = 100
        private const val PERMISSION_CODE_STORAGE = 101
    }
}