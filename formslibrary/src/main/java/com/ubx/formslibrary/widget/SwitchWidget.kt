package com.ubx.formslibrary.widget

import android.content.Context
import android.os.Build
import android.util.Log
import android.view.View
import android.widget.Switch

class SwitchWidget(val label: String,
                   val key: String,
                   val isRequired: Boolean,
                   override var width: Int,
                   override var height: Int)
    : BaseWidget(width, height) {

    var defaultCheck = false
    var textOn = ""
    var textOff = ""
    private lateinit var switch: Switch

    override fun getValue(): String {
        return if (getBooleanValue()) {
            if (textOn.isBlank()) {
                DEFAULT_TEXT_TRUE
            } else {
                textOn
            }
        } else {
            if (textOff.isBlank()) {
                DEFAULT_TEXT_FALSE
            } else {
                textOff
            }
        }
    }

    override fun setError(message: String?) {
        Log.d(TAG, "[$key] $message")
        TODO("Not yet implemented")
    }

    override fun createView(context: Context, isSharingRow: Boolean): View {
        switch = Switch(context)
        switch.text = label
        switch.isChecked = defaultCheck
        if (textOn.isNotBlank()) {
            switch.textOn = textOn
        }
        if (textOff.isNotBlank()) {
            switch.textOff = textOff
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            switch.id = View.generateViewId()
        }
        return switch
    }

    fun getBooleanValue(): Boolean {
        return if (::switch.isInitialized) {
            switch.isChecked
        } else {
            false
        }
    }

    companion object {
        private const val DEFAULT_TEXT_TRUE = "TRUE"
        private const val DEFAULT_TEXT_FALSE = "FALSE"
    }
}