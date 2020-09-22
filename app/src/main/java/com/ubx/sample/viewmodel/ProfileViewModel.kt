package com.ubx.sample.viewmodel

import androidx.lifecycle.ViewModel
import com.ubx.sample.model.ProfileDataRepository

class ProfileViewModel: ViewModel() {

    fun getStoredValues(): Map<String, Any> {
        return ProfileDataRepository.instance.getProfile()
    }
}