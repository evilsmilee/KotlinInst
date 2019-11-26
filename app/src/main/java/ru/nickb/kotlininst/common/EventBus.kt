package ru.nickb.kotlininst.common

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.nickb.kotlininst.models.Comment
import ru.nickb.kotlininst.models.FeedPost

object  EventBus {
    private val liveDataBus = MutableLiveData<Event>()

    val events: LiveData<Event> = liveDataBus

    fun publish(event: Event) {
        liveDataBus.value = event
    }

}

sealed class Event {
    data class CreateComment(val postId: String, val comment: Comment): Event()
    data class CreateLike(val postId: String, val uid: String): Event()
    data class CreateFollow(val fromUid: String, val toUid: String): Event()
    data class CreateFeedPost(val post: FeedPost): Event()
}