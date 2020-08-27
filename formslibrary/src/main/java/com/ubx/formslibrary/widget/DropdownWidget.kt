package com.ubx.formslibrary.widget

import android.content.Context
import android.os.Build
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.view.ContextThemeWrapper

class DropdownWidget(val hint: String,
                     val choices: List<String>,
                     val key: String,
                     val isRequired: Boolean,
                     override var width: Int,
                     override var height: Int)
    : BaseWidget(width, height) {

    private lateinit var spinner: Spinner

    override fun getValue(): String {
        return if (::spinner.isInitialized) {
            spinner.selectedItem.toString()
        } else {
            ""
        }
    }

    override fun setError(message: String?) {
        message?.let {
            Log.d(TAG, "[$key] $it")
        }
        TODO("Not yet implemented")
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

        val textView = if (style != null) {
            TextView(ContextThemeWrapper(context, style!!), null, 0)
        } else {
            TextView(context)
        }
        textView.text = hint
        textView.textSize = 11F
        dropdownLinearLayout.addView(textView)

        spinner = Spinner(context)
        ArrayAdapter(context, android.R.layout.simple_list_item_1, choices)
            .also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinner.adapter = adapter
            }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            textView.id = View.generateViewId()
            spinner.id = View.generateViewId()
            dropdownLinearLayout.id = View.generateViewId()
        }
        dropdownLinearLayout.addView(spinner)

        return dropdownLinearLayout
    }
}