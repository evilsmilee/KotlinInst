package ru.nickb.kotlininst.screens.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.alexbezhan.instagram.common.SingleLiveEvent
import com.google.android.gms.tasks.OnFailureListener
import ru.nickb.kotlininst.data.FeedPostsRepository
import ru.nickb.kotlininst.data.common.map
import ru.nickb.kotlininst.data.firebase.FirebaseFeedPostsRepository
import ru.nickb.kotlininst.models.FeedPost
import ru.nickb.kotlininst.screens.common.BaseViewModel

class HomeViewModel(onFailureListener: OnFailureListener,
    private val feedPostsRepo: FeedPostsRepository): BaseViewModel(onFailureListener) {
    lateinit var uid: String
    lateinit var feedPosts: LiveData<List<FeedPost>>
    private var loadedLikes = mapOf<String, LiveData<FeedPostLikes>>()
    private val _goToCommentsScreen = SingleLiveEvent<String>()
    val goToCommentsScreen = _goToCommentsScreen

    fun init(uid: String) {
        if (!this::uid.isInitialized) {
            this.uid = uid
            feedPosts = feedPostsRepo.getFeedPosts(uid).map {
                it.sortedByDescending { it.timestampDate() }
            }
        }
    }

    fun toggleLike(postId: String) {
        feedPostsRepo.toggleLike(postId, uid).addOnFailureListener(onFailureListener)
    }

    fun getLikes(postId: String): LiveData<FeedPostLikes>? = loadedLikes[postId]

    fun loadLikes(postId: String): LiveData<FeedPostLikes> {
        val existingLoadedLikes = loadedLikes[postId]
        if (existingLoadedLikes == null) {
            val liveData = feedPostsRepo.getLikes(postId).map { likes ->
                FeedPostLikes(
                    likesCount = likes.size,
                    likedByUser = likes.find { it.userId == uid } != null)
            }
            loadedLikes += postId to liveData
            return liveData
        } else {
            return existingLoadedLikes
        }
    }

    fun openComments(postId: String) {
        _goToCommentsScreen.value = postId
    }
}

