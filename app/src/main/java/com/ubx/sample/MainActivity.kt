package com.ubx.sample

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.Gravity
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.ubx.kyclibrary.KYCHelper
import com.ubx.loginlibrary.LoginHelper
import com.ubx.loginlibrary.model.UIElement

class MainActivity : Activity() {
    lateinit var loginHelper: LoginHelper
    lateinit var kycHelper: KYCHelper

    lateinit var loginIntent: Intent
    lateinit var kycIntent: Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_main)

        //Create Login Parameters
        createHelpers()
        useThirdParties()
        createIntents()

        createLoginContent()
        createKYCContent()

        setSignOutHandler()
        var user = loginHelper.getUser()
        if (user == null) {
            showLogin()
        }
    }

    private fun setSignOutHandler() {
        val btnSignOut = findViewById<Button>(R.id.btn_logout)
        btnSignOut.setOnClickListener {
            loginHelper.signOutUser()
            showLogin()
        }
    }

    /**
     * Initialize LoginHelper and KYCHelper
     */
    private fun createHelpers() {
        loginHelper = LoginHelper(applicationContext, "name",
            ConstraintLayout.LayoutParams.MATCH_PARENT,
            ConstraintLayout.LayoutParams.MATCH_PARENT)

        kycHelper = KYCHelper(applicationContext, "LoginHelper")
    }

    /**
     * Set config for Facebook and Firebase SDK
     */
    private fun useThirdParties() {
        loginHelper.useFacebook("738407303585042",
            "fb738407303585042")
        loginHelper.useFirebase("915758123947",
            "https://trialapp-18e6e.firebaseio.com",
            "trialapp-18e6e",
            "trialapp-18e6e.appspot.com",
            "1:915758123947:android:c5761b86a82280408520ee",
            "AIzaSyAF9yimwJdEqdNcxsxEGnqTRo0w5KTbcqM",
            "915758123947-4vhnf7vbjk7p6c0njqp5lben0ede1o9v.apps.googleusercontent.com")
    }

    /**
     * Create log-in and kyc intent
     */
    private fun createIntents() {
        if (!this::loginIntent.isInitialized) {
            loginIntent = loginHelper.getIntent()
            kycHelper.setLoginIntent(loginIntent)
        }
        if (!this::kycIntent.isInitialized) {
            kycIntent = kycHelper.getIntent()
            loginHelper.setKYCIntent(kycIntent)
        }
    }

    private fun showLogin() {
        startActivity(loginIntent)
    }

    private fun createLoginContent() {
        loginHelper.setPadding(10, 10, 10, 10)
        loginHelper.setBackground(R.color.white)
        loginHelper.setStyle(R.style.Background)

        val iconImage = loginHelper.addImage(R.drawable.wallet_icon ,
            loginHelper.sizeInDP(150),
            loginHelper.sizeInDP(150)
        )
        iconImage.layoutGravity = Gravity.CENTER_HORIZONTAL
        iconImage.margins = UIElement.Margins(0, loginHelper.sizeInDP(20), 0, 0)

        val textLogin = loginHelper.addText("Log in",
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        textLogin.style = R.style.TitleText
        textLogin.layoutGravity = Gravity.CENTER_HORIZONTAL
        textLogin.margins = UIElement.Margins(0, loginHelper.sizeInDP(5),
            0, loginHelper.sizeInDP(5))

        val inputUsername = loginHelper.addInput("Username",
            false,
            InputType.TYPE_CLASS_TEXT,
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        inputUsername.style = R.style.EditText_DefaultAlpha

        val inputPassword = loginHelper.addInput("Password",
            true,
            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD,
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        inputPassword.style = R.style.EditText_DefaultAlpha

        val buttonLogin = loginHelper.addButton("Log-in",
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        buttonLogin.margins = UIElement.Margins(0, loginHelper.sizeInDP(5), 0, 0)

        val textOthers = loginHelper.addText("------- or -------",
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        textOthers.margins = UIElement.Margins(0, loginHelper.sizeInDP(5),
            0, loginHelper.sizeInDP(5))
        textOthers.style = R.style.LabelText
        textOthers.layoutGravity = Gravity.CENTER_HORIZONTAL

        val partyFacebook = loginHelper.addFacebookSignIn(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        val partyGoogle = loginHelper.addGoogleSignIn(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        val buttonRegister = loginHelper.addIntentButton("No account yet? Register now!",
            kycIntent,
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        buttonRegister.style = R.style.Button_DefaultAlpha
        buttonRegister.margins = UIElement.Margins(0, loginHelper.sizeInDP(10), 0, 0)

    }

    private fun createKYCContent(){
        kycHelper.setPadding(kycHelper.sizeInDP(5), kycHelper.sizeInDP(5), kycHelper.sizeInDP(5), 0)
        kycHelper.addPage("Terms and Agreement", null, null)
        kycHelper.addText(applicationContext.getString(R.string.terms_and_agreement),
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        kycHelper.addButton("I agree to the Terms and Agreement",
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT)

        kycHelper.addPage("Registration", null, "Next")
        kycHelper.addInput("E-mail", false,
            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS,
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            "email",
            true
        )
        val usernameInput = kycHelper.addInput("Username", false,
            InputType.TYPE_CLASS_TEXT,
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            "username",
            true
        )
        usernameInput.minimumLength = 8
        usernameInput.maximumLength = 15
        val passwordInput = kycHelper.addInput("Password", true,
            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD,
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            "password",
            true
        )
        passwordInput.minimumLength = 8
        passwordInput.maximumLength = 15
        passwordInput.regexPositiveValidation.add(".*[a-zA-Z]+.*")
        passwordInput.regexPositiveValidation.add(".*\\d.*")
        passwordInput.regexNegativeValidation.add("[a-zA-Z0-9]*")
        kycHelper.addText("Password should contain 8-20 characters, with at least 1 letter, 1 number and 1 special character",
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        kycHelper.addPage("Personal Details", "Back", "Submit")
        kycHelper.addInput("Address", false,
            InputType.TYPE_CLASS_TEXT,
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            "address",
            true
        )
        kycHelper.addDate("Birthday",
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            "birthday",
            true
        )
        kycHelper.addDropdown("Gender",
            listOf("Female", "Male", "Non-binary"),
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            "gender",
            true)
    }

}
