package com.ubx.loginlibrary.datarepository

import android.app.Activity
import com.ubx.loginlibrary.LoginHelper
import com.ubx.loginlibrary.model.User

class UserDataRepository {
    var user: User? = null
    var mainActivity: Activity? = null
    var loginHandler: LoginHelper.CustomLoginHandler? = null

    private object HOLDER {
        val INSTANCE = UserDataRepository()
    }

    companion object {
        val instance: UserDataRepository by lazy { UserDataRepository.HOLDER.INSTANCE }
    }
}