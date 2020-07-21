package com.ubx.kyclibrary.helper

import com.ubx.kyclibrary.datarepository.KYCValuesDataRepository

class KYCValueHelper {
    companion object {
        fun setValue(key: String, value: String){
            getDataRepo().values[key] = value
        }

        fun getValue(key: String): String {
            return getDataRepo().values[key] ?: ""
        }

        private fun getDataRepo(): KYCValuesDataRepository {
            return KYCValuesDataRepository.instance
        }
    }
}