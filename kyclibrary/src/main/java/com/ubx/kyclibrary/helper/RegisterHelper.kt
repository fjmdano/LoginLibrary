package com.ubx.kyclibrary.helper

import android.R.attr
import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.lang.Exception


class RegisterHelper {

    companion object {
        private const val TAG = "RegisterHelper"

        fun createUserWithEmail(activity: Activity, email: String, password: String) {
            try {
                val firebaseAuth = Firebase.auth
                firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(activity) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success")
                            val user = firebaseAuth.currentUser
                            Toast.makeText(activity, "Successful registration.",
                                Toast.LENGTH_SHORT).show()
                            KYCValueHelper.storeInDB(activity)
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.exception)
                            Toast.makeText(activity, "Authentication failed.",
                                Toast.LENGTH_SHORT).show()
                        }
                    }
            } catch (e: Exception) {
                Log.w("Register", "registerUser: error", e)
            }
        }
    }
}