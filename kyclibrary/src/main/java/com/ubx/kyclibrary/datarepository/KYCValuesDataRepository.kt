package com.ubx.kyclibrary.datarepository

import android.graphics.Bitmap

class KYCValuesDataRepository {
    var values: MutableMap<String, String> = mutableMapOf()
    var images: MutableMap<String, Bitmap> = mutableMapOf()

    private object HOLDER {
        val INSTANCE = KYCValuesDataRepository()
    }

    companion object {
        private const val TAG = "KYCLibrary"
        val instance: KYCValuesDataRepository by lazy { KYCValuesDataRepository.HOLDER.INSTANCE }
    }
}