package com.ubx.formslibrary.view.widget

import android.content.Context
import android.os.Build
import android.text.InputType
import android.util.Log
import android.view.View
import android.widget.EditText
import com.google.android.material.textfield.TextInputLayout
import com.ubx.formslibrary.R
import com.ubx.formslibrary.helper.FormValueHelper
import com.ubx.formslibrary.listener.ViewListener
import com.ubx.formslibrary.util.DisplayUtil

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
    private var value: String = ""

    override fun getValue(): String {
        return value
    }

    override fun getStoredValue(): String {
        value = FormValueHelper.getString(key)
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
        } else {
            setError(null)
            true
        }
    }

    override fun createView(context: Context, isSharingRow: Boolean): TextInputLayout {
        textInputLayout = createTextInputLayout(context, isSharingRow)
        textInputEditText = createTextInputEditText(context)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            textInputEditText.focusable = View.NOT_FOCUSABLE
        }
        //textInputEditText.compoundDrawablePadding = DisplayUtil.sizeInDP(context, 20)
        /*
        textInputEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0,
            R.drawable.ic_triangle_down, 0)*/
        textInputLayout.endIconMode = TextInputLayout.END_ICON_CUSTOM
        textInputLayout.endIconDrawable = context.resources.getDrawable(R.drawable.ic_triangle_down)
        textInputLayout.hint = hint + (if (isRequired) " *" else "")
        textInputEditText.setText(getStoredValue())
        textInputLayout.addView(textInputEditText)

        listener?.let {
            textInputEditText.inputType = InputType.TYPE_NULL
            textInputEditText.setOnClickListener { _ ->
                it.onClick()
            }
        }
        return textInputLayout
    }

    override fun createUneditableView(context: Context, isSharingRow: Boolean): TextInputLayout {
        val textInputLayout = createTextInputLayout(context, isSharingRow)
        val textInputEditText = createFixedEditText(context, hint, getStoredValue())
        textInputLayout.addView(textInputEditText)
        return textInputLayout
    }

    fun setSelected(selected: String) {
        value = selected
        textInputEditText.setText(selected)
    }


    fun setOnClickListener(listener: ViewListener) {
        this.listener = listener
        if (::textInputEditText.isInitialized) {
            textInputEditText.inputType = InputType.TYPE_NULL
            textInputEditText.setOnClickListener {
                listener.onClick()
            }
        }
    }
}