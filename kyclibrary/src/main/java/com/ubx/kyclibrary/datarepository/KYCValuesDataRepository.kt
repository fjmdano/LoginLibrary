package com.ubx.kyclibrary.datarepository

import android.graphics.Bitmap

class KYCValuesDataRepository {
    var stringValues: MutableMap<String, String> = mutableMapOf()
    var imageValues: MutableMap<String, Bitmap> = mutableMapOf()
    var booleanValues: MutableMap<String, Boolean> = mutableMapOf()
    var listValues: MutableMap<String, List<String>> = mutableMapOf()

    private object HOLDER {
        val INSTANCE = KYCValuesDataRepository()
    }

    companion object {
        private const val TAG = "KYCLibrary"
        val instance: KYCValuesDataRepository by lazy { HOLDER.INSTANCE }
    }
}