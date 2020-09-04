package com.ubx.formslibrary.view.widget.validation

import android.view.View

abstract class BaseValidationRule(open var label: String) {
    abstract fun displayValidationRule(): View
    abstract fun isValid(string: String): Boolean

}