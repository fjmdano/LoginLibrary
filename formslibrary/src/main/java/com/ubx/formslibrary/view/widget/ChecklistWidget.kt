package com.ubx.formslibrary.view.widget

import android.content.Context
import android.os.Build
import android.util.Log
import android.view.View
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.view.ContextThemeWrapper
import com.ubx.formslibrary.helper.FormValueHelper

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

    override fun getValue(): List<String> {
        val selectedOptions: MutableList<String> = mutableListOf()
        selectedIndices.forEach {
            selectedOptions.add(options[it])
        }
        return selectedOptions
    }

    override fun getStoredValue(): List<String> {
        return FormValueHelper.getList(key)
    }

    override fun getKeyValue(): Map<String, List<String>> {
        return mapOf(key to getValue())
    }

    override fun setError(message: String?) {
        message?.let {
            Log.d(TAG, "[$key] $it")
        }
        //TODO("Not yet implemented")
    }

    override fun isValid(): Boolean {
        return true
    }

    override fun createView(context: Context, isSharingRow: Boolean): View {
        linearLayout = createLayoutAndLabel(context)
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

    override fun createUneditableView(context: Context, isSharingRow: Boolean): View {
        linearLayout = createLayoutAndLabel(context)

        options.forEachIndexed { index, string ->
            val checkBox = CheckBox(context)
            checkBox.text = string
            checkBox.isChecked = (index in selectedIndices)
            checkBox.isEnabled = false

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

    private fun createLayoutAndLabel(context: Context): LinearLayout {
        val linearLayout = LinearLayout(context)
        linearLayout.orientation = LinearLayout.VERTICAL
        if (label.isNotBlank()) {
            labelTextView = if (style != null) {
                TextView(ContextThemeWrapper(context, style!!), null, 0)
            } else {
                TextView(context)
            }
            labelTextView.text = label
            customizeLinearElement(context, labelTextView)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                labelTextView.id = View.generateViewId()
            }
            linearLayout.addView(labelTextView)
        }
        return linearLayout
    }
}