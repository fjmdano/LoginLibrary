package com.ubx.formslibrary.view.widget.validation

import android.view.View
import com.ubx.formslibrary.view.widget.InputWidget

class CompareValidationRule(override var label: String,
                              var compareToWidget: InputWidget)
    :BaseValidationRule(label) {

    override fun displayValidationRule(): View {
        TODO("Not yet implemented")
    }

    override fun isValid(string: String): Boolean {
        if (compareToWidget.getValue() != string) {
            return false
        }
        return true
    }
}