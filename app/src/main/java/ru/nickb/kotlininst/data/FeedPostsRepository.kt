package ru.nickb.kotlininst.data

import androidx.lifecycle.LiveData
import com.google.android.gms.tasks.Task
import ru.nickb.kotlininst.models.Comment
import ru.nickb.kotlininst.models.FeedPost


interface FeedPostsRepository {

    fun copyFeedPosts(postsAuthorUid: String, uid: String): Task<Unit>
    fun deleteFeedPosts(postsAuthorUid: String, uid: String): Task<Unit>
    fun getFeedPosts(uid: String): LiveData<List<FeedPost>>
    fun toggleLike(postId: String, uid: String): Task<Unit>
    fun getLikes(postId: String) : LiveData<List<FeedPostLike>>
    fun getComments(postId: String): LiveData<List<Comment>>
    fun createComment(postId: String, comment: Comment): Task<Unit>

}

data class FeedPostLike(val userId: String?)