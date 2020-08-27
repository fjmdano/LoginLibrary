package com.ubx.formslibrary.widget

import android.content.Context
import android.os.Build
import android.util.Log
import android.view.View
import androidx.appcompat.view.ContextThemeWrapper
import androidx.appcompat.widget.SwitchCompat

class SwitchWidget(val label: String,
                   val key: String,
                   val isRequired: Boolean,
                   override var width: Int,
                   override var height: Int)
    : BaseWidget(width, height) {

    var defaultCheck = false
    var textOn = ""
    var textOff = ""
    private lateinit var switch: SwitchCompat

    override fun getValue(): Boolean {
        return if (::switch.isInitialized) {
            switch.isChecked
        } else {
            false
        }
    }

    override fun setError(message: String?) {
        message?.let {
            Log.d(TAG, "[$key] $it")
        }
        TODO("Not yet implemented")
    }

    override fun createView(context: Context, isSharingRow: Boolean): View {
        switch = if (style != null) {
            SwitchCompat(ContextThemeWrapper(context, style!!), null, 0)
        } else {
            SwitchCompat(context)
        }
        customizeLinearElement(context, switch)

        switch.text = label
        switch.isChecked = defaultCheck
        if (textOn.isNotBlank()) {
            switch.showText = true
            switch.textOn = textOn
        }
        if (textOff.isNotBlank()) {
            switch.showText = true
            switch.textOff = textOff
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            switch.id = View.generateViewId()
        }
        return switch
    }

}