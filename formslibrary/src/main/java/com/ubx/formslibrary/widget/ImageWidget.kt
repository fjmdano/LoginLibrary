package com.ubx.formslibrary.widget

import android.content.Context
import android.os.Build
import android.view.View
import android.widget.ImageView
import androidx.appcompat.view.ContextThemeWrapper
import com.ubx.formslibrary.listener.ViewListener

class ImageWidget(val image: Int,
                  override var width: Int,
                  override var height: Int)
    : BaseWidget(width, height) {
    private lateinit var imageView: ImageView
    private var listener: ViewListener? = null

    override fun getValue(): String {
        return ""
    }

    override fun setError(message: String?) {
        //Do nothing
    }

    override fun createView(context: Context, isSharingRow: Boolean): ImageView {
        imageView = if (style != null) {
            ImageView(ContextThemeWrapper(context, style!!), null, 0)
        } else {
            ImageView(context)
        }
        customizeLinearElement(context, imageView)
        imageView.setImageResource(image)
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
        return createView(context, isSharingRow)
    }

    fun setOnClickListener(listener: ViewListener) {
        this.listener = listener
        if (::imageView.isInitialized) {
            imageView.setOnClickListener {
                listener.onClick()
            }
        }
    }
}