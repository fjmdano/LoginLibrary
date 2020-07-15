package com.ubx.loginhelper.datarepository

import android.content.Intent
import com.ubx.loginhelper.model.User

class UserDataRepository {
    var user: User? = null
    var registerIntent: Intent? = null

    private object HOLDER {
        val INSTANCE = UserDataRepository()
    }

    companion object {
        val instance: UserDataRepository by lazy { UserDataRepository.HOLDER.INSTANCE }
    }
}