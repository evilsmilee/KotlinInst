package ru.nickb.kotlininst.data.firebase.common

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import ru.nickb.kotlininst.data.firebase.common.auth
import ru.nickb.kotlininst.data.firebase.common.database

class FirebaseHelper(private val activity: Activity) {
    val auth: FirebaseAuth = FirebaseAuth.getInstance()
    val database: DatabaseReference = FirebaseDatabase.getInstance().reference
    val storage: StorageReference = FirebaseStorage.getInstance().reference

    fun currentUserReference(): DatabaseReference =
        database.child("users").child(currentUid()!!)

    fun currentUid(): String? =
        auth.currentUser?.uid

}