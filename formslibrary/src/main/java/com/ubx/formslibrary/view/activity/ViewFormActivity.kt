package com.ubx.formslibrary.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ubx.formslibrary.R

class ViewFormActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_form)
        supportActionBar?.hide()
    }
    companion object {
        private const val TAG = "ViewFormActivity"
        fun getIntent(context: Context): Intent {
            return Intent(context, Class.forName("com.ubx.formslibrary.view.activity.ViewFormActivity"))
        }
    }
}