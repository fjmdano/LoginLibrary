package com.ubx.kyclibrary

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle

class CameraActivity: Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)
    }

    override fun onStart() {
        super.onStart()

    }

    companion object {
        private const val TAG = "KYCLibrary: CameraActivity"

        fun getIntent(context: Context): Intent {
            return Intent(context, Class.forName("com.ubx.kyclibrary.CameraActivity"))
        }
    }
}