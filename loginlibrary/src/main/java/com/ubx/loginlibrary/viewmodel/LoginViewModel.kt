package com.ubx.loginlibrary.viewmodel

import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.view.ContextThemeWrapper
import com.google.firebase.auth.FirebaseAuth
import com.ubx.loginlibrary.LoginActivity
import com.ubx.loginlibrary.helper.LoginParamHelper
import com.ubx.loginlibrary.helper.LoginValuesHelper
import com.ubx.loginlibrary.helper.ThirdPartyConfigHelper
import com.ubx.loginlibrary.helper.UserHelper
import com.ubx.loginlibrary.model.LoginParamModel
import com.ubx.loginlibrary.model.User
import com.ubx.loginlibrary.util.*

class LoginViewModel(private val context: Context) {
    private var isFacebookSdkInitialized = false
    private var isFirebaseSdkInitialized = false
    private lateinit var linearLayout: LinearLayout
    private val loginParameters = LoginParamHelper.getLoginParam()!!
    private val googleSignInClient = ThirdPartyConfigHelper.getGoogleSignInClient()

    /**
     * Create Login Page
     * Add the elements specified in loginParameters to the login view
     *
     * @param activity Parent Activity
     * @return LinearLayout containing the login items
     */
    fun createLoginPage(activity: LoginActivity): LinearLayout {
        val linearLayout = if (loginParameters.style != null) {
            LinearLayout(ContextThemeWrapper(context, loginParameters.style!!), null, 0)
        } else {
            LinearLayout(context)
        }

        linearLayout.orientation = LinearLayout.VERTICAL
        DisplayUtil.customizeConstraintElement(context, linearLayout, loginParameters)

        loginParameters.elements.forEach {
            when(it.type) {
                LoginParamModel.ElementType.IMAGE -> {
                    linearLayout.addView(
                        UIElementUtil.createImageElement(
                            context, it.value as LoginParamModel.ImageElement))
                }
                LoginParamModel.ElementType.TEXT -> {
                    linearLayout.addView(
                        UIElementUtil.createTextElement(
                            context, it.value as LoginParamModel.TextElement))
                }
                LoginParamModel.ElementType.EDIT -> {
                    linearLayout.addView(
                        UIElementUtil.createInputElement(
                            context, it.value as LoginParamModel.InputElement))
                }
                LoginParamModel.ElementType.BUTTON -> {
                    if (it.value is LoginParamModel.LoginButtonElement) {
                        val button =
                            UIElementUtil.createLoginButtonElement(
                                context, it.value)
                        button.setOnClickListener {
                            onClickLoginButton(activity)
                        }
                        linearLayout.addView(button)
                    } else if (it.value is LoginParamModel.ButtonElement) {
                        linearLayout.addView(
                            UIElementUtil.createButtonElement(
                                context, it.value))
                    }
                }
                LoginParamModel.ElementType.THIRD_PARTY -> {
                    if (it.value is LoginParamModel.ThirdPartyFacebook) {
                        linearLayout.addView(
                            UIElementUtil.createFacebookButton(
                                context, it.value
                            ))
                    } else if (it.value is LoginParamModel.ThirdPartyGoogle) {
                        if (googleSignInClient == null) {
                            println("Google not initialized!")
                        } else {
                            linearLayout.addView(
                                UIElementUtil.createGoogleButton(
                                    context, it.value, googleSignInClient, activity
                                ))
                        }
                    } else {
                        //Do nothing
                    }
                }
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            linearLayout.id = View.generateViewId()
        }
        return linearLayout
    }

    /**
     * Function that handles on click login button
     *
     */
    private fun onClickLoginButton(activity: LoginActivity) {
        var noError = true
        var value: String
        LoginParamHelper.getInputElements().forEach {
            value = it.editText.text.toString()
            if (value.isBlank()) {
                it.inputLayout.error = it.hint + " is required."
                noError = false
            } else {
                it.inputLayout.error = null
                LoginValuesHelper.setValue(it.key, it.editText.text.toString())
            }
        }

        if (noError) {
            activity.firebaseAuthWithEmail(
                LoginValuesHelper.getValue(KEY_EMAIL),
                LoginValuesHelper.getValue(KEY_PASSWORD))
        }
    }


    /**
     * Retrieve instance of FirebaseAuth
     *
     * @return FirebaseAuth instance
     */
    fun getFirebaseAuth(): FirebaseAuth {
        return ThirdPartyConfigHelper.getFirebaseAuth()
    }

    /**
     * Check if there is currently logged in user
     *
     * @return currently signed in user (null if none)
     */
    fun getUser(): User? {
        return UserHelper.getSignedInUser()
    }

    /**
     * Set signed in user details
     *
     * @param account Can be FirebaseUser, FacebookUser, or json return from provided BE API
     */
    fun setUser(account: Any) {
        return UserHelper.setSignedInUser(account)
    }

    /**
     * Check if app uses Facebook sdk
     */
    fun isFacebookIntegrated(): Boolean {
        return ThirdPartyConfigHelper.isFacebookIntegrated()
    }

    /**
     * Check if app uses Firebase sdk
     */
    fun isFirebaseIntegrated(): Boolean {
        return ThirdPartyConfigHelper.isFirebaseIntegrated()
    }

    /**
     * Get MainActivity intent
     * Called when returning control to app
     */
    fun getMainActivityIntent(): Intent {
        return UserHelper.getMainActivity()!!.intent
    }

    /**
     * Call for custom signin handler
     */
    fun customSignIn() {
        val customHandler = UserHelper.getCustomHandler()
        if (customHandler != null) {
            customHandler.login()
        } else {
            Toast.makeText(context, "Authentication failed", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        const val KEY_EMAIL = "email"
        const val KEY_PASSWORD = "password"
    }

}