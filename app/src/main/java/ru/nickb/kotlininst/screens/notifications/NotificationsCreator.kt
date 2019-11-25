package ru.nickb.kotlininst.screens.notifications

import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.Observer
import ru.nickb.kotlininst.common.Event
import ru.nickb.kotlininst.common.EventBus
import ru.nickb.kotlininst.data.FeedPostsRepository
import ru.nickb.kotlininst.data.NotificationsRepository
import ru.nickb.kotlininst.data.UsersRepository
import ru.nickb.kotlininst.data.common.observeFirstNotNull
import ru.nickb.kotlininst.data.common.zip
import ru.nickb.kotlininst.models.Notification
import ru.nickb.kotlininst.models.NotificationType

class NotificationsCreator(
    private val notificationsRepo: NotificationsRepository,
    private val usersRepo: UsersRepository,
    private val feedPostsRepo: FeedPostsRepository
) : LifecycleOwner {

    private val lifecycleRegistry = LifecycleRegistry(this)

    init {
        lifecycleRegistry.currentState = Lifecycle.State.CREATED
        lifecycleRegistry.currentState = Lifecycle.State.STARTED
        EventBus.events.observe(this, Observer {
            it?.let { event ->
                when (event) {
                    is Event.CreateFollow -> {
                        getUser(event.fromUid).observeFirstNotNull(this) { user ->
                            val notification = Notification(
                                uid = user.uid,
                                username = user.username,
                                photo = user.photo,
                                type = NotificationType.Follow
                            )
                            notificationsRepo.createNotification(event.toUid, notification)
                                .addOnFailureListener {
                                    Log.d("Nfc", "Failed to create notification", it)
                                }
                        }
                    }
                    is Event.CreateLike -> {
                        val userData = usersRepo.getUser(event.uid)
                        val postData = feedPostsRepo.getFeedPost(event.uid, event.postId)
                        userData.zip(postData).observeFirstNotNull(this) { (user, post) ->
                            val notification = Notification(
                                uid = user.uid,
                                username = user.username,
                                photo = user.photo,
                                postId = post.id!!,
                                postImage = post.image,
                                type = NotificationType.Like
                            )
                            notificationsRepo.createNotification(post.uid, notification)
                                .addOnFailureListener {
                                    Log.d("Nfc", "Failed to create notification", it)
                                }
                        }
                    }
                    is Event.CreateComment -> {
                        feedPostsRepo.getFeedPost(event.comment.uid!!, event.postId)
                            .observeFirstNotNull(this) { post ->
                                val notification = Notification(
                                    uid = event.comment.uid,
                                    username = event.comment.username,
                                    photo = event.comment.photo,
                                    postId = event.postId,
                                    postImage = post.image,
                                    commentText = event.comment.text,
                                    type = NotificationType.Comment
                                )
                                notificationsRepo.createNotification(
                                    post.uid,
                                    notification
                                )
                                    .addOnFailureListener {
                                        Log.d("Nfc", "Failed to create notification", it)
                                    }
                            }
                    }
                }
            }
        })
    }

    private fun getUser(uid: String) =
        usersRepo.getUser(uid)


    override fun getLifecycle(): Lifecycle = lifecycleRegistry

}