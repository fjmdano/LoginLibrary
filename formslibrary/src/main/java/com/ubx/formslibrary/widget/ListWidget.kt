package com.ubx.formslibrary.widget

import android.content.Context
import android.os.Build
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.view.ContextThemeWrapper
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.ubx.formslibrary.R
import com.ubx.formslibrary.listener.ViewListener

class ListWidget(val hint: String,
                 val choices: List<String>,
                 val key: String,
                 private val isRequired: Boolean,
                 override var width: Int,
                 override var height: Int)
    : BaseWidget(width, height) {

    private lateinit var textInputLayout: TextInputLayout
    private lateinit var textInputEditText: EditText
    private var listener: ViewListener? = null

    override fun getValue(): String {
        return if (::textInputEditText.isInitialized) {
            textInputEditText.text.toString()
        } else {
            ""
        }
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
        textInputLayout.isErrorEnabled = true

        textInputEditText = if (style != null) {
            TextInputEditText(ContextThemeWrapper(context, style!!), null, 0)
        } else {
            TextInputEditText(context)
        }
        gravity?.let {
            textInputEditText.gravity = it
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            textInputEditText.focusable = View.NOT_FOCUSABLE
        }
        textInputEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0,
            R.drawable.ic_arrow_down_2, 0)

        textInputEditText.hint = hint + (if (isRequired) " *" else "")

        textInputLayout.addView(textInputEditText)

        listener?.let {
            textInputLayout.setOnClickListener { _ ->
                it.onClick()
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            textInputLayout.id = View.generateViewId()
            textInputEditText.id = View.generateViewId()
        }
        return textInputLayout
    }

    fun setSelected(selected: String) {
        textInputEditText.setText(selected)
    }


    fun setOnClickListener(listener: ViewListener) {
        this.listener = listener
        if (::textInputEditText.isInitialized) {
            textInputEditText.setOnClickListener {
                listener.onClick()
            }
        }
    }
}