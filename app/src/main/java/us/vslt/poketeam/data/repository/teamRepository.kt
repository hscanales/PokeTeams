package us.vslt.poketeam.data.repository

import com.google.firebase.auth.FirebaseAuth

class teamRepository {

    lateinit var firebaseAuth: FirebaseAuth

    init {
        firebaseAuth = FirebaseAuth.getInstance()
    }


}