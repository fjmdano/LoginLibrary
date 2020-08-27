package com.ubx.formslibrary.widget.validation

import android.view.View

class CharacterValidationRule(override var label: String,
                              var minimumLimit: Int)
    :BaseValidationRule(label) {

    override fun displayValidationRule(): View {
        TODO("Not yet implemented")
    }

    override fun isValid(string: String): Boolean {
        if (minimumLimit != 0 && string.length < minimumLimit) {
            return false
        }
        return true
    }
}