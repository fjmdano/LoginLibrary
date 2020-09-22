package com.ubx.loginlibrary

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.ubx.loginlibrary.viewmodel.ForgotPasswordViewModel

class ForgotPasswordActivity: AppCompatActivity() {
    private val viewModel: ForgotPasswordViewModel by viewModels()
    private lateinit var loadingView: RelativeLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        loadingView = findViewById(R.id.rl_loading)
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
        viewModel.showLoadingAnimation.observe(this, Observer {
            if (it) {
                showLoadingAnimation()
            } else {
                hideLoadingAnimation()
            }
        })
    }

    /**
     * Show Toast message
     */
    private fun showToast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    /**
     * Display Loading Animation
     */
    private fun showLoadingAnimation() {
        loadingView.visibility = View.VISIBLE
    }

    /**
     * Hide Loading Animation
     */
    private fun hideLoadingAnimation() {
        loadingView.visibility = View.INVISIBLE
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