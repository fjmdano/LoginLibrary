package com.ubx.kyclibrary.helper

import android.graphics.Bitmap
import android.util.Log
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.ubx.kyclibrary.datarepository.KYCValuesDataRepository
import java.io.ByteArrayOutputStream

class KYCValueHelper {
    companion object {
        /**
         * Store value
         * @param key item key
         * @param value item value
         */
        fun setValue(key: String, value: String){
            getDataRepo().values[key] = value
        }

        /**
         * Retrieve stored value
         * @param key item key
         * @return empty string if key not found; else item value
         */
        fun getValue(key: String): String {
            return getDataRepo().values[key] ?: ""
        }

        /**
         * Store bitmap
         * @param key item key
         * @param value item value (in Bitmap)
         */
        fun setBitmap(key: String, value: Bitmap){
            getDataRepo().images[key] = value
        }

        /**
         * Retrieve stored bitmap
         * @param key item key
         * @return null if key not found; else item value (in Bitmap)
         */
        fun getBitmap(key: String): Bitmap? {
            return getDataRepo().images[key]
        }

        /**
         * Store items in DB
         */
        fun storeInDB(): Boolean {
            var isOK = false
            val outputStream = ByteArrayOutputStream()
            //Check if logged in
            val firebaseUser = Firebase.auth.currentUser ?: return isOK
            try {
                val database = Firebase.database.getReference("users")
                    .child(firebaseUser.uid).child("profile")
                getDataRepo().values.forEach {
                    if (it.key != "email" && it.key != "password") {
                        database.child(it.key).setValue(it.value)
                    }
                }

                val storage = Firebase.storage.getReference("users")
                    .child(firebaseUser.uid).child("profile")
                getDataRepo().images.forEach {
                    Log.d("FirebaseDB", "Saving $it.key")
                    val path = storage.child(it.key + ".png")
                    it.value.compress(Bitmap.CompressFormat.PNG, 100, outputStream)

                    val uploadTask = path.putBytes(outputStream.toByteArray())
                    uploadTask.addOnFailureListener {
                        Log.d("FirebaseDB", "storeInFirebaseStorage: failure", it)
                    }.addOnSuccessListener { _ ->
                        database.child(it.key).setValue(path.path)
                    }
                }
                isOK = true
            } catch (e: Exception){
                Log.d("FirebaseDB", "storeInFirebaseDB: failure", e)
                isOK = false
            }
            return isOK
        }

        private fun getDataRepo(): KYCValuesDataRepository {
            return KYCValuesDataRepository.instance
        }
    }
}