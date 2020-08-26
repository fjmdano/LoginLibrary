package com.ubx.formslibrary.util

import android.text.TextUtils
import android.util.Log
import android.util.Patterns

class ValidationUtil {
    companion object {
        private const val TAG = "Util"

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