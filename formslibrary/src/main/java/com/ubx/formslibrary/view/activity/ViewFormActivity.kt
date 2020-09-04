package com.ubx.formslibrary.view.activity

import android.content.Context
import android.content.Intent

class ViewFormActivity {

    companion object {
        fun getIntent(context: Context): Intent {
            return Intent(context, Class.forName("com.ubx.formslibrary.view.activity.UpdateFormActivity"))
        }
    }
}