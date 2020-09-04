package com.ubx.formslibrary.view.widget.validation

import android.view.View

class RegexValidationRule(override var label: String,
                          var regexString: String,
                          var expectedResult: Boolean)
    :BaseValidationRule(label) {

    override fun displayValidationRule(): View {
        TODO("Not yet implemented")
    }

    override fun isValid(string: String): Boolean {
        if (expectedResult != string.matches(regexString.toRegex())) {
            return false
        }
        return true
    }
}