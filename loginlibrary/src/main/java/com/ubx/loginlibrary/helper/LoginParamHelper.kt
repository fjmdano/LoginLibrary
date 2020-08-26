package com.ubx.loginlibrary.helper

import com.ubx.formslibrary.widget.*
import com.ubx.loginlibrary.LoginHelper
import com.ubx.loginlibrary.datarepository.LoginParamDataRepository
import com.ubx.loginlibrary.widget.ForgotPasswordWidget
import com.ubx.loginlibrary.widget.LoginWidget

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
            getDataRepo().loginWidget =
                LoginWidget(appName, width, height)
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
            getLoginWidget()?.setPadding(left, top, right, bottom)
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
            getLoginWidget()?.setMargins(left, top, right, bottom)
        }

        /**
         * Set Background of login view
         *
         * @param background background (i.e. R.drawable.*)
         */
        fun setBackground(background: Int) {
            getLoginWidget()?.background = background
        }

        /**
         * Set Style of login view
         *
         * @param style style (i.e. R.style.*)
         */
        fun setStyle(style: Int) {
            getLoginWidget()?.style = style
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
         * @return ImageWidget that can be customized with style, background, padding and margins
         */
        fun addImage(image: Int, width: Int, height: Int): ImageWidget {
            val imageElement = ImageWidget(image, width, height)
            getLoginWidget()?.elements?.add(imageElement)
            return imageElement
        }

        /**
         * Add an text in the login view
         *
         * @param label text label
         * @param width width of text
         * @param height height of text
         * @return TextWidget that can be customized with style, background, padding and margins
         */
        fun addText(label: String, width: Int, height: Int): TextWidget {
            val text = TextWidget(label, width, height)
            getLoginWidget()?.elements?.add(text)
            return text
        }

        /**
         * Add an input text in the login view
         *
         * @param hint input hint
         * @param isPassword true if input text is password, else false
         * @param inputType input type
         * @param key input key - to be used for retrieving the input value
         * @param width width of text
         * @param height height of text
         * @return InputWidget that can be customized with style, background, padding and margins
         */
        fun addInput(
            hint: String, isPassword: Boolean, inputType: Int,
            key: String, width: Int, height: Int
        ): InputWidget {
            val input = InputWidget(hint, isPassword, inputType, key, true, width, height)
            getLoginWidget()?.elements?.add(input)
            addInputElement(input)
            return input
        }

        /**
         * Add a login button in the login view
         *
         * @param label button label
         * @param width width of text
         * @param height height of text
         * @return ButtonWidget that can be customized with style, background, padding and margins
         */
        fun addLoginButton(label: String, width: Int, height: Int): ButtonWidget {
            val button = ButtonWidget(label, width, height)
            button.setAsCustom(true)
            getLoginWidget()?.elements?.add(button)
            return button
        }

        /**
         * Add a button (with intent to next activity) in the login view
         *
         * @param label button label
         * @param listener button onclick listener
         * @param width width of text
         * @param height height of text
         * @return ButtonWidget that can be customized with style, background, padding and margins
         */
        fun addButton(label: String, listener: LoginHelper.CustomListener, width: Int, height: Int): ButtonWidget {
            val button = ButtonWidget(label, width, height)
            button.setOnClickListener(listener)
            getLoginWidget()?.elements?.add(button)
            return button
        }

        /**
         * Add Sign in with Google
         *
         * @param width width of text
         * @param height height of text
         * @return Google Sign In Button that can be customized with style, background, padding and margins
         */
        fun addGoogleSignIn(width: Int, height: Int): GoogleLogin {
            val button = GoogleLogin(width, height)
            getLoginWidget()?.elements?.add(button)
            return button
        }

        /**
         * Add Sign in with Facebook
         *
         * @param width width of text
         * @param height height of text
         * @return Facebook Sign In Button that can be customized with style, background, padding and margins
         */
        fun addFacebookSignIn(width: Int, height: Int): FacebookLogin {
            val button = FacebookLogin(width, height)
            getLoginWidget()?.elements?.add(button)
            return button
        }

        fun addForgotPassword(label: String, imageDrawable: Int?,
                              headerText: String,
                              subheaderText: String,
                              inputFieldHint: String,
                              buttonLabel: String): ForgotPasswordWidget {

            val forgotPasswordWidget =
                ForgotPasswordWidget(
                    label, imageDrawable,
                    headerText, subheaderText, inputFieldHint, buttonLabel
                )
            getDataRepo().forgotPasswordWidget = forgotPasswordWidget
            getLoginWidget()?.elements?.add(forgotPasswordWidget)
            return forgotPasswordWidget
        }

        /**
         * Store input elements to data array
         */
        private fun addInputElement(element: InputWidget) {
            getDataRepo().inputWidgets.add(element)
        }

        /**
         * Get stored input elements
         *
         * @return stored input elements
         */
        fun getInputElements(): MutableList<InputWidget> {
            return getDataRepo().inputWidgets
        }

        /**
         * Get singleton instance of ForgotPasswordElement
         *
         * @return instance of LoginParamModel
         */
        fun getForgotPasswordElement(): ForgotPasswordWidget? {
            return getDataRepo().forgotPasswordWidget
        }

        /**
         * Get singleton instance of LoginParamModel
         *
         * @return instance of LoginParamModel
         */
        fun getLoginWidget(): LoginWidget? {
            return getDataRepo().loginWidget
        }

        private fun getDataRepo(): LoginParamDataRepository {
            return LoginParamDataRepository.instance
        }
    }
}