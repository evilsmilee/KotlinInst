package ru.nickb.kotlininst.data.firebase.common

import androidx.lifecycle.LiveData
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import ru.nickb.kotlininst.models.Comment
import ru.nickb.kotlininst.models.FeedPost
import ru.nickb.kotlininst.models.User

val auth: FirebaseAuth =
    FirebaseAuth.getInstance()

val storage: StorageReference = FirebaseStorage.getInstance().reference

val database: DatabaseReference = FirebaseDatabase.getInstance().reference


fun currentUid(): String? = auth.currentUser?.uid

fun DataSnapshot.asComment(): Comment? =
    getValue(Comment::class.java)?.copy(id = key)

fun DataSnapshot.asUser(): User? =
   getValue(User::class.java)?.copy(uid = key!!)

fun DataSnapshot.asFeedPost(): FeedPost? =
     getValue(FeedPost::class.java)?.copy(id = key)

fun DatabaseReference.setValueTrueOrRemove(value: Boolean): Task<Void> =
    if (value) setValue(true) else removeValue()

fun DatabaseReference.liveData(): LiveData<DataSnapshot> =
    FirebaseLiveData(this)