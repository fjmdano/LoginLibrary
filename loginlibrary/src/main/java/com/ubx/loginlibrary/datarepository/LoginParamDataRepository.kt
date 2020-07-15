package com.ubx.loginlibrary.datarepository

import android.util.Log
import com.ubx.loginlibrary.model.LoginParamModel

class LoginParamDataRepository {
    var loginParamModel: LoginParamModel? = null
        get() {
            return if (field != null) {
                field
            } else {
                Log.w(TAG, "Login Parameters not initialized.")
                null
            }
        }

    private object HOLDER {
        val INSTANCE = LoginParamDataRepository()
    }

    companion object {
        private const val TAG = "LoginLibrary"
        val instance: LoginParamDataRepository by lazy { LoginParamDataRepository.HOLDER.INSTANCE }
    }
}