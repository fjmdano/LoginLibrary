package com.ubx.formslibrary.view.widget

import android.content.Context
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Build
import android.text.Editable
import android.text.InputFilter
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.ubx.formslibrary.R
import com.ubx.formslibrary.helper.FormValueHelper
import com.ubx.formslibrary.model.Margins
import com.ubx.formslibrary.util.DisplayUtil
import com.ubx.formslibrary.util.ValidationUtil
import com.ubx.formslibrary.view.widget.validation.BaseValidationRule
import com.ubx.formslibrary.view.widget.validation.CharacterValidationRule
import com.ubx.formslibrary.view.widget.validation.CompareValidationRule
import com.ubx.formslibrary.view.widget.validation.RegexValidationRule

class InputWidget(val hint: String,
                  private val isPassword: Boolean,
                  private val inputType: Int,
                  val key: String,
                  private val isRequired: Boolean,
                  override var width: Int,
                  override var height: Int)
    : BaseWidget(width, height) {

    private lateinit var textInputLayout: TextInputLayout
    private lateinit var textInputEditText: TextInputEditText

    private lateinit var okColorFilter: ColorFilter
    private lateinit var ngColorFilter: ColorFilter

    private var barViews: MutableList<View> = mutableListOf()
    private var ruleCircleViews: MutableList<View> = mutableListOf()
    private var validationRules: MutableList<BaseValidationRule> = mutableListOf()
    private var isValidationBarsDisplayed = false
    private var isValidationRulesDisplayed = false
    private var maximumLength: Int = 0
    private var value: String = ""

    override fun getValue(): String {
        value = if (::textInputEditText.isInitialized) {
            textInputEditText.text.toString()
        } else {
            ""
        }
        return value
    }

    override fun getStoredValue(): String {
        return FormValueHelper.getString(key)
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
        val value = getValue()

        return if (isRequired && value.isBlank()) {
            setError("$hint is required.")
            false
        } else if (inputType == (InputType.TYPE_CLASS_TEXT or
                    InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS) &&
                !ValidationUtil.isValidEmail(value)) {
            setError("Not a valid email.")
            false
        }
        else if (checkValidationRules(value) != validationRules.size) {
            //setError("$hint is not valid.")
            false
        } else {
            setError(null)
            true
        }
    }

    override fun createView(context: Context, isSharingRow: Boolean): View {
        textInputLayout = createTextInputLayout(context, isSharingRow)
        textInputEditText = createTextInputEditText(context)

        textInputLayout.hint = hint + (if (isRequired) " *" else "")
        if (isPassword) {
            textInputLayout.endIconMode = TextInputLayout.END_ICON_PASSWORD_TOGGLE
        }
        @Suppress("DEPRECATION")
        textInputLayout.isPasswordVisibilityToggleEnabled = isPassword
        textInputEditText.inputType = inputType
        textInputEditText.setText(getStoredValue())
        if (maximumLength > 0) {
            textInputEditText.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(maximumLength))
        }

        textInputLayout.addView(textInputEditText)

        return if (!(isValidationBarsDisplayed || isValidationRulesDisplayed)) {
             textInputLayout
        } else {
            //Have an onchange listener
            textInputEditText.addTextChangedListener(object: TextWatcher {
                override fun afterTextChanged(s: Editable?) {}
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    checkValidationRules(s.toString(), false)
                }
            })
            createValidationBarsAndRules(context, textInputLayout)
        }
    }

    override fun createUneditableView(context: Context, isSharingRow: Boolean): TextInputLayout {
        val textInputLayout = createTextInputLayout(context, isSharingRow)
        val textInputEditText = createFixedEditText(context, hint, getStoredValue())
        textInputLayout.addView(textInputEditText)
        return textInputLayout
    }

    /**
     * Set Character Length for Input
     * @param label validation label; also be displayed in case rule is not met
     * @param minimumLength expected minimum length of input
     * @param maximumLength expected maximum length of input
     */
    fun setCharacterLength(label: String, minimumLength: Int, maximumLength: Int) {
        this.maximumLength = maximumLength
        if (::textInputEditText.isInitialized && maximumLength > 0) {
            textInputEditText.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(maximumLength))
        }
        validationRules.add(CharacterValidationRule(
            if (label.isBlank()) {
                if (minimumLength != 0) {
                    if (maximumLength != 0) {
                        "$hint should be $minimumLength-$maximumLength characters"
                    } else {
                        "$hint should have at least $minimumLength characters"
                    }
                } else {
                    label
                }
            } else {
                label
            },
            minimumLength
        ))
    }


    /**
     * Add Validation Rule: Compare value to another input
     * @param label validation label; also be displayed in case rule is not met
     * @param compareToWidget widget in comparison with
     */
    fun addCompareRule(label: String, compareToWidget: InputWidget) {
        validationRules.add(CompareValidationRule(
            if (label.isBlank()) "$hint should be same with ${compareToWidget.hint}" else label,
            compareToWidget
        ))
    }

    /**
     * Add Validation Rule: Regex checking
     * @param label validation label; also be displayed in case rule is not met
     * @param regexString regular expression to be compared with
     * @param expectedResult expected result after compared with regexString
     */
    fun addRegexRule(label: String, regexString: String, expectedResult: Boolean = true) {
        validationRules.add(RegexValidationRule(
            if (label.isBlank()) "$hint is not valid" else label,
            regexString,
            expectedResult
        ))
    }

    /**
     * Display Validation Rules in UI
     * @param isDisplayed validation rules are displayed in UI if true
     */
    fun displayValidationRules(isDisplayed: Boolean = true) {
        isValidationRulesDisplayed = isDisplayed
    }

    /**
     * Display Validation Bars in UI
     * @param isDisplayed validation bars are displayed in UI if true
     */
    fun displayValidationBars(isDisplayed: Boolean = true) {
        isValidationBarsDisplayed = isDisplayed
    }

    private fun createValidationBarsAndRules(context: Context,
                                             textInputLayout: TextInputLayout): LinearLayout {
        val linearLayout = LinearLayout(context)
        linearLayout.orientation = LinearLayout.VERTICAL
        linearLayout.addView(textInputLayout)

        if (isValidationBarsDisplayed) {
            linearLayout.addView(createValidationBars(context))
        }
        if (isValidationRulesDisplayed) {
            linearLayout.addView(createValidationRules(context))
        }
        return linearLayout
    }

    private fun createValidationBars(context: Context): LinearLayout {
        val barLinearLayout = LinearLayout(context)
        barLinearLayout.orientation = LinearLayout.HORIZONTAL
        barLinearLayout.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            DisplayUtil.sizeInDP(context, BAR_HEIGHT))

        validationRules.forEachIndexed {i, _ ->
            val barView = View(context)
            barView.layoutParams = LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1F
            )
            barView.setBackgroundColor(Color.parseColor(COLOR_NG))

            if (i != 0) {
                DisplayUtil.setMargins(context, barView,
                    Margins(DisplayUtil.sizeInDP(context, BAR_SPACING), 0, 0, 0))
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                barView.id = View.generateViewId()
            }
            barViews.add(barView)
            barLinearLayout.addView(barView)
        }

        DisplayUtil.setMargins(context, barLinearLayout,
            Margins(0, DisplayUtil.sizeInDP(context, RULE_SPACING), 0, 0))
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            barLinearLayout.id = View.generateViewId()
        }
        return barLinearLayout
    }

    private fun createValidationRules(context: Context): LinearLayout {
        val rulesLinearLayout = LinearLayout(context)
        rulesLinearLayout.orientation = LinearLayout.VERTICAL
        rulesLinearLayout.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        okColorFilter = PorterDuffColorFilter(
            ContextCompat.getColor(context, R.color.lime_alpha),
            PorterDuff.Mode.MULTIPLY)
        ngColorFilter = PorterDuffColorFilter(
            ContextCompat.getColor(context, R.color.gray_alpha),
            PorterDuff.Mode.MULTIPLY)

        validationRules.forEach {
            rulesLinearLayout.addView(createAValidationRule(context, it.label))
        }

        if (isValidationBarsDisplayed) {
            DisplayUtil.setMargins(context, rulesLinearLayout,
                Margins(0, DisplayUtil.sizeInDP(context, 2*RULE_SPACING), 0, 0))
        } else {
            DisplayUtil.setMargins(context, rulesLinearLayout,
                Margins(0, DisplayUtil.sizeInDP(context, RULE_SPACING), 0, 0))
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            rulesLinearLayout.id = View.generateViewId()
        }
        return rulesLinearLayout
    }

    private fun createAValidationRule(context: Context, label: String): LinearLayout {
        val ruleLinearLayout = LinearLayout(context)
        ruleLinearLayout.orientation = LinearLayout.HORIZONTAL
        ruleLinearLayout.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        val circleView = View(context)
        val circleLayoutParams = LinearLayout.LayoutParams(
            DisplayUtil.sizeInDP(context, CIRCLE_DIAMETER),
            DisplayUtil.sizeInDP(context, CIRCLE_DIAMETER)
        )
        circleLayoutParams.gravity = Gravity.CENTER
        circleView.layoutParams = circleLayoutParams
        circleView.setBackgroundResource(R.drawable.layout_circle)
        circleView.background.colorFilter = ngColorFilter

        val textView = TextView(context)
        val textLayoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.FILL_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            1F
        )
        textLayoutParams.gravity = Gravity.START
        textView.layoutParams = textLayoutParams
        textView.text = label

        DisplayUtil.setMargins(context, textView,
            Margins(DisplayUtil.sizeInDP(context, RULE_SPACING), 0, 0, 0))

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            circleView.id = View.generateViewId()
            textView.id = View.generateViewId()
            ruleLinearLayout.id = View.generateViewId()
        }

        ruleCircleViews.add(circleView)
        ruleLinearLayout.addView(circleView)
        ruleLinearLayout.addView(textView)
        return ruleLinearLayout
    }

    private fun checkValidationRules(value: String, displayError: Boolean = true): Int {
        var rulesSatisfied = 0
        var isOK = true
        validationRules.forEachIndexed {idx, it ->
            if (!it.isValid(value)) {
                if (displayError && isOK) setError(it.label)
                if (!(isValidationBarsDisplayed || isValidationRulesDisplayed)) return idx
                isOK = false
                if (isValidationRulesDisplayed && idx < ruleCircleViews.size) {
                    ruleCircleViews[idx].background.colorFilter = ngColorFilter
                }
            } else {
                rulesSatisfied += 1
                if (isValidationRulesDisplayed && idx < ruleCircleViews.size) {
                    ruleCircleViews[idx].background.colorFilter = okColorFilter
                }
            }
        }
        if (isValidationBarsDisplayed) updateValidationBar(rulesSatisfied)
        return rulesSatisfied
    }

    /**
     * Update colors of validation bars based on validation
     */
    private fun updateValidationBar(numberOfOk: Int) {
        for (idx in 0 until numberOfOk) {
            barViews[idx].setBackgroundColor(Color.parseColor(COLOR_OK))
        }
        for (idx in numberOfOk until barViews.size) {
            barViews[idx].setBackgroundColor(Color.parseColor(COLOR_NG))
        }
    }

    companion object {
        private const val BAR_HEIGHT = 10
        private const val BAR_SPACING = 3
        private const val RULE_SPACING = 3
        private const val CIRCLE_DIAMETER = 10
        private const val COLOR_NG = "#D8D8D8"
        private const val COLOR_OK = "#A0E562"
    }

}