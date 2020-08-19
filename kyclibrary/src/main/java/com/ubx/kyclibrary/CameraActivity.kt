package com.ubx.kyclibrary

import android.R.id.message
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.camerakit.CameraKit
import com.camerakit.CameraKitView
import com.ubx.kyclibrary.viewmodel.KYCViewModel


class CameraActivity: AppCompatActivity() {
    lateinit var captureButton: Button
    private lateinit var camera: CameraKitView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)
        supportActionBar?.hide()

        loadCamera()

        captureButton = findViewById(R.id.btn_capture_photo)
        captureButton.setOnClickListener {
            camera.captureImage { _: CameraKitView?, photo: ByteArray ->
                setImageBase64(BitmapFactory.decodeByteArray(photo, 0, photo.size))
            }
        }
    }

    override fun onStart() {
        super.onStart()
        camera.onStart()
    }

    override fun onPause() {
        super.onPause()
        camera.onPause()
    }

    override fun onResume() {
        super.onResume()
        camera.onResume()
    }

    override fun onStop() {
        super.onStop()
        camera.onStop()
    }

    private fun setImageBase64(rawBitmap: Bitmap) {
        val filename = "captured_image.png"
        val stream = this.openFileOutput(filename, Context.MODE_PRIVATE);
        val bitmap = Bitmap.createScaledBitmap(rawBitmap, 300, 500, true)
        bitmap.compress(Bitmap.CompressFormat.PNG, 70, stream)

        stream.close()
        bitmap.recycle()

        val intent = Intent()
        intent.putExtra("image", filename)
        setResult(RESULT_OK, intent)
        finish()
    }

    private fun loadCamera() {
        camera = findViewById(R.id.camera)
        val isBackFacing = intent.extras?.get(ARGS_BACK_FACE) as Boolean
        camera.facing = if (!isBackFacing) {
            CameraKit.FACING_FRONT
        } else {
            CameraKit.FACING_BACK
        }
    }

    companion object {
        const val ARGS_BACK_FACE = "ARGS_BACK_FACE"
        fun getIntent(context: Context, isBackFacing: Boolean): Intent {
            val intent = Intent(context, Class.forName("com.ubx.kyclibrary.CameraActivity"))
            intent.putExtra(ARGS_BACK_FACE, isBackFacing)
            return intent
        }
    }
}