package com.ubx.formslibrary.widget

import android.content.Context
import android.os.Build
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.view.ContextThemeWrapper
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
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
        val textInputEditText = if (style != null) {
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

        textInputEditText.hint = hint
        textInputEditText.setText(value)
        return textInputEditText
    }

    fun createTextInputLayout(context: Context, isSharingRow: Boolean): TextInputLayout {
        val textInputLayout = TextInputLayout(context)
        customizeLinearElement(context, textInputLayout)
        if (isSharingRow) {
            textInputLayout.layoutParams = LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1F)
        }
        textInputLayout.isErrorEnabled = true
        return textInputLayout
    }

    companion object {
        const val TAG = "WIDGET"
    }
}