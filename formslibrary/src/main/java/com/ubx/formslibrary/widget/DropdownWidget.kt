package com.ubx.formslibrary.widget

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.view.ContextThemeWrapper
import androidx.core.content.ContextCompat
import com.google.android.material.textfield.TextInputLayout

class DropdownWidget(val hint: String,
                     val choices: List<String>,
                     val key: String,
                     val isRequired: Boolean,
                     override var width: Int,
                     override var height: Int)
    : BaseWidget(width, height) {

    private lateinit var spinner: Spinner
    private lateinit var hintTextView: TextView
    private lateinit var errorTextView: TextView
    private var hintColor = Color.GRAY
    private var errorColor = Color.rgb(255, 87, 34)
    private var value: String = ""

    override fun getValue(): String {
        value = if (::spinner.isInitialized) {
            spinner.selectedItem.toString()
        } else {
            ""
        }
        return value
    }

    override fun getKeyValue(): Map<String, String> {
        return mapOf(key to getValue())
    }

    override fun setError(message: String?) {
        if (message != null) {
            Log.d(TAG, "[$key] $message")
            hintTextView.setTextColor(errorColor)
        } else {
            if (::hintTextView.isInitialized) {
                hintTextView.setTextColor(hintColor)
            }
        }
        if (::errorTextView.isInitialized) {
            errorTextView.text = message?:""
        }
    }

    override fun isValid(): Boolean {
        return if (isRequired && getValue().isBlank()) {
            setError("$hint is required.")
            false
        } else {
            setError(null)
            true
        }
    }

    override fun createView(context: Context, isSharingRow: Boolean): LinearLayout {
        val dropdownLinearLayout = LinearLayout(context)
        dropdownLinearLayout.orientation = LinearLayout.VERTICAL

        dropdownLinearLayout.layoutParams = if (isSharingRow) {
            LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1F)
        } else {
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT)
        }

        hintTextView = if (style != null) {
            TextView(ContextThemeWrapper(context, style!!), null, 0)
        } else {
            TextView(context)
        }
        hintTextView.text = "$hint${if (isRequired) " *" else ""}"
        hintTextView.textSize = 11.5F
        hintColor = hintTextView.currentTextColor
        dropdownLinearLayout.addView(hintTextView)

        spinner = Spinner(context)
        ArrayAdapter(context, android.R.layout.simple_list_item_1, choices)
            .also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinner.adapter = adapter
            }
        dropdownLinearLayout.addView(spinner)

        errorTextView = TextView(context)
        errorTextView.setTextColor(errorColor)

        errorTextView.text = ""
        errorTextView.textSize = 11.5F
        dropdownLinearLayout.addView(errorTextView)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            hintTextView.id = View.generateViewId()
            spinner.id = View.generateViewId()
            dropdownLinearLayout.id = View.generateViewId()
        }

        return dropdownLinearLayout
    }

    override fun createUneditableView(context: Context, isSharingRow: Boolean): TextInputLayout {
        val textInputLayout = createTextInputLayout(context, isSharingRow)
        val textInputEditText = createFixedEditText(context, hint, value)
        textInputLayout.addView(textInputEditText)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            textInputLayout.id = View.generateViewId()
            textInputEditText.id = View.generateViewId()
        }
        return textInputLayout
    }
}