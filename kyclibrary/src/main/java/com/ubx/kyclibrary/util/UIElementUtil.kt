package com.ubx.kyclibrary.util

import android.R
import android.app.DatePickerDialog
import android.bluetooth.BluetoothClass.Device
import android.content.Context
import android.os.Build
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.view.ContextThemeWrapper
import android.view.View
import android.widget.*
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.ubx.kyclibrary.model.KYCParamModel
import java.text.SimpleDateFormat
import java.util.*


class UIElementUtil {
    companion object {
        private const val TAG = "KYCLibrary"
        private const val DATE_FORMAT = "MM/dd/yyyy"

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
         * Create EditText with Date Picker
         */
        fun createDateElement(context: Context, element: KYCParamModel.DateElement) : LinearLayout {
            val dateLinearLayout = LinearLayout(context)
            dateLinearLayout.orientation = LinearLayout.HORIZONTAL

            dateLinearLayout.layoutParams = LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1F)

            val textView = if (element.style != null) {
                TextView(ContextThemeWrapper(context, element.style!!), null, 0)
            } else {
                TextView(context)
            }

            textView.text = element.hint
            dateLinearLayout.addView(textView)

            val editText = if (element.style != null) {
                EditText(ContextThemeWrapper(context, element.style!!), null, 0)
            } else {
                EditText(context)
            }
            editText.hint = DATE_FORMAT
            editText.setOnClickListener {
                showDatePicker(context, editText)
            }
            element.editText = editText

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                textView.id = View.generateViewId()
                editText.id = View.generateViewId()
                dateLinearLayout.id = View.generateViewId()
            }
            dateLinearLayout.addView(editText)

            return dateLinearLayout
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
            dpd.setOnDateSetListener { view, year, month, dayOfMonth ->
                val cal = Calendar.getInstance()
                cal[Calendar.YEAR] = year
                cal[Calendar.MONTH] = month
                cal[Calendar.DAY_OF_MONTH] = dayOfMonth
                val dateRepresentation = cal.time
                editText.setText(formatter.format(cal.time))
            }
            dpd.show()
        }


        /**
         * Create Dropdown
         */
        fun createDropdownElement(context: Context, element: KYCParamModel.DropdownElement) : LinearLayout {
            val dropdownLinearLayout = LinearLayout(context)
            dropdownLinearLayout.orientation = LinearLayout.HORIZONTAL

            val layoutParams = LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1F)
            dropdownLinearLayout.layoutParams = layoutParams

            val textView = if (element.style != null) {
                TextView(ContextThemeWrapper(context, element.style!!), null, 0)
            } else {
                TextView(context)
            }
            textView.text = element.hint
            dropdownLinearLayout.addView(textView)

            val spinner = Spinner(context)
            ArrayAdapter(context, R.layout.simple_list_item_1, element.choices)
                .also { adapter ->
                    // Specify the layout to use when the list of choices appears
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    // Apply the adapter to the spinner
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