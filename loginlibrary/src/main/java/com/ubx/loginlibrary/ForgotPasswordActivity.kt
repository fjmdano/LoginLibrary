package com.ubx.loginlibrary

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.ubx.loginlibrary.viewmodel.ForgotPasswordViewModel

class ForgotPasswordActivity: AppCompatActivity() {
    private val viewModel: ForgotPasswordViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        supportActionBar?.hide()

        observeViewModelData()
        addPage()
    }

    /**
     * Observe ViewModel Variables
     */
    private fun observeViewModelData() {
        viewModel.toastMessage.observe(this, Observer {
            showToast(it)
        })
        viewModel.isSendSuccess.observe(this, Observer {
            if (it) finish()
        })
    }

    /**
     * Show Toast message
     */
    private fun showToast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    /**
     * Add created linear layout to display
     */
    private fun addPage() {
        val constraintLayout = findViewById<ConstraintLayout>(R.id.forgot_password_layout)

        val linearLayout = viewModel.createLayoutPage(this)
        constraintLayout.addView(linearLayout)

        val constraintSet = ConstraintSet()
        constraintSet.clone(constraintLayout)
        constraintSet.connect(linearLayout.id, ConstraintSet.LEFT, constraintLayout.id, ConstraintSet.LEFT, 0)
        constraintSet.connect(linearLayout.id, ConstraintSet.RIGHT, constraintLayout.id, ConstraintSet.RIGHT, 0)
        constraintSet.connect(linearLayout.id, ConstraintSet.TOP, constraintLayout.id, ConstraintSet.TOP, 0)
        constraintSet.applyTo(constraintLayout)
    }

    companion object {
        private const val TAG = "ForgotPasswordActivity"

        fun getIntent(context: Context): Intent {
            return Intent(context, Class.forName("com.ubx.loginlibrary.ForgotPasswordActivity"))
        }
    }
}