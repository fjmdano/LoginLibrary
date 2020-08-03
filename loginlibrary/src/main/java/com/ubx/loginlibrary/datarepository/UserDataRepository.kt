package com.ubx.loginlibrary.datarepository

import android.app.Activity
import android.content.Intent
import com.ubx.loginlibrary.model.User

class UserDataRepository {
    var user: User? = null
    var kycIntent: Intent? = null
    var mainActivity: Activity? = null

    private object HOLDER {
        val INSTANCE = UserDataRepository()
    }

    companion object {
        val instance: UserDataRepository by lazy { UserDataRepository.HOLDER.INSTANCE }
    }
}