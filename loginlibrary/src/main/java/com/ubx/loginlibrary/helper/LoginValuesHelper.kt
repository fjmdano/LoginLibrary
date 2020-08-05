package com.ubx.loginlibrary.helper

import com.ubx.loginlibrary.datarepository.LoginValuesDataRepository

class LoginValuesHelper {
    companion object {
        fun setValue(key: String, value: String){
            getDataRepo().values[key] = value
        }

        fun getValue(key: String): String {
            return getDataRepo().values[key] ?: ""
        }

        private fun getDataRepo(): LoginValuesDataRepository {
            return LoginValuesDataRepository.instance
        }
    }
}