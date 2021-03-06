package com.ubx.kyclibrary.helper

import android.app.Activity
import android.util.Log
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.ubx.kyclibrary.datarepository.KYCValuesDataRepository

class KYCValueHelper {
    companion object {
        fun setValue(key: String, value: String){
            getDataRepo().values[key] = value
        }

        fun getValue(key: String): String {
            return getDataRepo().values[key] ?: ""
        }

        fun storeInDB(activity: Activity) {
            //Check if logged in
            val firebaseUser = Firebase.auth.currentUser ?: return
            try {
                val database = Firebase.database.getReference("users")
                    .child(firebaseUser.uid).child("profile")
                getDataRepo().values.forEach {
                    if (it.key != "email" && it.key != "password") {
                        database.child(it.key).setValue(it.value)
                    }
                }
                activity.finish()
            } catch (e: Exception){
                Log.d("FirebaseDB", "storeInDB: failure", e)
            }
        }

        private fun getDataRepo(): KYCValuesDataRepository {
            return KYCValuesDataRepository.instance
        }
    }
}