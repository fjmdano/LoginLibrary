package com.ubx.loginlibrary

import android.app.Activity
import android.content.Intent
import android.widget.LinearLayout
import com.ubx.formslibrary.model.ParamModel
import com.ubx.formslibrary.model.UIElement
import com.ubx.formslibrary.model.User
import com.ubx.loginlibrary.model.ForgotPasswordElement
import com.ubx.loginlibrary.model.LoginParamModel

interface LoginInterface {

    /*******************[START] CUSTOMIZE MAIN LOGIN LAYOUT ******************************/
    /**
     * Set Padding of login view
     *
     * @param left left padding
     * @param top top padding
     * @param right right padding
     * @param bottom bottom padding
     */
    fun setPadding(left: Int, top: Int, right: Int, bottom: Int)

    /**
     * Set Margins of login view
     *
     * @param left left margins
     * @param top top margins
     * @param right right margins
     * @param bottom bottom margins
     */
    fun setMargins(left: Int, top: Int, right: Int, bottom: Int)

    /**
     * Set Background of login view
     *
     * @param background background (i.e. R.drawable.*)
     */
    fun setBackground(background: Int)

    /**
     * Set Style of login view
     *
     * @param style style (i.e. R.style.*)
     */
    fun setStyle(style: Int)

    /**
     * Set Style of input fields
     *
     * @param style style (i.e. R.style.*)
     */
    fun setInputStyle(style: Int)
    /***********************[END] CUSTOMIZE MAIN LOGIN LAYOUT ****************************/

    /*******************[START] ADD UI ELEMENTS TO LOG_IN VIEW****************************/
    /**
     * Add an image in the login view
     *
     * @param image image (i.e. R.drawable.*)
     * @param width width of image
     * @param height height of image
     * @return ImageElement that can be customized with style, background, padding and margins
     */
    fun addImage(image: Int, width: Int = LinearLayout.LayoutParams.MATCH_PARENT,
                 height: Int = LinearLayout.LayoutParams.WRAP_CONTENT): ParamModel.ImageElement

    /**
     * Add a text in the login view
     *
     * @param label text label
     * @param width width of text
     * @param height height of text
     * @return TextElement that can be customized with style, background, padding and margins
     */
    fun addText(label: String,
                width: Int = LinearLayout.LayoutParams.WRAP_CONTENT,
                height: Int = LinearLayout.LayoutParams.WRAP_CONTENT): ParamModel.TextElement

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
    fun addInput(hint: String, isPassword: Boolean, inputType: Int,
                 width: Int = LinearLayout.LayoutParams.MATCH_PARENT,
                 height: Int = LinearLayout.LayoutParams.WRAP_CONTENT,
                 key: String): ParamModel.InputElement

    /**
     * Add a button in the login view
     *
     * @param label button label
     * @param width width of text
     * @param height height of text
     * @return ButtonElement that can be customized with style, background, padding and margins
     */
    fun addLoginButton(label: String,
                       width: Int = LinearLayout.LayoutParams.MATCH_PARENT,
                       height: Int = LinearLayout.LayoutParams.WRAP_CONTENT): ParamModel.CustomButtonElement

    /**
     * Add a button (with intent to next activity) in the login view
     *
     * @param label button label
     * @param listener button onclick listener
     * @param width width of text
     * @param height height of text
     * @return ButtonElement that can be customized with style, background, padding and margins
     */
    fun addButton(label: String, listener: LoginHelper.CustomListener,
                  width: Int = LinearLayout.LayoutParams.MATCH_PARENT,
                  height: Int = LinearLayout.LayoutParams.WRAP_CONTENT): ParamModel.ButtonElement

    /**
     * Add Sign in with Google
     *
     * @param width width of text
     * @param height height of text
     * @return Google Sign In Button that can be customized with style, background, padding and margins
     */
    fun addGoogleSignIn(width: Int = LinearLayout.LayoutParams.MATCH_PARENT,
                        height: Int = LinearLayout.LayoutParams.WRAP_CONTENT): LoginParamModel.ThirdPartyGoogle

    /**
     * Add Sign in with Facebook
     *
     * @param width width of text
     * @param height height of text
     * @return Facebook Sign In Button that can be customized with style, background, padding and margins
     */
    fun addFacebookSignIn(width: Int = LinearLayout.LayoutParams.MATCH_PARENT,
                          height: Int = LinearLayout.LayoutParams.WRAP_CONTENT): LoginParamModel.ThirdPartyFacebook

    /**
     * Add Forgot Password
     *
     * @param label button label
     * @param imageDrawable width of text
     * @param header UI header
     * @param subheader UI subheader
     */
    fun addForgotPassword(label: String, imageDrawable: Int?,
                          headerText: String,
                          subheaderText: String): ForgotPasswordElement
    /*********************[END] ADD UI ELEMENTS TO LOG_IN VIEW****************************/

    /*********************[START] THIRD PARTY RELATED************************************/

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
                    appId: String, apiKey: String, clientId: String)

    /**
     * Use Facebook SDK for logging in
     *
     * @param appID App ID (or facebook_app_id) generated from https://developers.facebook.com/
     * @param protocolScheme Protocol Scheme (or fb_login_protocol_scheme) generated from https://developers.facebook.com/
     */
    fun useFacebook(appID: String, protocolScheme: String)
    /***********************[END] THIRD PARTY RELATED************************************/

    /***********************[START] USER RELATED*****************************************/
    /**
     * Get currently logged in user
     *
     * @return user with type (can be FACEBOOK, GOOGLE, or OWN) and the account details (based from the auth method)
     */
    fun getUser(): User?

    /**
     * Sign out current user
     *
     * @return user with type (can be FACEBOOK, GOOGLE, or OWN) and the account details (based from the auth method)
     */
    fun signOutUser()
    /*************************[END] USER RELATED*****************************************/

    /***********************[START] INTENT RELATED***************************************/
    /**
     * Get intent for login view
     *
     * @return intent
     */
    fun getIntent(activity: Activity): Intent
    /*************************[END] INTENT RELATED***************************************/

    /**
     * Get value of input text
     *
     * @return string value
     */
    fun getValue(key: String): String

    /**
     * Get size in DP
     *
     * @param size Int
     * @return size in DP
     */
    fun sizeInDP(size: Int): Int

    /**
     * Set Custom Sign in Handler
     *
     * @param loginHandler function handler
     */
    fun setCustomSignInHandler(loginHandler: LoginHelper.CustomLoginHandler)
}