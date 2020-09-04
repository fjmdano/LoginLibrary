package com.ubx.formslibrary.view.widget

import android.content.Context
import android.os.Build
import android.text.InputType
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.view.ContextThemeWrapper
import com.google.android.material.textfield.TextInputLayout
import com.ubx.formslibrary.R

class DropdownWidget(val hint: String,
                     val choices: List<String>,
                     val key: String,
                     val isRequired: Boolean,
                     override var width: Int,
                     override var height: Int)
    : BaseWidget(width, height) {
    private lateinit var textInputLayout: TextInputLayout
    private var value: String = ""

    override fun getValue(): String {
        return value
    }

    override fun getKeyValue(): Map<String, String> {
        return mapOf(key to getValue())
    }

    override fun setError(message: String?) {
        message?.let {
            Log.d(TAG, "[$key] $it")
        }
        if (::textInputLayout.isInitialized) {
            textInputLayout.error = message
        }
    }

    override fun isValid(): Boolean {
        return if (isRequired && getValue().isBlank()) {
            setError("$hint is required.")
            false
        } else if (getValue() !in choices) {
            setError("$hint is invalid.")
            false
        } else {
            setError(null)
            true
        }
    }

    override fun createView(context: Context, isSharingRow: Boolean): TextInputLayout {
        textInputLayout = createTextInputLayout(context, isSharingRow, true)
        textInputLayout.hint = hint + (if (isRequired) " *" else "")

        val autoTextView = AutoCompleteTextView(
            ContextThemeWrapper(
                context,
                if (style != null) style!! else R.style.AutoCompleteDefaultAlpha
            ))
        ArrayAdapter(context, android.R.layout.simple_list_item_1, choices)
            .also { adapter ->
                //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                autoTextView.setAdapter(adapter)
            }
        autoTextView.inputType = InputType.TYPE_NULL
        autoTextView.background = null
        autoTextView.setOnItemClickListener { parent, _, position, _ ->
            value = parent.getItemAtPosition(position).toString()
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            autoTextView.id = View.generateViewId()
        }
        textInputLayout.addView(autoTextView)
        return textInputLayout
    }

    override fun createUneditableView(context: Context, isSharingRow: Boolean): TextInputLayout {
        val textInputLayout = createTextInputLayout(context, isSharingRow)
        val textInputEditText = createFixedEditText(context, hint, value)
        textInputLayout.addView(textInputEditText)
        return textInputLayout
    }
}