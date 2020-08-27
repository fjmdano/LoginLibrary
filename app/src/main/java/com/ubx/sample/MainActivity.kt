package com.ubx.sample

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import com.ubx.kyclibrary.KYCHelper
import com.ubx.loginlibrary.LoginHelper
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    lateinit var loginHelper: LoginHelper
    lateinit var kycHelper: KYCHelper

    lateinit var loginIntent: Intent
    lateinit var kycIntent: Intent
    private val model: MainActivityViewModel by viewModels()
    val activity = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        //Create Login Parameters
        createHelpers()
        useThirdParties()

        createLoginContent()
        createKYCContent()

        createIntents()

        setSignOutHandler()
    }

    override fun onResume() {
        super.onResume()
        if (loginHelper.getUser() == null) {
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

        loginHelper.setCustomSignInHandler(object: LoginHelper.CustomLoginHandler {
            override fun login(): Any? {
                Log.d(TAG, "Custom signing in")
                Log.d(TAG, "email: ${loginHelper.getValue("email")}")
                Log.d(TAG, "password: ${loginHelper.getValue("password")}")
                return null
            }
        })
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
            loginIntent = loginHelper.getIntent(this)
        }
        if (!this::kycIntent.isInitialized) {
            kycIntent = kycHelper.getIntent(this)
        }
    }

    private fun showLogin() {
        startActivity(loginIntent)
    }


    private fun createLoginContent() {
        loginHelper.setPadding(10, 10, 10, 10)
        loginHelper.setBackground(R.color.white)
        loginHelper.setStyle(R.style.Background)
        loginHelper.setInputStyle(R.style.EditText_DefaultAlpha)

        val iconImage = loginHelper.addImage(R.drawable.wallet_icon,
            loginHelper.sizeInDP(150),
            loginHelper.sizeInDP(150)
        )
        iconImage.layoutGravity = Gravity.CENTER_HORIZONTAL
        iconImage.setMargins(0, loginHelper.sizeInDP(20), 0, 0)

        val textLogin = loginHelper.addText("Log in")
        textLogin.style = R.style.TitleText
        textLogin.layoutGravity = Gravity.CENTER_HORIZONTAL
        textLogin.setMargins(0, loginHelper.sizeInDP(5),
            0, loginHelper.sizeInDP(5))

        loginHelper.addInput(
            "Username",
            false,
            InputType.TYPE_CLASS_TEXT,
            "email",
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        loginHelper.addInput(
            "Password",
            true,
            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD,
            "password",
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        val forgotPassword = loginHelper.addForgotPassword("Forgot password?",
            R.drawable.wallet_icon,
            "Forgot your password?",
            "Confirm your email and we'll send you instructions")
        forgotPassword.setImageDimensions(loginHelper.sizeInDP(150), loginHelper.sizeInDP(150))
        forgotPassword.button.style = R.style.Button_RightLink
        forgotPassword.button.layoutGravity = Gravity.END
        forgotPassword.image?.setMargins(0, loginHelper.sizeInDP(20), 0, 0)
        forgotPassword.header?.style = R.style.TitleText
        forgotPassword.header?.setMargins(0, loginHelper.sizeInDP(5),
            0, loginHelper.sizeInDP(5))
        forgotPassword.subheader?.setMargins(0, 0,
            0, loginHelper.sizeInDP(3))

        val buttonLogin = loginHelper.addLoginButton("Log-in")
        buttonLogin.setMargins(0, loginHelper.sizeInDP(5), 0, 0)

        val textOthers = loginHelper.addText("------- or -------")
        textOthers.setMargins(0, loginHelper.sizeInDP(5),
            0, loginHelper.sizeInDP(5))
        textOthers.style = R.style.LabelText
        textOthers.layoutGravity = Gravity.CENTER_HORIZONTAL

        loginHelper.addFacebookSignIn()
        loginHelper.addGoogleSignIn()

        val buttonRegister = loginHelper.addButton("No account yet? Register now!",
            object: LoginHelper.CustomListener {
                override fun onClick() {
                    startActivity(kycHelper.getIntent(activity))
                }
            },
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        buttonRegister.style = R.style.Button_DefaultAlpha
        buttonRegister.setMargins(0, loginHelper.sizeInDP(10), 0, 0)
    }

    private fun createKYCContent(){
        kycHelper.setPadding(kycHelper.sizeInDP(5), kycHelper.sizeInDP(5), kycHelper.sizeInDP(5), 0)
        kycHelper.addPage("Terms and Agreement", null, null)
        kycHelper.addText(applicationContext.getString(R.string.terms_and_agreement))

        kycHelper.addSwitch("Are you healthy?",
        "isHealthy",
        true)

        kycHelper.addChecklist("Random label",
            listOf("I have read the Terms and Agreement",
            "Not read aww"),
            "terms",
            true
        )

        kycHelper.addNextButton("I agree to the Terms and Agreement")

        kycHelper.addPage("Registration", null, "Next")
        kycHelper.addInput(
            "E-mail", false,
            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS,
            "email",
            true)
        val passwordInput = kycHelper.addInput(
            "Password", true,
            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD,
            "password",
            true)
        passwordInput.setCharacterLength("", 8, 15)
        passwordInput.addRegexRule(
            "Password should contain an alphabetical character",
            ".*[a-zA-Z]+.*")
        passwordInput.addRegexRule(
            "Password should contain a numeric character",
            ".*\\d.*")
        passwordInput.addRegexRule(
            "Password should contain a symbol character (!@#\$%^)",
            "[a-zA-Z0-9]*", false)
        passwordInput.displayValidationBars()
        passwordInput.displayValidationRules()
        kycHelper.addPage("Personal Details", "Back", "Submit")

        kycHelper.addPageRow(mutableListOf(
            kycHelper.addInputInRow(
                "First Name", false,
                InputType.TYPE_CLASS_TEXT,
                "firstname",
                true),
            kycHelper.addInputInRow(
                "Last Name", false,
                InputType.TYPE_CLASS_TEXT,
                "lastname",
                true)
        ))

        kycHelper.addList(
            "Province",
            listOf("Metro Manila", "Abra", "Apayao", "Benguet", "Ifugao", "Kalinga", "Mountain Province", "Ilocos Norte", "Ilocos Sur", "La Union", "Pangasinan", "Batanes", "Cagayan", "Isabela", "Nueva Vizcaya", "Quirino", "Aurora", "Bataan", "Bulacan", "Nueva Ecija", "Pampanga", "Tarlac", "Zambales", "Batangas", "Cavite", "Laguna", "Quezon", "Rizal", "Marinduque", "Occidental Mindoro", "Oriental Mindoro", "Palawan", "Romblon", "Albay", "Camarines Norte", "Camarines Sur", "Catanduanes", "Masbate", "Sorsogon", "Aklan", "Antique", "Capiz", "Guimaras", "Iloilo", "Negros Occidental", "Bohol", "Cebu", "Negros Oriental", "Siquijor", "Biliran", "Eastern Samar", "Leyte", "Northern Samar", "Samar", "Southern Leyte", "Zamboanga del Norte", "Zamboanga del Sur", "Zamboanga Sibugay", "Bukidnon", "Camiguin", "Lanao del Norte", "Misamis Occidental", "Misamis Oriental", "Compostela Valley", "Davao del Norte", "Davao del Sur", "Davao Occidental", "Davao Oriental", "Cotabato", "Sarangani", "South Cotabato", "Sultan Kudarat", "Agusan del Norte", "Agusan del Sur", "Dinagat Islands", "Surigao del Norte", "Surigao del Sur", "Basilan", "Lanao del Sur", "Maguindanao", "Sulu", "Tawi-tawi"),
            "province",
            true)
        kycHelper.addInput(
            "Address", false,
            InputType.TYPE_CLASS_TEXT,
            "address",
            true)
        kycHelper.addInput(
            "Phone", false,
            InputType.TYPE_CLASS_PHONE,
            "phone",
            true)

        kycHelper.addPageRow(mutableListOf(
            kycHelper.addDateInRow(
                "Birthday",
                "birthday",
                true),
            kycHelper.addDropdownInRow(
                "Gender",
                listOf("Female", "Male", "Non-binary"),
                "gender",
                true)
        ))
    }

    companion object {
        private const val TAG = "MAIN"
    }
}
