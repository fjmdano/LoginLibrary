package com.ubx.loginlibrary.viewmodel

import android.content.Context
import android.widget.LinearLayout
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ubx.formslibrary.listener.ViewListener
import com.ubx.formslibrary.util.ValidationUtil
import com.ubx.loginlibrary.helper.LoginParamHelper
import com.ubx.loginlibrary.helper.ThirdPartyConfigHelper


class ForgotPasswordViewModel: ViewModel() {
    val toastMessage: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    val isSendSuccess: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    val showLoadingAnimation: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }
    private val forgotPasswordElement = LoginParamHelper.getForgotPasswordElement()

    /**
     * Create Layout Page
     * Add the elements specified in forgotPasswordElement to the UI
     *
     * @param activity Parent Activity
     * @return LinearLayout containing the login items
     */
    fun createLayoutPage(context: Context): LinearLayout {
        if (forgotPasswordElement == null) return LinearLayout(context)
        val loginWidget = LoginParamHelper.getLoginWidget()?:return LinearLayout(context)

        val inputStyle = LoginParamHelper.getInputStyle()
        val linearLayout = loginWidget.createView(context) as LinearLayout

        //Add image
        forgotPasswordElement.image?.let {
            linearLayout.addView(it.createView(context))
        }
        //Add header
        forgotPasswordElement.header?.let {
            linearLayout.addView(it.createView(context))
        }
        //Add header
        forgotPasswordElement.subheader?.let {
            linearLayout.addView(it.createView(context))
        }

        //Add input field
        if (inputStyle != -1) forgotPasswordElement.inputField.style = inputStyle
        linearLayout.addView(forgotPasswordElement.inputField.createView(context, false))

        //Add submit button
        forgotPasswordElement.resetButton.setOnClickListener(object: ViewListener {
            override fun onClick() {
                sendPasswordReset()
            }
        })
        linearLayout.addView(forgotPasswordElement.resetButton.createView(context))
        return linearLayout
    }

    /**
     * Send password reset to e-mail
     * Current assumption: Project is using Firebase
     */
    private fun sendPasswordReset() {
        val userEmail = forgotPasswordElement?.inputField?.getValue()
        showLoadingAnimation.value = true
        if (isValidEmail(userEmail)) {
            ThirdPartyConfigHelper.getFirebaseAuth().sendPasswordResetEmail(userEmail!!)
                .addOnSuccessListener {
                    toastMessage.value = "E-mail sent. Please check your email."
                    isSendSuccess.value = true
                    showLoadingAnimation.value = false
                }
                .addOnFailureListener {
                    toastMessage.value = it.localizedMessage
                    showLoadingAnimation.value = false
                }
        } else {
            showLoadingAnimation.value = false
        }
    }

    /**
     * Check if input email is valid
     */
    private fun isValidEmail(userEmail: String?): Boolean {
        var isValid = true
        if (userEmail.isNullOrBlank()) {
            isValid = false
            forgotPasswordElement?.inputField?.setError("This field is required")
        } else if (!ValidationUtil.isValidEmail(userEmail)) {
            isValid = false
            forgotPasswordElement?.inputField?.setError("Please enter valid email")
        } else {
            forgotPasswordElement?.inputField?.setError(null)
        }
        return isValid
    }
}