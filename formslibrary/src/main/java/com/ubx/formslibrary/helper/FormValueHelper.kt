package com.ubx.formslibrary.helper

import android.graphics.Bitmap
import android.util.Log
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.ubx.formslibrary.model.datarepository.FormValuesDataRepository
import java.io.ByteArrayOutputStream

class FormValueHelper {
    companion object {

        /**
         * Clear currently stored values
         */
        fun clearValues() {
            getDataRepo().stringValues.clear()
            getDataRepo().imageValues.clear()
            getDataRepo().booleanValues.clear()
            getDataRepo().listValues.clear()
        }

        /**
         * Store value
         * @param key item key
         * @param value item value
         */
        fun setString(key: String, value: String){
            getDataRepo().stringValues[key] = value
        }

        /**
         * Retrieve stored value
         * @param key item key
         * @return empty string if key not found; else item value
         */
        fun getString(key: String): String {
            return getDataRepo().stringValues[key] ?: ""
        }

        /**
         * Store bitmap
         * @param key item key
         * @param value item value (in Bitmap)
         */
        fun setImage(key: String, value: Bitmap){
            getDataRepo().imageValues[key] = value
        }

        /**
         * Retrieve stored bitmap
         * @param key item key
         * @return null if key not found; else item value (in Bitmap)
         */
        fun getImage(key: String): Bitmap? {
            return getDataRepo().imageValues[key]
        }

        /**
         * Store value
         * @param key item key
         * @param value item value
         */
        fun setBoolean(key: String, value: Boolean){
            getDataRepo().booleanValues[key] = value
        }

        /**
         * Retrieve stored value
         * @param key item key
         * @return empty string if key not found; else item value
         */
        fun getBoolean(key: String): Boolean {
            return getDataRepo().booleanValues[key] ?: false
        }

        /**
         * Store value
         * @param key item key
         * @param value item value
         */
        fun setList(key: String, value: List<String>){
            getDataRepo().listValues[key] = value
        }

        /**
         * Retrieve stored value
         * @param key item key
         * @return empty string if key not found; else item value
         */
        fun getList(key: String): List<String> {
            return getDataRepo().listValues[key] ?: mutableListOf()
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
                getDataRepo().stringValues.forEach {
                    if (it.key != "email" && it.key != "password") {
                        database.child(it.key).setValue(it.value)
                    }
                }

                val storage = Firebase.storage.getReference("users")
                    .child(firebaseUser.uid).child("profile")
                getDataRepo().imageValues.forEach {
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

        private fun getDataRepo(): FormValuesDataRepository {
            return FormValuesDataRepository.instance
        }
    }
}