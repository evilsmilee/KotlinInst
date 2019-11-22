package ru.nickb.kotlininst.screens.share

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.Tasks
import ru.nickb.kotlininst.data.UsersRepository
import ru.nickb.kotlininst.models.FeedPost
import ru.nickb.kotlininst.models.User
import ru.nickb.kotlininst.screens.common.BaseViewModel

class ShareViewModel(private val usersRepo: UsersRepository,
                    onFailureListener: OnFailureListener): BaseViewModel(onFailureListener) {
    val  user = usersRepo.getUser()

    fun share(user: User, imageUri: Uri?, caption: String) {
        if (imageUri != null) {
            usersRepo.uploadUserImage(user.uid, imageUri).onSuccessTask { downloadUrl ->
                Tasks.whenAll(
                    usersRepo.setUserImage(user.uid, downloadUrl!!),
                    usersRepo.createFeedPost(user.uid, mkFeedPost(user, caption, downloadUrl))
                )
            }.addOnFailureListener(onFailureListener)
        }
    }

    private fun mkFeedPost(user: User, caption: String, imageUri: Uri): FeedPost {
        return FeedPost(
            uid = user.uid,
            username = user.username,
            image = imageUri.toString(),
            caption = caption,
            photo = user.photo
        )
    }


}