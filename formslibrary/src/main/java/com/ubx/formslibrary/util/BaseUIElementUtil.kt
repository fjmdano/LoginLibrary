package com.ubx.formslibrary.util

import android.app.DatePickerDialog
import android.content.Context
import android.os.Build
import android.text.InputFilter
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.view.ContextThemeWrapper
import android.view.View
import android.widget.*
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.ubx.formslibrary.R
import com.ubx.formslibrary.model.ParamModel
import java.text.SimpleDateFormat
import java.util.*

class BaseUIElementUtil {
    companion object {
        private const val TAG = "Util"
        private const val DATE_FORMAT = "MM/dd/yyyy"

        /**
         * Create ImageView
         */
        fun createImageElement(context: Context, element: ParamModel.ImageElement): ImageView {
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
        fun createTextElement(context: Context, element: ParamModel.TextElement): TextView {
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
        fun createInputElement(context: Context, element: ParamModel.InputElement): TextInputLayout {
            val textInputLayout = TextInputLayout(context)
            DisplayUtil.customizeLinearElement(
                context,
                textInputLayout,
                element
            )
            @Suppress("DEPRECATION")
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
            textInputEditText.hint = element.hint + (if (element.isRequired) " *" else "")

            if (element.maximumLength > 0) {
                textInputEditText.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(element.maximumLength))
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
        fun createCustomButtonElement(context: Context, element: ParamModel.CustomButtonElement) : Button {
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
         * Create Button
         */
        fun createButtonElement(context: Context, element: ParamModel.ButtonElement) : Button {
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
            button.setOnClickListener {
                element.listener.onClick()
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                button.id = View.generateViewId()
            }
            return button
        }

        /**
         * Create EditText with Date Picker
         */
        fun createDateElement(context: Context,
                              element: ParamModel.DateElement,
                              isSharingRow: Boolean = false): TextInputLayout {
            val textInputLayout = TextInputLayout(context)
            DisplayUtil.customizeLinearElement(
                context,
                textInputLayout,
                element
            )
            if (isSharingRow) {
                textInputLayout.layoutParams = LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1F)
            }
            textInputLayout.isErrorEnabled = true

            val textInputEditText = if (element.style != null) {
                TextInputEditText(ContextThemeWrapper(context, element.style!!), null, 0)
            } else {
                TextInputEditText(context)
            }
            if (element.gravity != null) {
                textInputEditText.gravity = element.gravity!!
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                textInputEditText.focusable = View.NOT_FOCUSABLE
            }
            textInputEditText.setOnClickListener {
                showDatePicker(context, textInputEditText)
            }

            textInputEditText.hint = element.hint + (if (element.isRequired) " *" else "")

            textInputLayout.addView(textInputEditText)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                textInputLayout.id = View.generateViewId()
                textInputEditText.id = View.generateViewId()
            }
            element.editText = textInputEditText
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
            val dpd: DatePickerDialog = DatePickerDialog(context)
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

        /**
         * Create Dropdown
         */
        fun createDropdownElement(context: Context,
                                  element: ParamModel.DropdownElement,
                                  isSharingRow: Boolean = false) : LinearLayout {
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

            val textView = if (element.style != null) {
                TextView(ContextThemeWrapper(context, element.style!!), null, 0)
            } else {
                TextView(context)
            }
            textView.text = element.hint
            textView.textSize = 11F
            dropdownLinearLayout.addView(textView)

            val spinner = Spinner(context)
            ArrayAdapter(context, android.R.layout.simple_list_item_1, element.choices)
                .also { adapter ->
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spinner.adapter = adapter
                }
            element.spinner = spinner

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                textView.id = View.generateViewId()
                spinner.id = View.generateViewId()
                dropdownLinearLayout.id = View.generateViewId()
            }
            dropdownLinearLayout.addView(spinner)

            return dropdownLinearLayout
        }

        /**
         * Create EditText that shows list choices when clicked
         */
        fun createListElement(context: Context,
                              element: ParamModel.ListElement,
                              isSharingRow: Boolean = false): TextInputLayout {
            val textInputLayout = TextInputLayout(context)
            DisplayUtil.customizeLinearElement(
                context,
                textInputLayout,
                element
            )
            if (isSharingRow) {
                textInputLayout.layoutParams = LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1F)
            }
            textInputLayout.isErrorEnabled = true

            val textInputEditText = if (element.style != null) {
                TextInputEditText(ContextThemeWrapper(context, element.style!!), null, 0)
            } else {
                TextInputEditText(context)
            }
            if (element.gravity != null) {
                textInputEditText.gravity = element.gravity!!
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                textInputEditText.focusable = View.NOT_FOCUSABLE
            }
            element.editText = textInputEditText
            textInputEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0,
                R.drawable.ic_arrow_down_2, 0);

            textInputEditText.hint = element.hint + (if (element.isRequired) " *" else "")

            textInputLayout.addView(textInputEditText)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                textInputLayout.id = View.generateViewId()
                textInputEditText.id = View.generateViewId()
            }
            element.editText = textInputEditText
            return textInputLayout
        }

        /**
         * Create ImageView that when clicked, user can select an existing picture from gallery
         * or new picture via camera
         */
        fun createMediaElement(context: Context,
                               element: ParamModel.MediaElement
        ): ImageView {
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
            imageView.setBackgroundResource(R.drawable.dotted_border)
            imageView.setImageResource(R.drawable.icon_camera)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                imageView.id = View.generateViewId()
            }
            element.imageView = imageView
            return imageView
        }

        /**
         * E-mail validator
         */
        fun isValidEmail(target: CharSequence): Boolean {
            return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target)
                .matches()
        }

        /**
         * Input Text Validator
         */
        fun isValidInput(input: String, regexPositiveStrings: MutableList<String>, regexNegativeStrings: MutableList<String>): Boolean {
            regexPositiveStrings.forEach {
                if (!input.matches(it.toRegex())) {
                    Log.w(TAG, "[INVALID INPUT] Error for this regex: " + it.toRegex())
                    return false
                }
            }
            regexNegativeStrings.forEach {
                if (input.matches(it.toRegex())) {
                    Log.w(TAG, "[INVALID INPUT] Error for this regex: " + it.toRegex())
                    return false
                }
            }
            return true
        }

    }
}