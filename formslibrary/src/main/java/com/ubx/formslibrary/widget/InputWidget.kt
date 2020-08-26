package com.ubx.formslibrary.widget

import android.content.Context
import android.os.Build
import android.text.InputFilter
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.view.ContextThemeWrapper
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.ubx.formslibrary.util.BaseUIElementUtil

class InputWidget(val hint: String,
                  private val isPassword: Boolean,
                  private val inputType: Int,
                  override var width: Int,
                  override var height: Int,
                  val key: String,
                  private val isRequired: Boolean)
    : BaseWidget(width, height) {

    private lateinit var textInputLayout: TextInputLayout
    private lateinit var textInputEditText: EditText

    var minimumLength: Int = 0
    var maximumLength: Int = 0
    var regexPositiveValidation: MutableList<String> = mutableListOf()
    var regexNegativeValidation: MutableList<String> = mutableListOf()

    override fun getValue(): String {
        return if (::textInputEditText.isInitialized) {
            textInputEditText.text.toString()
        } else {
            ""
        }
    }

    override fun setError(message: String?) {
        if (::textInputLayout.isInitialized) {
            textInputLayout.error = message
        }
    }

    override fun isValid(): Boolean {
        val value = getValue()

        return if (isRequired && value.isBlank()) {
            Log.d(TAG, "$hint is required.")
            setError("$hint is required.")
            false
        } else if (minimumLength != 0) {
            Log.d(TAG, "$hint should have at least $minimumLength characters")
            setError("$hint should have at least $minimumLength characters")
            false
        } else if (!BaseUIElementUtil.isValidInput(value,
                regexPositiveValidation,
                regexNegativeValidation)
        ) {
            Log.d(TAG, "$hint is not valid.")
            setError("$hint is not valid.")
            false
        } else {
            setError(null)
            true
        }
    }

    override fun createView(context: Context, isSharingRow: Boolean): View {
        textInputLayout = TextInputLayout(context)
        customizeLinearElement(context, textInputLayout)
        if (isSharingRow) {
            textInputLayout.layoutParams = LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1F)
        }

        @Suppress("DEPRECATION")
        textInputLayout.isPasswordVisibilityToggleEnabled = isPassword
        textInputLayout.isErrorEnabled = true

        textInputEditText = if (style != null) {
            TextInputEditText(ContextThemeWrapper(context, style!!), null, 0)
        } else {
            TextInputEditText(context)
        }
        gravity?.let {
            textInputEditText.gravity = it
        }
        textInputEditText.inputType = inputType
        textInputEditText.hint = hint + (if (isRequired) " *" else "")

        if (maximumLength > 0) {
            textInputEditText.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(maximumLength))
        }
        textInputLayout.addView(textInputEditText)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            textInputLayout.id = View.generateViewId()
            textInputEditText.id = View.generateViewId()
        }

        return textInputLayout
    }

}