package com.ubx.kyclibrary.datarepository

class KYCValuesDataRepository {
    var values: MutableMap<String, String> = mutableMapOf()

    private object HOLDER {
        val INSTANCE = KYCValuesDataRepository()
    }

    companion object {
        private const val TAG = "KYCLibrary"
        val instance: KYCValuesDataRepository by lazy { KYCValuesDataRepository.HOLDER.INSTANCE }
    }
}