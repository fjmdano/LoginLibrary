package com.ubx.formslibrary.widget

import android.content.Context
import android.os.Build
import android.util.Log
import android.view.View
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.view.ContextThemeWrapper

class ChecklistWidget(val label: String,
                      val options: List<String>,
                      val key: String,
                      val isRequired: Boolean,
                      override var width: Int,
                      override var height: Int)
    : BaseWidget(width, height) {

    private lateinit var linearLayout: LinearLayout
    private lateinit var labelTextView: TextView
    private var checkBoxes: MutableList<CheckBox> = mutableListOf()
    private var selectedIndices: MutableList<Int> = mutableListOf()

    override fun getValue(): String {
        return ""
    }

    override fun setError(message: String?) {
        Log.d(TAG, "[$key] $message")
        TODO("Not yet implemented")
    }

    override fun isValid(): Boolean {
        return true
    }

    override fun createView(context: Context, isSharingRow: Boolean): View {
        linearLayout = LinearLayout(context)
        linearLayout.orientation = LinearLayout.VERTICAL
        if (label.isNotBlank()) {
            labelTextView = if (style != null) {
                TextView(ContextThemeWrapper(context, style!!), null, 0)
            } else {
                TextView(context)
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                labelTextView.id = View.generateViewId()
            }
            linearLayout.addView(labelTextView)
        }
        options.forEachIndexed { index, string ->
            val checkBox = CheckBox(context)
            checkBox.text = string
            checkBox.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    selectedIndices.add(index)
                } else {
                    selectedIndices.remove(index)
                }
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                checkBox.id = View.generateViewId()
            }
            linearLayout.addView(checkBox)
            checkBoxes.add(checkBox)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            linearLayout.id = View.generateViewId()
        }
        return linearLayout
    }

    fun getSelectedOptions(): List<String> {
        val selectedOptions: MutableList<String> = mutableListOf()
        selectedIndices.forEach {
            selectedOptions.add(options[it])
        }
        return selectedOptions
    }
}