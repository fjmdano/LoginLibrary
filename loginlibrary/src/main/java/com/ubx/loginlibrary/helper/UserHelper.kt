package com.ubx.loginlibrary.helper

import android.app.Activity
import com.facebook.AccessToken
import com.facebook.login.LoginManager
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.ubx.formslibrary.model.User
import com.ubx.loginlibrary.LoginHelper
import com.ubx.loginlibrary.datarepository.UserDataRepository

class UserHelper {
    companion object {

        /**
         * Get currently signed-in user details
         *
         * @return User
         */
        fun getSignedInUser(): User? {
            var user = getDataRepo().user
            if (user == null) {
                if (ThirdPartyConfigHelper.isFirebaseIntegrated()) {
                    val firebaseUser = Firebase.auth.currentUser
                    if (firebaseUser != null) {
                        user = User(User.AuthMethod.FIREBASE, firebaseUser)
                        getDataRepo().user = user
                        return user
                    }
                }
                if (ThirdPartyConfigHelper.isFacebookIntegrated()) {
                    val facebookUser = AccessToken.getCurrentAccessToken()
                    if (facebookUser != null && !facebookUser.isExpired) {
                        user = User(User.AuthMethod.FACEBOOK, facebookUser)
                        getDataRepo().user = user
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
            getDataRepo().user = when (account) {
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
            when (getDataRepo().user?.method) {
                User.AuthMethod.FIREBASE -> Firebase.auth.signOut()
                User.AuthMethod.FACEBOOK -> {
                    LoginManager.getInstance().logOut();
                }
                User.AuthMethod.OWN -> {
                    //TODO: Call BE API provided somewhere
                }
            }
            getDataRepo().user = null
        }

        fun setCustomHandler(loginHandler: LoginHelper.CustomLoginHandler) {
            getDataRepo().loginHandler = loginHandler
        }

        fun getCustomHandler(): LoginHelper.CustomLoginHandler? {
            return getDataRepo().loginHandler
        }

        /**
         * Set Main Activity: where it will return once done
         */
        fun setMainActivity(activity: Activity) {
            getDataRepo().mainActivity = activity
        }

        /**
         * Get Main Activity
         * Called once user is logged in
         */
        fun getMainActivity(): Activity? {
            return getDataRepo().mainActivity
        }

        /**
         * Get instance of UserDataRepository
         *
         * @return instance of UserDataRepository
         */
        private fun getDataRepo(): UserDataRepository {
            return UserDataRepository.instance
        }
    }
}