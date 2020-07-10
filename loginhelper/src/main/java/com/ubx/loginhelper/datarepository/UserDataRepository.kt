package com.ubx.loginhelper.datarepository

import com.ubx.loginhelper.model.User

class UserDataRepository {
    var user: User? = null

    private object HOLDER {
        val INSTANCE = UserDataRepository()
    }

    companion object {
        val instance: UserDataRepository by lazy { UserDataRepository.HOLDER.INSTANCE }
    }
}