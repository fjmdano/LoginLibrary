package com.ubx.loginlibrary.viewmodel

import android.content.Context
import android.os.Build
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.view.ContextThemeWrapper
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ubx.formslibrary.util.BaseUIElementUtil
import com.ubx.formslibrary.util.DisplayUtil
import com.ubx.loginlibrary.helper.LoginParamHelper
import com.ubx.loginlibrary.helper.ThirdPartyConfigHelper


class ForgotPasswordViewModel: ViewModel() {
    val toastMessage: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    val isSendSuccess: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }
    private var userEmail: String = ""
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
        val loginParameters = LoginParamHelper.getLoginParam()?:return LinearLayout(context)

        val inputStyle = LoginParamHelper.getInputStyle()
        val linearLayout = if (loginParameters.style != null) {
            LinearLayout(ContextThemeWrapper(context, loginParameters.style!!), null, 0)
        } else {
            LinearLayout(context)
        }
        linearLayout.orientation = LinearLayout.VERTICAL
        DisplayUtil.customizeConstraintElement(context, linearLayout, loginParameters)

        //Add image
        forgotPasswordElement.image?.let {
            linearLayout.addView(
                BaseUIElementUtil.createImageElement(
                    context, it))
        }
        //Add header
        forgotPasswordElement.header?.let {
            linearLayout.addView(
                BaseUIElementUtil.createTextElement(
                    context, it))
        }
        //Add header
        forgotPasswordElement.subheader?.let {
            linearLayout.addView(
                BaseUIElementUtil.createTextElement(
                    context, it))
        }

        //Add input field
        if (inputStyle != -1) forgotPasswordElement.inputField.style = inputStyle
        val inputField = BaseUIElementUtil.createInputElement(context, forgotPasswordElement.inputField)
        forgotPasswordElement.inputField.editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) { }
            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
                userEmail = s.toString()
            }
        })
        linearLayout.addView(inputField)

        //Add submit button
        val button = BaseUIElementUtil.createCustomButtonElement(context,
            forgotPasswordElement.resetButton)
        button.setOnClickListener {
            sendPasswordReset()
        }
        linearLayout.addView(button)


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            linearLayout.id = View.generateViewId()
        }
        return linearLayout
    }

    private fun sendPasswordReset() {
        if (isValidEmail()) {
            ThirdPartyConfigHelper.getFirebaseAuth().sendPasswordResetEmail(userEmail)
                .addOnSuccessListener {
                    toastMessage.value = "E-mail sent. Please check your email."
                    isSendSuccess.value = true
                }
                .addOnFailureListener {
                    toastMessage.value = it.localizedMessage
                }
        }
    }

    fun isValidEmail(): Boolean {
        var isValid = true
        if (userEmail.isBlank()) {
            isValid = false
            forgotPasswordElement?.inputField?.inputLayout?.error = "This field is required"
        } else if (!BaseUIElementUtil.isValidEmail(userEmail)) {
            isValid = false
            forgotPasswordElement?.inputField?.inputLayout?.error = "Please enter valid email"
        } else {
            forgotPasswordElement?.inputField?.inputLayout?.error = null
        }
        return isValid
    }
}