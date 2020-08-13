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
import com.ubx.loginlibrary.model.UIElement
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

        loginHelper.setCustomSigninHandler(object: LoginHelper.CustomLoginHandler {
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
            LinearLayout.LayoutParams.WRAP_CONTENT,
            "email"
        )
        inputUsername.style = R.style.EditText_DefaultAlpha

        val inputPassword = loginHelper.addInput("Password",
            true,
            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD,
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            "password"
        )
        inputPassword.style = R.style.EditText_DefaultAlpha

        val buttonLogin = loginHelper.addLoginButton("Log-in",
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

        loginHelper.addFacebookSignIn(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        loginHelper.addGoogleSignIn(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

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
        buttonRegister.margins = UIElement.Margins(0, loginHelper.sizeInDP(10), 0, 0)
    }

    private fun createKYCContent(){
        kycHelper.setPadding(kycHelper.sizeInDP(5), kycHelper.sizeInDP(5), kycHelper.sizeInDP(5), 0)
        kycHelper.addPage("Terms and Agreement", null, null)
        kycHelper.addText(applicationContext.getString(R.string.terms_and_agreement),
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        kycHelper.addNextButton("I agree to the Terms and Agreement",
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT)

        kycHelper.addPage("Registration", null, "Next")
        kycHelper.addMedia("Camera", LinearLayout.LayoutParams.MATCH_PARENT,
            kycHelper.sizeInDP(150), "selfie", true)
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

        kycHelper.addList("Country",
            listOf("Afghanistan", "Afghanistan US Military Base", "Albania", "Algeria", "American Samoa", "Andorra", "Angola", "Anguilla", "Antigua And Barbuda", "Argentina", "Aruba", "Australia", "Austria", "Azerbaijan", "Bahamas", "Bahrain", "Bahrain US Military Base", "Bangladesh", "Barbados", "Belarus", "Belgium", "Belgium US Military Base", "Belize", "Benin", "Bermuda", "Bhutan", "Bolivia", "Bonaire", "Bosnia and Herzegovina", "Botswana", "Brazil", "British Virgin Islands", "Brunei Darussalam", "Bulgaria", "Burkina Faso", "Burundi", "Cambodia", "Cameroon", "Canada", "Cape Verde", "Cayman Islands", "Central African Republic", "Chad", "Chile", "China", "Colombia", "Comoros", "Congo, Democratic Republic of", "Congo-Brazzaville", "Cook Islands", "Costa Rica", "Croatia", "Cuba", "Cuba US Military Base", "Curacao", "Cyprus", "Cyprus (Northern)", "Czech Republic", "Denmark", "Djibouti", "Djibouti US Military Base", "Dominica", "Dominican Republic", "East Timor", "Ecuador", "Egypt", "El Salvador", "England", "Equatorial Guinea", "Eritrea", "Estonia", "Ethiopia", "Falkland Islands (Malvinas)", "Fiji", "Finland", "France", "French Guiana", "French Polynesia", "Gabon", "Gambia", "Georgia", "Germany", "Germany US Military Base", "Ghana", "Gibraltar", "Greece", "Greece US Military Base", "Grenada", "Guadeloupe", "Guam", "Guam US Military Base", "Guatemala", "Guinea", "Guinea-Bissau", "Guyana", "Haiti", "Honduras", "Honduras US Military Base", "Hong Kong", "Hungary", "Iceland", "Iceland US Military Base", "India", "Indonesia", "International Test Country", "Iraq", "Iraq US Military Base", "Ireland", "Israel", "Italy", "Italy US Military Base", "Ivory Coast", "Jamaica", "Japan", "Japan US Military Base", "Jordan", "Kazakhstan", "Kenya", "Kiribati", "Korea", "Korea US Military Base", "Kosovo", "Kosovo US Military Base", "Kuwait", "Kuwait US Military Base", "Kyrghyz Republic", "Kyrghyz Republic US Military Base", "Laos", "Latvia", "Lebanon", "Lesotho", "Liberia", "Libya", "Liechtenstein", "Lithuania", "Luxembourg", "Macau", "Macedonia", "Madagascar", "Malawi", "Malaysia", "Maldives", "Mali", "Malta", "Marshall Islands", "Martinique", "Mauritania", "Mauritius", "Mayotte", "Mexico", "Micronesia", "Moldova", "Monaco", "Mongolia", "Montenegro", "Montserrat", "Morocco", "Mozambique", "Myanmar", "Namibia", "Nauru", "Nepal", "Netherlands", "Netherlands US Military Base", "New Caledonia", "New Zealand", "Nicaragua", "Niger", "Nigeria", "Niue", "North Ireland", "Northern Mariana Islands", "Norway", "Oman", "Pakistan", "Palau", "Palestinian Authority", "Panama", "Papua New Guinea", "Paraguay", "Peru", "Philippines", "Poland", "Portugal", "Portugal US Military Base", "Puerto Rico", "Qatar", "Qatar US Military Base", "Reunion Island", "Romania", "Rota, CNMI", "Russia", "Rwanda", "Saint Barthelemy", "Saint Kitts And Nevis", "Saint Lucia", "Saint Vincent And The Grenadines", "Saipan, CNMI", "Samoa", "Sao Tome And Principe", "Saudi Arabia", "Scotland", "Senegal", "Serbia", "Seychelles", "Sierra Leone", "Singapore", "Slovakia", "Slovenia", "Solomon Islands", "Somalia", "Somaliland", "South Africa", "South Sudan", "Spain", "Spain US Military Base", "Sri Lanka", "St. Maarten", "St. Martin", "St. Thomas", "Sudan", "Suriname", "Swaziland", "Sweden", "Switzerland", "Syria", "Taiwan", "Tajikistan", "Tanzania", "Thailand", "Tinian, CNMI", "Togo", "Tonga", "Trinidad and Tobago", "Tunisia", "Turkey", "Turkey US Military Base", "Turkmenistan", "Turks and Caicos Islands", "Tuvalu", "UAE US Military Base", "Uganda", "Ukraine", "United Arab Emirates", "United Kingdom", "United Kingdom US Military Base", "United States", "Uruguay", "Uzbekistan", "Vanuatu", "Venezuela", "Vietnam", "Virgin Islands (US)", "Wales", "Yemen", "Zambia", "Zimbabwe"),
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            "country",
            true
        )
        kycHelper.addInput("Address", false,
            InputType.TYPE_CLASS_TEXT,
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            "address",
            true
        )

        kycHelper.addPageRow(mutableListOf(
            kycHelper.addDateInRow("Birthday",
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                "birthday",
                true
            ),
            kycHelper.addDropdownInRow("Gender",
                listOf("Female", "Male", "Non-binary"),
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                "gender",
                true)
        ))

    }

    companion object {
        private const val TAG = "MAIN"
    }
}
