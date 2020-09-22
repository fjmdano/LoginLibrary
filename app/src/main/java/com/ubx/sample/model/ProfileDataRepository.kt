package com.ubx.sample.model

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ProfileDataRepository {
    private val database = Firebase.database
    private lateinit var profileRef: DatabaseReference
    private var profileMap: MutableMap<String, Any> = mutableMapOf()

    fun setupFirebase(uid: String) {
        profileRef = database.getReference(KEY_USER).child(uid).child(KEY_PROFILE)

        profileRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val values: Map<String, Any> = dataSnapshot.value as Map<String, Any>
                organizeProfileData(values)
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                println("Error reading value of How To from Firebase DB")
                println(error.toString())
            }
        })
    }

    fun getProfile(): MutableMap<String, Any> {
        return profileMap
    }

    private fun organizeProfileData(values: Map<String, Any>) {
        values.forEach { (key, value) ->
            if (key in profileMap) {
                if (profileMap[key] != value) {
                    profileMap[key] = value
                }
            } else {
                profileMap[key] = value
            }
        }
    }



    private object HOLDER {
        val INSTANCE = ProfileDataRepository()
    }

    companion object {
        const val KEY_USER = "users"
        const val KEY_PROFILE = "profile"
        val instance: ProfileDataRepository by lazy { HOLDER.INSTANCE }
    }
}