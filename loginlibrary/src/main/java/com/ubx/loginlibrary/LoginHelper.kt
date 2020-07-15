package com.ubx.loginlibrary

import android.content.Context
import android.content.Intent
import android.view.ViewGroup
import android.widget.LinearLayout
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.ubx.loginlibrary.model.LoginParamModel
import com.ubx.loginlibrary.model.User
import com.ubx.loginlibrary.util.DisplayUtil
import com.ubx.loginlibrary.helper.LoginParamHelper
import com.ubx.loginlibrary.helper.ThirdPartyConfigHelper
import com.ubx.loginlibrary.helper.UserHelper

class LoginHelper(val context: Context, appName: String,
                  width: Int, height: Int) {
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
    fun setPadding(left: Int, top: Int, right: Int, bottom: Int) {
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
    fun setMargins(left: Int, top: Int, right: Int, bottom: Int) {
        LoginParamHelper.setMargins(left, top, right, bottom)
    }

    /**
     * Set Background of login view
     *
     * @param background background (i.e. R.drawable.*)
     */
    fun setBackground(background: Int) {
        LoginParamHelper.setBackground(background)
    }

    /**
     * Set Style of login view
     *
     * @param style style (i.e. R.style.*)
     */
    fun setStyle(style: Int) {
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
    fun addImage(image: Int, width: Int, height: Int): LoginParamModel.ImageElement {
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
    fun addText(label: String, width: Int, height: Int): LoginParamModel.TextElement {
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
     * @return InputElement that can be customized with style, background, padding and margins
     */
    fun addInput(hint: String, isPassword: Boolean, inputType: Int,
                width: Int, height: Int): LoginParamModel.InputElement {
        return LoginParamHelper.addInput(hint, isPassword, inputType, width, height)
    }

    /**
     * Add a button in the login view
     *
     * @param label button label
     * @param width width of text
     * @param height height of text
     * @return ButtonElement that can be customized with style, background, padding and margins
     */
    fun addButton(label: String, width: Int, height: Int): LoginParamModel.ButtonElement {
        return LoginParamHelper.addButton(label, width, height)
    }

    /**
     * Add Sign in with Google
     *
     * @param width width of text
     * @param height height of text
     * @return Google Sign In Button that can be customized with style, background, padding and margins
     */
    fun addGoogleSignIn(width: Int, height: Int): LoginParamModel.ThirdPartyGoogle {
        return LoginParamHelper.addGoogleSignIn(width, height)
    }

    /**
     * Add Sign in with Facebook
     *
     * @param width width of text
     * @param height height of text
     * @return Facebook Sign In Button that can be customized with style, background, padding and margins
     */
    fun addFacebookSignIn(width: Int, height: Int): LoginParamModel.ThirdPartyFacebook {
        return LoginParamHelper.addFacebookSignIn(width, height)
    }

    /**
     * Get size in DP
     *
     * @param size Int
     * @return size in DP
     */
    fun sizeInDP(size: Int): Int {
        return DisplayUtil.sizeInDP(context, size)
    }

    /**
     * Get intent for login view
     *
     * @return intent
     */
    fun getIntent(): Intent {
        initializeThirdParty()
        return LoginActivity.getIntent(context)
    }

    /**
     * Get currently logged in user
     *
     * @return user with type (can be FACEBOOK, GOOGLE, or OWN) and the account details (based from the auth method)
     */
    fun getUser(): User? {
        initializeThirdParty()
        return UserHelper.getSignedInUser()
    }

    /**
     * Sign out current user
     *
     * @return user with type (can be FACEBOOK, GOOGLE, or OWN) and the account details (based from the auth method)
     */
    fun signOutUser() {
        UserHelper.signOutUser()
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
    fun useFirebase(projectNumber: String, firebaseUrl: String,
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
    fun useFacebook(appID: String, protocolScheme: String) {
        ThirdPartyConfigHelper.setFacebookConfig(appID, protocolScheme)
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

}