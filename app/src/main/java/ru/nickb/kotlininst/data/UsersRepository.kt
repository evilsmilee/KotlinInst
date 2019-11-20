package ru.nickb.kotlininst.data

import android.net.Uri
import androidx.lifecycle.LiveData
import com.google.android.gms.tasks.Task
import ru.nickb.kotlininst.models.User

interface UsersRepository {
     fun getUsers(): LiveData<List<User>>
     fun currentUid(): String?
     fun addFollow(fromUid: String, toUid: String): Task<Unit>
     fun deleteFollow(fromUid: String, toUid: String): Task<Unit>
     fun addFollower(fromUid: String, toUid: String): Task<Unit>
     fun deleteFollower(fromUid: String, toUid: String): Task<Unit>
     fun getUser(): LiveData<User>
     fun uploadUserPhoto(localImage: Uri): Task<Uri>
     fun updateUserPhoto(downloadUrl: Uri): Task<Unit>
     fun updateEmail(currentEmail: String, newEmail: String, password: String): Task<Unit>
     fun updateUserProfile(currentUser: User, newUser: User): Task<Unit>
     fun getImages(uid: String): LiveData<List<String>>
     fun isUserExistsForEmail(email: String): Task<Boolean>
     fun createUser(user: User, password: String): Task<Unit>


}