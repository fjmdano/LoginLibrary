package com.ubx.formslibrary.view.widget

import android.content.Context
import android.os.Build
import android.text.InputType
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.view.ContextThemeWrapper
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.ubx.formslibrary.R
import com.ubx.formslibrary.helper.FormParamHelper
import com.ubx.formslibrary.model.Margins
import com.ubx.formslibrary.model.Padding
import com.ubx.formslibrary.util.DisplayUtil

abstract class BaseWidget(open var width: Int, open var height: Int) {
    var style: Int? = null
    var background: Int? = null
    var gravity: Int? = null
    var layoutGravity: Int = Gravity.CENTER_HORIZONTAL
    var padding: Padding? = null
    var margins: Margins? = null

    abstract fun getValue(): Any
    abstract fun getStoredValue(): Any?
    abstract fun setError(message: String?)
    abstract fun createView(context: Context, isSharingRow: Boolean = false): View
    abstract fun createUneditableView(context: Context, isSharingRow: Boolean = false): View

    open fun getKeyValue(): Map<String, Any> {
        return emptyMap()
    }

    open fun isValid(): Boolean {
        return true
    }
    /**
     * Set Paddings of element
     *
     * @param left left padding
     * @param top top padding
     * @param right right padding
     * @param bottom bottom padding
     */
    fun setPadding(left: Int, top: Int, right: Int, bottom: Int) {
        padding = Padding(left, top, right, bottom)
    }

    /**
     * Set Margins of element
     *
     * @param left left margins
     * @param top top margins
     * @param right right margins
     * @param bottom bottom margins
     */
    fun setMargins(left: Int, top: Int, right: Int, bottom: Int) {
        margins = Margins(left, top, right, bottom)
    }

    /**
     * Set width, height, background, padding, margins of the element that is inside a
     * Constraint Layout
     *
     * @param context application/activity context
     * @param view view that will be customized
     */
    fun customizeConstraintElement(context: Context, view: View) {
        val layoutParams = ConstraintLayout.LayoutParams(width, height)
        view.layoutParams = layoutParams
        customizeElement(context, view)
    }

    /**
     * Set width, height, background, padding, margins of the element that is inside a
     * Linear Layout
     *
     * @param context application/activity context
     * @param view view that will be customized
     * @param element UIElement instance that contains the background, padding, margins
     */
    fun customizeLinearElement(context: Context, view: View) {
        val layoutParams = LinearLayout.LayoutParams(width, height)
        layoutParams.gravity = layoutGravity
        view.layoutParams = layoutParams
        customizeElement(context, view)
    }

    /**
     * Set background, padding, margins of the element.
     * This function is called by either customizeConstraintElement() or
     * customizeLinearElement()
     *
     * @param context application/activity context
     * @param view view that will be customized
     */
    private fun customizeElement(context: Context, view: View) {
        background?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                view.background = ContextCompat.getDrawable(context, it)
            } else {
                @Suppress("DEPRECATION")
                view.setBackgroundDrawable(ContextCompat.getDrawable(context, it))
            }
        }
        padding?.let {
            DisplayUtil.setPadding(context, view, it)
        }
        margins?.let {
            DisplayUtil.setMargins(context, view, it)
        }
    }

    fun createFixedEditText(context: Context, hint: String, value: String): TextInputEditText {
        val textInputEditText = TextInputEditText(
            ContextThemeWrapper(
                context,
                if (style != null) style!! else R.style.EditText_DefaultAlpha
            )
        )
        gravity?.let {
            textInputEditText.gravity = it
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            textInputEditText.focusable = View.NOT_FOCUSABLE
        }

        textInputEditText.hint = hint
        textInputEditText.background = null
        textInputEditText.inputType = InputType.TYPE_NULL
        textInputEditText.setText(
            if (value.isEmpty()) " - " else value
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            textInputEditText.id = View.generateViewId()
        }
        return textInputEditText
    }

    fun createTextInputLayout(context: Context, isSharingRow: Boolean,
                              isDropdown: Boolean = false
    ): TextInputLayout {
        val isOutlined = FormParamHelper.shouldOutlinedFields()
        val textInputLayout = TextInputLayout(ContextThemeWrapper(context,
            if (isOutlined) {
                if (isDropdown) {
                    R.style.Widget_MaterialComponents_TextInputLayout_OutlinedBox_ExposedDropdownMenu
                } else {
                    R.style.Widget_MaterialComponents_TextInputLayout_OutlinedBox
                }
            } else {
                if (isDropdown) {
                    R.style.Widget_MaterialComponents_TextInputLayout_FilledBox_ExposedDropdownMenu
                } else {
                    R.style.Widget_MaterialComponents_TextInputLayout_FilledBox
                }
            }
        ))
        customizeLinearElement(textInputLayout.context, textInputLayout)
        if (isSharingRow) {
            textInputLayout.layoutParams = LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1F)
        }
        if (isOutlined) {
            textInputLayout.boxBackgroundMode = TextInputLayout.BOX_BACKGROUND_OUTLINE
            textInputLayout.boxBackgroundColor = ContextCompat.getColor(textInputLayout.context, android.R.color.transparent)
            textInputLayout.setBoxCornerRadii(BOX_RADIUS, BOX_RADIUS, BOX_RADIUS, BOX_RADIUS)
        } else {
            textInputLayout.boxBackgroundMode = TextInputLayout.BOX_BACKGROUND_NONE
        }
        textInputLayout.errorIconDrawable = null
        textInputLayout.isErrorEnabled = true
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            textInputLayout.id = View.generateViewId()
        }
        return textInputLayout
    }

    fun createTextInputEditText(context: Context): TextInputEditText {
        val textInputEditText = TextInputEditText(
            ContextThemeWrapper(
                context,
                if (style != null) style!! else R.style.EditText_DefaultAlpha
            )
        )
        gravity?.let {
            textInputEditText.gravity = it
        }
        textInputEditText.background = null

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            textInputEditText.id = View.generateViewId()
        }
        return textInputEditText
    }

    companion object {
        const val TAG = "WIDGET"
        const val BOX_PADDING = 5
        const val BOX_RADIUS = 10F
    }
}