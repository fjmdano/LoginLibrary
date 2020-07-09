package com.ubx.loginhelper.viewmodel

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.ubx.loginhelper.model.User

class UserViewModel {
    private var user: User? = null

    private object HOLDER {
        val INSTANCE = UserViewModel()
    }

    fun getSignedInUser(): User? {
        if (user == null) {
            val firebaseUser = Firebase.auth.currentUser
            if (firebaseUser != null) {
                user = User(User.AuthMethod.FIREBASE, firebaseUser)
            }
        }
        return user
    }

    fun setSignedInUser(account: Any) {
        if (account is FirebaseUser) {
            user = User(User.AuthMethod.FIREBASE, account)
        } else {
            user = User(User.AuthMethod.OWN, account)
        }
    }

    fun signOutUser() {
        when (user?.method) {
            User.AuthMethod.FIREBASE -> Firebase.auth.signOut()
            User.AuthMethod.FACEBOOK -> {
                //TODO
            }
            User.AuthMethod.OWN -> {
                //TODO: Call BE API provided somewhere
            }
        }
        user = null
    }

    companion object {
        val instance: UserViewModel by lazy { HOLDER.INSTANCE }
    }
}