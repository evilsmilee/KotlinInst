package ru.nickb.kotlininst.data.firebase

import androidx.lifecycle.LiveData
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import ru.nickb.kotlininst.common.*
import ru.nickb.kotlininst.data.FeedPostLike
import ru.nickb.kotlininst.data.FeedPostsRepository
import ru.nickb.kotlininst.data.common.map
import ru.nickb.kotlininst.data.firebase.common.*
import ru.nickb.kotlininst.models.Comment
import ru.nickb.kotlininst.models.FeedPost

class FirebaseFeedPostsRepository : FeedPostsRepository {


    override fun createFeedPost(uid: String, feedPost: FeedPost): Task<Unit> {
        val reference = database.child("feed-posts").child(uid)
            .push()
       return reference.setValue(feedPost).toUnit().addOnSuccessListener {
                EventBus.publish(Event.CreateFeedPost(feedPost.copy(id = reference.key)))
            }
    }

    override fun getLikes(postId: String): LiveData<List<FeedPostLike>> =
        FirebaseLiveData(database.child("likes").child(postId)).map {
            it.children.map { FeedPostLike(it.key) }
        }

    override fun getComments(postId: String): LiveData<List<Comment>> =
        FirebaseLiveData(database.child("comments").child(postId)).map {
            it.children.map { it.asComment()!! }
        }

   /* override fun createFeedPost(uid: String, feedPost: FeedPost): Task<Unit> =
        database.child("feed-posts").child(uid)
            .push().setValue(feedPost).toUnit()*/

    override fun createComment(postId: String, comment: Comment): Task<Unit> =
        database.child("comments").child(postId).push().setValue(comment).toUnit()
            .addOnSuccessListener {
                EventBus.publish(Event.CreateComment(postId, comment))
            }


    override fun toggleLike(postId: String, uid: String): Task<Unit> {
        val reference = database.child("likes").child(postId).child(uid)
        return task { taskSource ->
            reference.addListenerForSingleValueEvent(ValueEventListenerAdapter {like->
                if (!like.exists()) {
                    reference.setValue(true).addOnSuccessListener {
                        EventBus.publish(Event.CreateLike(postId, uid))
                    }
                } else {
                    reference.removeValue()
                }
                taskSource.setResult(Unit)
            })
        }

    }

    override fun getFeedPost(uid: String, postId: String): LiveData<FeedPost> =
        FirebaseLiveData(database.child("feed-posts").child(uid).child(postId)).map {
            it.asFeedPost()!!
        }



    override fun getFeedPosts(uid: String): LiveData<List<FeedPost>> =
        FirebaseLiveData(database.child("feed-posts").child(uid)).map {
            it.children.map { it.asFeedPost()!! }
        }


    override fun copyFeedPosts(postsAuthorUid: String, uid: String): Task<Unit> =
        task { taskSource ->
            database.child("feed-posts").child(postsAuthorUid)
                .orderByChild("uid")
                .equalTo(postsAuthorUid)
                .addListenerForSingleValueEvent(ValueEventListenerAdapter {
                    val postsMap = it.children.map { it.key to it.value }.toMap()
                    database.child("feed-posts").child(uid)
                        .updateChildren(postsMap)
                        .toUnit()
                        .addOnCompleteListener(
                            TaskSourceOnCompleteListener(
                                taskSource
                            )
                        )
                })

        }

    override fun deleteFeedPosts(postsAuthorUid: String, uid: String): Task<Unit> =
        task { taskSource ->
            database.child("feed-posts").child(uid)
                .orderByChild(uid)
                .equalTo(postsAuthorUid)
                .addListenerForSingleValueEvent(ValueEventListenerAdapter {
                    val postsMap = it.children.map { it.key to it.value }.toMap()
                    database.child("feed-posts").child(uid)
                        .updateChildren(postsMap)
                        .toUnit()
                        .addOnCompleteListener(
                            TaskSourceOnCompleteListener(
                                taskSource
                            )
                        )

                })
        }

    private fun DataSnapshot.asFeedPost(): FeedPost? =
        getValue(FeedPost::class.java)?.copy(id = key)

    private fun DataSnapshot.asComment(): Comment? =
        getValue(Comment::class.java)?.copy(id = key)


}