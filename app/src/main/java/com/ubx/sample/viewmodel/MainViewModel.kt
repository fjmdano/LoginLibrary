package com.ubx.sample.viewmodel

import androidx.lifecycle.ViewModel
import com.ubx.sample.model.ProfileDataRepository

class MainViewModel: ViewModel() {

    fun getFirebaseProfile(uid: String) {
        ProfileDataRepository.instance.setupFirebase(uid)
    }

    fun getFirebaseValue(key: String): Any? {
        val map = ProfileDataRepository.instance.getProfile()
        if (key in map) {
            return map[key]
        }
        return null
    }

}