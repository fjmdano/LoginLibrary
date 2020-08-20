package com.ubx.loginlibrary.viewmodel

import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.view.ContextThemeWrapper
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.ubx.formslibrary.model.ParamModel
import com.ubx.formslibrary.model.SignInCredentials
import com.ubx.formslibrary.model.User
import com.ubx.formslibrary.util.DisplayUtil
import com.ubx.formslibrary.util.BaseUIElementUtil
import com.ubx.loginlibrary.LoginActivity
import com.ubx.loginlibrary.LoginHelper
import com.ubx.loginlibrary.helper.LoginParamHelper
import com.ubx.loginlibrary.helper.LoginValuesHelper
import com.ubx.loginlibrary.helper.ThirdPartyConfigHelper
import com.ubx.loginlibrary.helper.UserHelper
import com.ubx.loginlibrary.model.ForgotPasswordElement
import com.ubx.loginlibrary.model.LoginParamModel
import com.ubx.loginlibrary.util.UIElementUtil

class LoginViewModel: ViewModel() {
    private lateinit var callbackManager: CallbackManager
    private val loginParameters = LoginParamHelper.getLoginParam()
    private val googleSignInClient = ThirdPartyConfigHelper.getGoogleSignInClient()

    val googleSignInIntent: MutableLiveData<Intent> by lazy {
        MutableLiveData<Intent>()
    }
    val emailCredentials: MutableLiveData<SignInCredentials> by lazy {
        MutableLiveData<SignInCredentials>()
    }
    val facebookAccessToken: MutableLiveData<AccessToken> by lazy {
        MutableLiveData<AccessToken>()
    }
    val googleIdToken: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val toastMessage: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val isForgotPasswordButtonClicked: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    /**
     * Setup Callback for Facebook LoginManager
     */
    fun setupFacebook() {
        if (!isFacebookIntegrated()) {
            return
        }
        callbackManager = CallbackManager.Factory.create()
        LoginManager.getInstance().registerCallback(callbackManager,
            object : FacebookCallback<LoginResult?> {
                override fun onSuccess(loginResult: LoginResult?) {
                    if (loginResult != null) {
                        toastMessage.value = "[Facebook] Logged in successfully"
                        facebookAccessToken.value = loginResult.accessToken
                    }
                }

                override fun onCancel() {
                    toastMessage.value = "[Facebook] Logged in canceled"
                }

                override fun onError(exception: FacebookException) {
                    toastMessage.value = "[Facebook] Error!"
                    Log.w(TAG, "Facebook sign in failed", exception)
                }
            })
    }

    /**
     * Handle sign in result
     */
    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (isFacebookIntegrated()) {
            callbackManager.onActivityResult(requestCode, resultCode, data)
        }

        if (isFirebaseIntegrated()) {
            // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
            if (requestCode == LoginActivity.RC_SIGN_IN) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                try {
                    // Google Sign In was successful, authenticate with Firebase
                    val account = task.getResult(ApiException::class.java)!!
                    Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
                    googleIdToken.value = account.idToken!!
                } catch (exception: ApiException) {
                    // Google Sign In failed, update UI appropriately
                    Log.w(TAG, "Google sign in failed", exception)
                    toastMessage.value = "Error signing in"
                }
            }
        }
    }

    /**
     * Create Login Page
     * Add the elements specified in loginParameters to the login view
     *
     * @param activity Parent Activity
     * @return LinearLayout containing the login items
     */
    fun createLayoutPage(context: Context): LinearLayout {
        if (loginParameters == null) return LinearLayout(context)
        val linearLayout = if (loginParameters.style != null) {
            LinearLayout(ContextThemeWrapper(context, loginParameters.style!!), null, 0)
        } else {
            LinearLayout(context)
        }
        val inputStyle = LoginParamHelper.getInputStyle()

        linearLayout.orientation = LinearLayout.VERTICAL
        DisplayUtil.customizeConstraintElement(context, linearLayout, loginParameters)

        loginParameters.elements.forEach {
            when(it) {
                is ParamModel.TextElement -> {
                    linearLayout.addView(
                        BaseUIElementUtil.createTextElement(
                            context, it))
                }
                is ParamModel.InputElement -> {
                    if (inputStyle != -1) it.style = inputStyle
                    linearLayout.addView(
                        BaseUIElementUtil.createInputElement(
                            context, it))
                }
                is ParamModel.ImageElement -> {
                    linearLayout.addView(
                        BaseUIElementUtil.createImageElement(
                            context, it))
                }
                is ParamModel.CustomButtonElement -> {
                    val button = BaseUIElementUtil.createCustomButtonElement(
                        context, it)
                    button.setOnClickListener {
                        onClickLoginButton()
                    }
                    linearLayout.addView(button)
                }
                is ParamModel.ButtonElement -> {
                    linearLayout.addView(
                        BaseUIElementUtil.createButtonElement(
                            context, it))
                }
                is LoginParamModel.ThirdPartyFacebook -> {
                    linearLayout.addView(
                        UIElementUtil.createFacebookButton(
                            context, it
                        ))
                }
                is LoginParamModel.ThirdPartyGoogle -> {
                    if (googleSignInClient == null) {
                        println("Google not initialized!")
                    } else {
                        val googleButton = UIElementUtil.createGoogleButton(
                            context, it, googleSignInClient)
                        googleButton.setOnClickListener {
                            googleSignInIntent.value = googleSignInClient.signInIntent
                        }
                        linearLayout.addView(googleButton)
                    }
                }
                is ForgotPasswordElement -> {
                    val button = BaseUIElementUtil.createCustomButtonElement(
                        context, it.button)
                    button.setOnClickListener {
                        isForgotPasswordButtonClicked.value = true
                    }
                    linearLayout.addView(button)
                }
                else -> {
                    toastMessage.value = "Not defined type"
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
    private fun onClickLoginButton() {
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
            emailCredentials.value = SignInCredentials(
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
            toastMessage.value = "Authentication failed"
        }
    }

    companion object {
        const val TAG = "LoginViewModel"
        const val KEY_EMAIL = "email"
        const val KEY_PASSWORD = "password"
    }

}