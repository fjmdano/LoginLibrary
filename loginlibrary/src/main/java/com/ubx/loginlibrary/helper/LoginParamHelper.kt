package com.ubx.loginlibrary.helper

import com.ubx.formslibrary.model.ParamModel
import com.ubx.formslibrary.model.UIElement
import com.ubx.loginlibrary.LoginHelper
import com.ubx.loginlibrary.datarepository.LoginParamDataRepository
import com.ubx.loginlibrary.model.ForgotPasswordElement
import com.ubx.loginlibrary.model.LoginParamModel

class LoginParamHelper {

    companion object {
        /**
         * Initialize LoginParamModel
         *
         * @param appName Mobile App Name
         * @param width width that the login view can consume
         * @param height height that the login view can consume
         */
        fun setLoginParam(appName: String, width: Int, height: Int) {
            getDataRepo().loginParamModel = LoginParamModel(appName, width, height)
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
            getLoginParam()?.padding = UIElement.Padding(left, top, right, bottom)
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
            getLoginParam()?.margins = UIElement.Margins(left, top, right, bottom)
        }

        /**
         * Set Background of login view
         *
         * @param background background (i.e. R.drawable.*)
         */
        fun setBackground(background: Int) {
            getLoginParam()?.background = background
        }

        /**
         * Set Style of login view
         *
         * @param style style (i.e. R.style.*)
         */
        fun setStyle(style: Int) {
            getLoginParam()?.style = style
        }

        /**
         * Set Style of input fields
         *
         * @param style style (i.e. R.style.*)
         */
        fun setInputStyle(style: Int) {
            getDataRepo().inputStyle = style
        }

        /**
         * Get Style of input fields
         *
         * @param style style (i.e. R.style.*)
         */
        fun getInputStyle(): Int {
            return getDataRepo().inputStyle
        }

        /**
         * Add an image in the login view
         *
         * @param image image (i.e. R.drawable.*)
         * @param width width of image
         * @param height height of image
         * @return ImageElement that can be customized with style, background, padding and margins
         */
        fun addImage(image: Int, width: Int, height: Int): ParamModel.ImageElement {
            val imageElement = ParamModel.ImageElement(image, width, height)
            getLoginParam()?.elements?.add(imageElement)
            return imageElement
        }

        /**
         * Add an text in the login view
         *
         * @param label text label
         * @param width width of text
         * @param height height of text
         * @return TextElement that can be customized with style, background, padding and margins
         */
        fun addText(label: String, width: Int, height: Int): ParamModel.TextElement {
            val text = ParamModel.TextElement(label, width, height)
            getLoginParam()?.elements?.add(text)
            return text
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
        fun addInput(hint: String, isPassword: Boolean, inputType: Int,
                     width: Int, height: Int, key: String): ParamModel.InputElement {
            val input = ParamModel.InputElement(hint, isPassword, inputType, width, height, key, true)
            getLoginParam()?.elements?.add(input)
            addInputElement(input)
            return input
        }

        /**
         * Add a login button in the login view
         *
         * @param label button label
         * @param width width of text
         * @param height height of text
         * @return ButtonElement that can be customized with style, background, padding and margins
         */
        fun addLoginButton(label: String, width: Int, height: Int): ParamModel.CustomButtonElement {
            val button = ParamModel.CustomButtonElement(label, width, height)
            getLoginParam()?.elements?.add(button)
            return button
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
        fun addButton(label: String, listener: LoginHelper.CustomListener, width: Int, height: Int): ParamModel.ButtonElement {
            val button = ParamModel.ButtonElement(label, listener, width, height)
            getLoginParam()?.elements?.add(button)
            return button
        }

        /**
         * Add Sign in with Google
         *
         * @param width width of text
         * @param height height of text
         * @return Google Sign In Button that can be customized with style, background, padding and margins
         */
        fun addGoogleSignIn(width: Int, height: Int): LoginParamModel.ThirdPartyGoogle {
            val button = LoginParamModel.ThirdPartyGoogle(width, height)
            getLoginParam()?.elements?.add(button)
            return button
        }

        /**
         * Add Sign in with Facebook
         *
         * @param width width of text
         * @param height height of text
         * @return Facebook Sign In Button that can be customized with style, background, padding and margins
         */
        fun addFacebookSignIn(width: Int, height: Int): LoginParamModel.ThirdPartyFacebook {
            val button = LoginParamModel.ThirdPartyFacebook(width, height)
            getLoginParam()?.elements?.add(button)
            return button
        }

        fun addForgotPassword(label: String, imageDrawable: Int?,
                              headerText: String,
                              subheaderText: String,
                              inputFieldHint: String,
                              buttonLabel: String): ForgotPasswordElement {

            val forgotPasswordElement = ForgotPasswordElement(label, imageDrawable,
                headerText, subheaderText, inputFieldHint, buttonLabel)
            getDataRepo().forgotPasswordElement = forgotPasswordElement
            getLoginParam()?.elements?.add(forgotPasswordElement)
            return forgotPasswordElement
        }

        /**
         * Store input elements to data array
         */
        private fun addInputElement(element: ParamModel.InputElement) {
            getDataRepo().inputElements.add(element)
        }

        /**
         * Get stored input elements
         *
         * @return stored input elements
         */
        fun getInputElements(): MutableList<ParamModel.InputElement> {
            return getDataRepo().inputElements
        }

        /**
         * Get singleton instance of ForgotPasswordElement
         *
         * @return instance of LoginParamModel
         */
        fun getForgotPasswordElement(): ForgotPasswordElement? {
            return getDataRepo().forgotPasswordElement
        }

        /**
         * Get singleton instance of LoginParamModel
         *
         * @return instance of LoginParamModel
         */
        fun getLoginParam(): LoginParamModel? {
            return getDataRepo().loginParamModel
        }

        private fun getDataRepo(): LoginParamDataRepository {
            return LoginParamDataRepository.instance
        }
    }
}