package com.ubx.formslibrary.widget

import android.content.Context
import android.os.Build
import android.view.View
import android.widget.TextView
import androidx.appcompat.view.ContextThemeWrapper
import com.ubx.formslibrary.listener.ViewListener

class TextWidget(val text: String,
                 override var width: Int,
                 override var height: Int)
    : BaseWidget(width, height) {
    private lateinit var textView: TextView
    private var listener: ViewListener? = null

    override fun getValue(): String {
        return ""
    }

    override fun setError(message: String?) {
        //Do nothing
    }

    override fun createView(context: Context, isSharingRow: Boolean): TextView {
        textView = if (style != null) {
            TextView(ContextThemeWrapper(context, style!!), null, 0)
        } else {
            TextView(context)
        }
        gravity?.let {
            textView.gravity = it
        }
        customizeLinearElement(context, textView)
        textView.text = text
        listener?.let {
            textView.setOnClickListener { _ ->
                it.onClick()
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            textView.id = View.generateViewId()
        }
        return textView
    }

    override fun createUneditableView(context: Context, isSharingRow: Boolean): TextView {
        return createView(context, isSharingRow)
    }

    fun setOnClickListener(listener: ViewListener) {
        this.listener = listener
        if (::textView.isInitialized) {
            textView.setOnClickListener {
                listener.onClick()
            }
        }
    }
}