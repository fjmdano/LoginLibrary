package com.ubx.loginlibrary.datarepository

import android.util.Log
import com.ubx.formslibrary.view.widget.InputWidget
import com.ubx.loginlibrary.widget.ForgotPasswordWidget
import com.ubx.loginlibrary.widget.LoginWidget

class LoginParamDataRepository {
    var loginWidget: LoginWidget? = null
        get() {
            return if (field != null) {
                field
            } else {
                Log.w(TAG, "Login Parameters not initialized.")
                null
            }
        }
    var inputWidgets: MutableList<InputWidget> = mutableListOf()
    var inputStyle: Int = -1
    var forgotPasswordWidget: ForgotPasswordWidget? = null

    private object HOLDER {
        val INSTANCE = LoginParamDataRepository()
    }

    companion object {
        private const val TAG = "LoginLibrary"
        val instance: LoginParamDataRepository by lazy { LoginParamDataRepository.HOLDER.INSTANCE }
    }
}