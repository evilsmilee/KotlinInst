package ru.nickb.kotlininst.screens.share

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.alexbezhan.instagram.common.SingleLiveEvent
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.Tasks
import ru.nickb.kotlininst.data.FeedPostsRepository
import ru.nickb.kotlininst.data.UsersRepository
import ru.nickb.kotlininst.models.FeedPost
import ru.nickb.kotlininst.models.User
import ru.nickb.kotlininst.screens.common.BaseViewModel

class ShareViewModel(private val usersRepo: UsersRepository,
                     private val feedPostsRepo: FeedPostsRepository,
                    onFailureListener: OnFailureListener): BaseViewModel(onFailureListener) {
    val  user = usersRepo.getUser()
    private val _shareCompletedEvent = SingleLiveEvent<Unit>()
    val shareCompletedEvent = _shareCompletedEvent
    fun share(user: User, imageUri: Uri?, caption: String) {
        if (imageUri != null) {
            usersRepo.uploadUserImage(user.uid, imageUri).onSuccessTask { downloadUrl ->
                Tasks.whenAll(
                    usersRepo.setUserImage(user.uid, downloadUrl!!),
                    feedPostsRepo.createFeedPost(user.uid, mkFeedPost(user, caption,
                        downloadUrl.toString()))
                )
            }.addOnCompleteListener {
                _shareCompletedEvent.call()
            }.addOnFailureListener(onFailureListener)
        }
    }

    private fun mkFeedPost(user: User, caption: String, imageUri: String): FeedPost {
        return FeedPost(
            uid = user.uid,
            username = user.username,
            image = imageUri,
            caption = caption,
            photo = user.photo
        )
    }


}