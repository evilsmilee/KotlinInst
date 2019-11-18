package ru.nickb.kotlininst.data.firebase.common

import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import ru.nickb.kotlininst.data.firebase.common.auth
import ru.nickb.kotlininst.data.firebase.common.database

class FirebaseHelper(private val activity: AppCompatActivity) {


  /*  fun uploadUserPhoto(photo: Uri, onSuccess: (UploadTask.TaskSnapshot) -> Unit) {
        storage.child("users/${currentUid()!!}/photo")
            .putFile(photo)
            .addOnCompleteListener{
                if(it.isSuccessful) {
                   onSuccess(it.result!!)
                } else {
                    activity.showToast(it.exception!!.message!!)
                }
            }
    }
*/
  /*  fun getDownloadUrl(onSuccess: (url: String) -> Unit) {
        storage.child("users/${currentUid()!!}/photo")
            .downloadUrl.addOnCompleteListener {
            if (it.isSuccessful) {
                onSuccess(it.result.toString())
            }
        }
    }*/

 /*  fun updateUserPhoto(photoUrl: String, onSuccess: () -> Unit) {
        database.child("users/${currentUid()!!}/photo").setValue(photoUrl)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    onSuccess()
                } else {
                    activity.showToast(it.exception!!.message!!)
                }
            }
    }*/

    /* fun updateUser(updates: Map<String, Any?>, onSuccess: () -> Unit) {
        database.child("users").child(currentUid()!!).updateChildren(updates)
            .addOnCompleteListener{
                if(it.isSuccessful) {
                    onSuccess()
                } else {
                    activity.showToast(it.exception!!.message!!)
                }
            }
    }*/


    /* fun updateEmail(email: String, onSuccess: () -> Unit) {
        auth.currentUser!!.updateEmail(email).addOnCompleteListener {
            if (it.isSuccessful) {
                onSuccess()
            } else {
                activity.showToast(it.exception!!.message!!)
            }
        }
    }

     fun reauthenticate(credential: AuthCredential, onSuccess: () -> Unit) {
        auth.currentUser!!.reauthenticate(credential).addOnCompleteListener {
            if (it.isSuccessful) {
                onSuccess()
            } else {
                activity.showToast(it.exception!!.message!!)
            }
        }
    }*/

    fun currentUserReference(): DatabaseReference =
        database.child("users").child(currentUid()!!)


    fun currentUid(): String? =
        auth.currentUser?.uid

}