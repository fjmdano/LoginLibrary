package com.ubx.sample

import android.os.Bundle
import android.text.InputType
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.ubx.loginhelper.LoginActivity
import com.ubx.loginhelper.LoginHelper
import com.ubx.loginhelper.model.thirdpartyconfig.FacebookConfig
import com.ubx.loginhelper.model.thirdpartyconfig.FirebaseConfig

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Create Login Parameters
        val loginHelper = createLoginHelper()
        loginHelper.useFacebook("738407303585042",
            "fb738407303585042")
        loginHelper.useFirebase("915758123947",
            "https://trialapp-18e6e.firebaseio.com",
            "trialapp-18e6e",
            "trialapp-18e6e.appspot.com",
            "1:915758123947:android:c5761b86a82280408520ee",
            "AIzaSyAF9yimwJdEqdNcxsxEGnqTRo0w5KTbcqM",
            "915758123947-4vhnf7vbjk7p6c0njqp5lben0ede1o9v.apps.googleusercontent.com")

        setSignOutHandler(loginHelper)
        var user = loginHelper.getUser()
        if (user == null) {
            showLogin(loginHelper)
        }
    }

    private fun setSignOutHandler(loginHelper: LoginHelper) {
        val btnSignOut = findViewById<Button>(R.id.btn_logout)
        btnSignOut.setOnClickListener {
            loginHelper.signOutUser()
            showLogin(loginHelper)
        }
    }

    private fun showLogin(loginHelper: LoginHelper) {
        val intent = loginHelper.getIntent()
        startActivity(intent)
    }


    private fun createLoginHelper(): LoginHelper {
        val loginHelper = LoginHelper(this, "name",
            ConstraintLayout.LayoutParams.MATCH_PARENT,
            ConstraintLayout.LayoutParams.MATCH_PARENT)

        loginHelper.setPadding(10, 10, 10, 10)
        loginHelper.setBackground(android.R.color.white)
        loginHelper.setStyle(R.style.Background)

        val iconImage = loginHelper.addImage(android.R.drawable.ic_lock_lock,
            loginHelper.sizeInDP(50),
            loginHelper.sizeInDP(50)
        )
        iconImage.layoutGravity = Gravity.CENTER_HORIZONTAL

        val textLogin = loginHelper.addText("Log-in",
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        textLogin.style = R.style.LabelText
        textLogin.layoutGravity = Gravity.CENTER_HORIZONTAL

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

        val textOthers = loginHelper.addText("Or log-in using",
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
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

        return loginHelper
    }

}
