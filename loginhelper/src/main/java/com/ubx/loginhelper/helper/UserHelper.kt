package com.ubx.loginhelper.helper

import com.facebook.AccessToken
import com.facebook.login.LoginManager
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.ubx.loginhelper.datarepository.UserDataRepository
import com.ubx.loginhelper.helper.ThirdPartyConfigHelper
import com.ubx.loginhelper.model.User

class UserHelper {
    companion object {

        /**
         * Get currently signed-in user details
         *
         * @return User
         */
        fun getSignedInUser(): User? {
            var user = UserDataRepository.instance.user
            if (user == null) {
                if (ThirdPartyConfigHelper.isFirebaseIntegrated()) {
                    val firebaseUser = Firebase.auth.currentUser
                    if (firebaseUser != null) {
                        user = User(User.AuthMethod.FIREBASE, firebaseUser)
                        UserDataRepository.instance.user = user
                        return user
                    }
                }
                if (ThirdPartyConfigHelper.isFacebookIntegrated()) {
                    val facebookUser = AccessToken.getCurrentAccessToken()
                    if (facebookUser != null && !facebookUser.isExpired) {
                        user = User(User.AuthMethod.FACEBOOK, facebookUser)
                        UserDataRepository.instance.user = user
                        return user
                    }
                }
            }
            return user
        }

        /**
         * Set user details
         *
         * @param account account details
         * (type is FirebaseUser if using Firebase,
         *          AccessToken if using Facebook,
         *          Any if using own authentication)
         */
        fun setSignedInUser(account: Any) {
            UserDataRepository.instance.user = when (account) {
                is FirebaseUser -> {
                    User(User.AuthMethod.FIREBASE, account)
                }
                is AccessToken -> {
                    User(User.AuthMethod.FACEBOOK, account)
                }
                else -> {
                    User(User.AuthMethod.OWN, account)
                }
            }
        }

        /**
         * Sign out user and remove stored user details
         */
        fun signOutUser() {
            when (UserDataRepository.instance.user?.method) {
                User.AuthMethod.FIREBASE -> Firebase.auth.signOut()
                User.AuthMethod.FACEBOOK -> {
                    LoginManager.getInstance().logOut();
                }
                User.AuthMethod.OWN -> {
                    //TODO: Call BE API provided somewhere
                }
            }
            UserDataRepository.instance.user = null
        }
    }
}