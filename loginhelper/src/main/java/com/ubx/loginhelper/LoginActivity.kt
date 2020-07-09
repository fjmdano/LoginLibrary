package com.ubx.loginhelper

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.ubx.loginhelper.viewmodel.LoginViewModel


class LoginActivity: AppCompatActivity() {
    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        loginViewModel = LoginViewModel(this)
        addLoginPage()
    }

    override fun onStart() {
        super.onStart()
        val account = GoogleSignIn.getLastSignedInAccount(this)
        if (account != null) {
            Toast.makeText(this, "Someone already signed in", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        println(requestCode)
        Toast.makeText(this, "Signing in!" + requestCode, Toast.LENGTH_SHORT).show()

        if (RC_SIGN_IN == requestCode) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task: Task<GoogleSignInAccount> =
                GoogleSignIn.getSignedInAccountFromIntent(data)
            handleGoogleSignInResult(task)
        }

    }

    private fun addLoginPage() {
        val constraintLayout = findViewById<ConstraintLayout>(R.id.login_layout)

        val linearLayout = loginViewModel.createLoginPage(this)
        constraintLayout.addView(linearLayout)

        val constraintSet = ConstraintSet()
        constraintSet.clone(constraintLayout)
        constraintSet.connect(linearLayout.id, ConstraintSet.LEFT, constraintLayout.id, ConstraintSet.LEFT, 0)
        constraintSet.connect(linearLayout.id, ConstraintSet.RIGHT, constraintLayout.id, ConstraintSet.RIGHT, 0)
        constraintSet.connect(linearLayout.id, ConstraintSet.TOP, constraintLayout.id, ConstraintSet.TOP, 0)
        constraintSet.applyTo(constraintLayout)
    }

    private fun handleGoogleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account= completedTask.getResult(ApiException::class.java)
            if (account != null) {
                Toast.makeText(this, "Signed in!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Null account retrieved, sorry.", Toast.LENGTH_SHORT).show()
            }

        } catch (e: ApiException) {
            Toast.makeText(this, "Error: " + e.message, Toast.LENGTH_SHORT).show()
            println(e.localizedMessage)
        }
    }

    companion object {
        const val RC_SIGN_IN = 111
        fun getIntent(context: Context): Intent {
            val intent = Intent(context, Class.forName("com.ubx.loginhelper.LoginActivity"))
            return intent
        }
    }
}