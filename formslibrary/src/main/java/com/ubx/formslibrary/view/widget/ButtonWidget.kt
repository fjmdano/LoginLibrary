package com.ubx.formslibrary.view.widget

import android.content.Context
import android.os.Build
import android.view.View
import android.widget.Button
import androidx.appcompat.view.ContextThemeWrapper
import com.ubx.formslibrary.listener.ViewListener

class ButtonWidget(val text: String,
                   override var width: Int,
                   override var height: Int)
    : BaseWidget(width, height) {

    private lateinit var button: Button
    private var listener: ViewListener? = null
    private var isCustom = false

    override fun getValue(): String {
        return ""
    }

    override fun getStoredValue(): String {
        return ""
    }

    override fun getKeyValue(): Map<String, String> {
        return emptyMap()
    }

    override fun setError(message: String?) {
        //Do nothing
    }

    override fun createView(context: Context, isSharingRow: Boolean): Button {
        button = if (style != null) {
            Button(ContextThemeWrapper(context, style!!), null, 0)
        } else {
            Button(context)
        }
        button.isAllCaps = false
        gravity?.let {
            button.gravity = it
        }
        customizeLinearElement(context, button)
        button.text = text
        listener?.let {
            button.setOnClickListener { _ ->
                it.onClick()
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            button.id = View.generateViewId()
        }
        return button
    }

    override fun createUneditableView(context: Context, isSharingRow: Boolean): Button {
        return createView(context, isSharingRow)
    }

    fun setOnClickListener(listener: ViewListener) {
        this.listener = listener
        if (::button.isInitialized) {
            button.setOnClickListener {
                listener.onClick()
            }
        }
    }

    fun setAsCustom(isCustom: Boolean) {
        this.isCustom = isCustom
    }


    fun checkIfCustom(): Boolean {
        return isCustom
    }
}