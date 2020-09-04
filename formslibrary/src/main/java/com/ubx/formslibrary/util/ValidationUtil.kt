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
    }
}