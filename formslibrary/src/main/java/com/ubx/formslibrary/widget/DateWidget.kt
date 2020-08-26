package com.ubx.formslibrary.widget

import android.app.DatePickerDialog
import android.content.Context
import android.os.Build
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.view.ContextThemeWrapper
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.text.SimpleDateFormat
import java.util.*

class DateWidget(val hint: String,
                 val key: String,
                 val isRequired: Boolean,
                 override var width: Int,
                 override var height: Int)
    : BaseWidget(width, height) {

    private lateinit var textInputLayout: TextInputLayout
    private lateinit var textInputEditText: EditText

    override fun getValue(): String {
        return if (::textInputEditText.isInitialized) {
            textInputEditText.text.toString()
        } else {
            ""
        }
    }

    override fun setError(message: String?) {
        Log.d(TAG, "[$key] $message")
        if (::textInputLayout.isInitialized) {
            textInputLayout.error = message
        }
    }

    override fun isValid(): Boolean {
        return if (isRequired && getValue().isBlank()) {
            Log.d(TAG, "$hint is required.")
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
        textInputEditText.setOnClickListener {
            showDatePicker(context, textInputEditText)
        }

        textInputEditText.hint = hint + (if (isRequired) " *" else "")
        textInputLayout.addView(textInputEditText)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            textInputLayout.id = View.generateViewId()
            textInputEditText.id = View.generateViewId()
        }
        return textInputLayout
    }


    private fun showDatePicker(context: Context, editText: EditText) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            Toast.makeText(context,
                "Sorry, but this is not available in your current android version.",
                Toast.LENGTH_SHORT).show()
            return
        }
        val now = Calendar.getInstance()
        val formatter = SimpleDateFormat(DATE_FORMAT)
        val dpd = DatePickerDialog(context)
        dpd.updateDate(now[Calendar.YEAR], now[Calendar.MONTH], now[Calendar.DAY_OF_MONTH])
        dpd.setOnDateSetListener { _, year, month, dayOfMonth ->
            val cal = Calendar.getInstance()
            cal[Calendar.YEAR] = year
            cal[Calendar.MONTH] = month
            cal[Calendar.DAY_OF_MONTH] = dayOfMonth
            editText.setText(formatter.format(cal.time))
        }
        dpd.show()
    }

    companion object {
        private const val DATE_FORMAT = "MM/dd/yyyy"
    }
}