package com.ubx.kyclibrary.util

import android.content.Context
import android.os.Build
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.text.TextUtils
import android.util.Patterns
import android.view.ContextThemeWrapper
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.ubx.kyclibrary.model.KYCParamModel


class UIElementUtil {
    companion object {
        /**
         * Create ImageView
         */
        fun createImageElement(context: Context, element: KYCParamModel.ImageElement): ImageView {
            val imageView = if (element.style != null) {
                ImageView(ContextThemeWrapper(context, element.style!!), null, 0)
            } else {
                ImageView(context)
            }
            DisplayUtil.customizeLinearElement(
                context,
                imageView,
                element
            )
            imageView.setImageResource(element.image)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                imageView.id = View.generateViewId()
            }
            return imageView
        }

        /**
         * Create TextView
         */
        fun createTextElement(context: Context, element: KYCParamModel.TextElement): TextView {
            val textView = if (element.style != null) {
                TextView(ContextThemeWrapper(context, element.style!!), null, 0)
            } else {
                TextView(context)
            }
            if (element.gravity != null) {
                textView.gravity = element.gravity!!
            }
            DisplayUtil.customizeLinearElement(
                context,
                textView,
                element
            )
            textView.text = element.text
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                textView.id = View.generateViewId()
            }
            return textView
        }

        /**
         * Create TextInputLayout with TextInputEditText (for show/hide password functionality
         */
        fun createInputElement(context: Context, element: KYCParamModel.InputElement): TextInputLayout {
            val textInputLayout = TextInputLayout(context)
            DisplayUtil.customizeLinearElement(
                context,
                textInputLayout,
                element
            )
            textInputLayout.isPasswordVisibilityToggleEnabled = element.isPassword
            textInputLayout.isErrorEnabled = true

            val textInputEditText = if (element.style != null) {
                TextInputEditText(ContextThemeWrapper(context, element.style!!), null, 0)
            } else {
                TextInputEditText(context)
            }
            if (element.gravity != null) {
                textInputEditText.gravity = element.gravity!!
            }

            textInputEditText.inputType = element.inputType
            textInputEditText.hint = element.hint

            if (element.maximumLength > 0) {
                textInputEditText.filters = arrayOf<InputFilter>(LengthFilter(element.maximumLength))
            }
            textInputLayout.addView(textInputEditText)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                textInputLayout.id = View.generateViewId()
                textInputEditText.id = View.generateViewId()
            }

            element.inputLayout = textInputLayout
            element.editText = textInputEditText
            return textInputLayout
        }

        /**
         * Create Button
         */
        fun createButtonElement(context: Context, element: KYCParamModel.ButtonElement) : Button {
            val button = if (element.style != null) {
                Button(ContextThemeWrapper(context, element.style!!), null, 0)
            } else {
                Button(context)
            }
            button.isAllCaps = false
            if (element.gravity != null) {
                button.gravity = element.gravity!!
            }
            DisplayUtil.customizeLinearElement(
                context,
                button,
                element
            )
            button.text = element.text

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                button.id = View.generateViewId()
            }
            return button
        }

        /**
         * E-mail validator
         */
        fun isValidEmail(target: CharSequence?): Boolean {
            return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target)
                .matches()
        }

        /**
         * Input Text Validator
         */
        fun isValidInput(input: String, regexPositiveStrings: MutableList<String>, regexNegativeStrings: MutableList<String>): Boolean {
            regexPositiveStrings.forEach {
                if (!input.matches(it.toRegex())) {
                    println("[TEXT] [ERROR] Error for this regex: " + it.toRegex())
                    return false
                }
            }
            regexNegativeStrings.forEach {
                if (input.matches(it.toRegex())) {
                    println("[TEXT] [ERROR] Error for this regex: " + it.toRegex())
                    return false
                }
            }
            return true
        }

    }
}