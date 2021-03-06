package com.ubx.loginlibrary

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.ViewGroup
import android.widget.LinearLayout
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.ubx.loginlibrary.model.LoginParamModel
import com.ubx.loginlibrary.model.User
import com.ubx.loginlibrary.util.DisplayUtil
import com.ubx.loginlibrary.helper.LoginParamHelper
import com.ubx.loginlibrary.helper.LoginValuesHelper
import com.ubx.loginlibrary.helper.ThirdPartyConfigHelper
import com.ubx.loginlibrary.helper.UserHelper

class LoginHelper(val context: Context, appName: String,
                  width: Int, height: Int): LoginInterface {
    private lateinit var linearLayout: LinearLayout
    private lateinit var googleSignInClient: GoogleSignInClient
    private var viewGroup: ViewGroup? = null
    private var isThirdPartyInitialized = false

    init {
        LoginParamHelper.setLoginParam(appName, width, height)
    }

    /**
     * Set Padding of login view
     *
     * @param left left padding
     * @param top top padding
     * @param right right padding
     * @param bottom bottom padding
     */
    override fun setPadding(left: Int, top: Int, right: Int, bottom: Int) {
        LoginParamHelper.setPadding(left, top, right, bottom)
    }

    /**
     * Set Margins of login view
     *
     * @param left left margins
     * @param top top margins
     * @param right right margins
     * @param bottom bottom margins
     */
    override fun setMargins(left: Int, top: Int, right: Int, bottom: Int) {
        LoginParamHelper.setMargins(left, top, right, bottom)
    }

    /**
     * Set Background of login view
     *
     * @param background background (i.e. R.drawable.*)
     */
    override fun setBackground(background: Int) {
        LoginParamHelper.setBackground(background)
    }

    /**
     * Set Style of login view
     *
     * @param style style (i.e. R.style.*)
     */
    override fun setStyle(style: Int) {
        LoginParamHelper.setStyle(style)
    }

    /**
     * Add an image in the login view
     *
     * @param image image (i.e. R.drawable.*)
     * @param width width of image
     * @param height height of image
     * @return ImageElement that can be customized with style, background, padding and margins
     */
    override fun addImage(image: Int, width: Int, height: Int): LoginParamModel.ImageElement {
        return LoginParamHelper.addImage(image, width, height)
    }

    /**
     * Add a text in the login view
     *
     * @param label text label
     * @param width width of text
     * @param height height of text
     * @return TextElement that can be customized with style, background, padding and margins
     */
    override fun addText(label: String, width: Int, height: Int): LoginParamModel.TextElement {
        return LoginParamHelper.addText(label, width, height)
    }

    /**
     * Add an input text in the login view
     *
     * @param hint input hint
     * @param isPassword true if input text is password, else false
     * @param inputType input type
     * @param width width of text
     * @param height height of text
     * @param key input key - to be used for retrieving the input value
     * @return InputElement that can be customized with style, background, padding and margins
     */
    override fun addInput(hint: String, isPassword: Boolean, inputType: Int,
                width: Int, height: Int, key: String): LoginParamModel.InputElement {
        return LoginParamHelper.addInput(hint, isPassword, inputType, width, height, key)
    }

    /**
     * Add a button in the login view
     *
     * @param label button label
     * @param width width of text
     * @param height height of text
     * @return ButtonElement that can be customized with style, background, padding and margins
     */
    override fun addLoginButton(label: String, width: Int, height: Int): LoginParamModel.LoginButtonElement {
        return LoginParamHelper.addLoginButton(label, width, height)
    }

    /**
     * Add a button (with intent to next activity) in the login view
     *
     * @param label button label
     * @param listener button onclick listener
     * @param width width of text
     * @param height height of text
     * @return ButtonElement that can be customized with style, background, padding and margins
     */
    override fun addButton(label: String, listener: CustomListener, width: Int, height: Int): LoginParamModel.ButtonElement {
        return LoginParamHelper.addButton(label, listener, width, height)
    }

    /**
     * Add Sign in with Google
     *
     * @param width width of text
     * @param height height of text
     * @return Google Sign In Button that can be customized with style, background, padding and margins
     */
    override fun addGoogleSignIn(width: Int, height: Int): LoginParamModel.ThirdPartyGoogle {
        return LoginParamHelper.addGoogleSignIn(width, height)
    }

    /**
     * Add Sign in with Facebook
     *
     * @param width width of text
     * @param height height of text
     * @return Facebook Sign In Button that can be customized with style, background, padding and margins
     */
    override fun addFacebookSignIn(width: Int, height: Int): LoginParamModel.ThirdPartyFacebook {
        return LoginParamHelper.addFacebookSignIn(width, height)
    }

    /**
     * Use Firebase SDK for logging in
     *
     * @param projectNumber project_info["project_number"] in the google-services.json file
     * @param firebaseUrl project_info["firebase_url"] in the google-services.json file
     * @param projectId project_info["projectId"] in the google-services.json file
     * @param storageBucket project_info["storageBucket"] in the google-services.json file
     * @param appId client[0]["client_info"]["mobilesdk_app_id"] in the google-services.json file
     * @param apiKey client[0]["api_key"]["current_key"] in the google-services.json file
     * @param clientId client[0]["oauth_client"]["client_id"] where client_type=3 in the google-services.json file
     */
    override fun useFirebase(projectNumber: String, firebaseUrl: String,
                    projectId: String, storageBucket: String,
                    appId: String, apiKey: String, clientId: String) {
        ThirdPartyConfigHelper.setFirebaseConfig(projectNumber, firebaseUrl,
            projectId, storageBucket, appId, apiKey, clientId)
    }

    /**
     * Use Facebook SDK for logging in
     *
     * @param appID App ID (or facebook_app_id) generated from https://developers.facebook.com/
     * @param protocolScheme Protocol Scheme (or fb_login_protocol_scheme) generated from https://developers.facebook.com/
     */
    override fun useFacebook(appID: String, protocolScheme: String) {
        ThirdPartyConfigHelper.setFacebookConfig(appID, protocolScheme)
    }

    /**
     * Get currently logged in user
     *
     * @return user with type (can be FACEBOOK, GOOGLE, or OWN) and the account details (based from the auth method)
     */
    override fun getUser(): User? {
        initializeThirdParty()
        return UserHelper.getSignedInUser()
    }

    /**
     * Sign out current user
     *
     * @return user with type (can be FACEBOOK, GOOGLE, or OWN) and the account details (based from the auth method)
     */
    override fun signOutUser() {
        UserHelper.signOutUser()
    }

    /**
     * Get intent for login view
     *
     * @return intent
     */
    override fun getIntent(activity: Activity): Intent {
        initializeThirdParty()
        UserHelper.setMainActivity(activity)
        return LoginActivity.getIntent(context)
    }

    override fun getValue(key: String): String {
        return LoginValuesHelper.getValue(key)
    }

    /**
     * Get size in DP
     *
     * @param size Int
     * @return size in DP
     */
    override fun sizeInDP(size: Int): Int {
        return DisplayUtil.sizeInDP(context, size)
    }

    /**
     * Set Custom Sign in Handler
     *
     * @param loginHandler function handler
     */
    override fun setCustomSigninHandler(loginHandler: CustomLoginHandler) {
        UserHelper.setCustomHandler(loginHandler)
    }

    /**
     * Initialize Third Party SDKS if not yet previously done
     */
    private fun initializeThirdParty() {
        if (!isThirdPartyInitialized) {
            ThirdPartyConfigHelper.initializeThirdParty(context)
            isThirdPartyInitialized = true
        }
    }

    interface CustomLoginHandler {
        fun login(): Any?
    }

    interface CustomListener {
        fun onClick()
    }
}