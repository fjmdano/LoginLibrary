package com.ubx.loginhelper.model

data class User(val method: AuthMethod, val details: Any) {
    enum class AuthMethod {
        OWN,
        FACEBOOK,
        FIREBASE
    }
}