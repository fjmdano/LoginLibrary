package com.ubx.loginlibrary.datarepository

class LoginValuesDataRepository {
    var values: MutableMap<String, String> = mutableMapOf()

    private object HOLDER {
        val INSTANCE = LoginValuesDataRepository()
    }

    companion object {
        val instance: LoginValuesDataRepository by lazy { HOLDER.INSTANCE }
    }
}