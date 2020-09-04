package com.ubx.formslibrary.view.widget

import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.appcompat.view.ContextThemeWrapper
import com.ubx.formslibrary.R
import com.ubx.formslibrary.listener.ViewListener

class MediaWidget(val hint: String,
                  val key: String,
                  val isRequired: Boolean,
                  override var width: Int,
                  override var height: Int)
    : BaseWidget(width, height) {

    private lateinit var imageView: ImageView
    private lateinit var fixedImageView: ImageView
    private var bitmap: Bitmap? = null
    private var listener: ViewListener? = null

    override fun getValue(): String {
        return ""
    }

    override fun getKeyValue(): Map<String, Bitmap> {
        getBitmap()?.let {
            return mapOf(key to it)
        }
        return emptyMap()
    }

    override fun setError(message: String?) {
        message?.let {
            Log.d(TAG, "[$key] $it")
        }
    }

    override fun isValid(): Boolean {
        return if (isRequired && bitmap == null) {
            setError("$hint is required.")
            false
        } else {
            setError(null)
            true
        }
    }

    override fun createView(context: Context, isSharingRow: Boolean): ImageView {
        imageView = if (style != null) {
            ImageView(ContextThemeWrapper(context, style!!), null, 0)
        } else {
            ImageView(context)
        }
        customizeLinearElement(context, imageView)
        imageView.setBackgroundResource(R.drawable.dotted_border)
        imageView.setImageResource(R.drawable.icon_camera)
        listener?.let {
            imageView.setOnClickListener { _ ->
                it.onClick()
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            imageView.id = View.generateViewId()
        }
        return imageView
    }

    override fun createUneditableView(context: Context, isSharingRow: Boolean): ImageView {
        imageView = if (style != null) {
            ImageView(ContextThemeWrapper(context, style!!), null, 0)
        } else {
            ImageView(context)
        }
        customizeLinearElement(context, imageView)
        imageView.setBackgroundResource(R.drawable.dotted_border)
        imageView.setImageBitmap(bitmap)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            imageView.id = View.generateViewId()
        }
        return imageView
    }

    fun setOnClickListener(listener: ViewListener) {
        this.listener = listener
        if (::imageView.isInitialized) {
            imageView.setOnClickListener {
                listener.onClick()
            }
        }
    }

    fun setBitmap(bitmap: Bitmap) {
        this.bitmap = bitmap
        if (::imageView.isInitialized) {
            imageView.setImageBitmap(bitmap)
        }
    }

    fun getBitmap(): Bitmap? {
        return bitmap
    }
}