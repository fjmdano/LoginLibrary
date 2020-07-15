package com.ubx.kyclibrary

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.ScrollView
import androidx.appcompat.app.AppCompatActivity
import com.ubx.kyclibrary.viewmodel.KYCViewModel

class KYCActivity: AppCompatActivity() {
    private lateinit var kycViewModel: KYCViewModel
    private lateinit var parentLayout: ScrollView
    private var currentLinearLayout: LinearLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kyc)
        parentLayout = findViewById(R.id.sv_container)
        kycViewModel = KYCViewModel(this)
        displayInView()
    }

    private fun displayInView() {
        if (currentLinearLayout != null) {
            parentLayout.removeView(currentLinearLayout)
            currentLinearLayout = null
        }
        currentLinearLayout = kycViewModel.getNextLayoutPage()
        parentLayout.addView(currentLinearLayout)
    }

    companion object {
        private const val TAG = "KYCLibrary"
        fun getIntent(context: Context): Intent {
            return Intent(context, Class.forName("com.ubx.kyclibrary.KYCActivity"))
        }
    }
}