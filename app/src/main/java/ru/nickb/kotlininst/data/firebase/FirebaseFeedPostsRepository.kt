package ru.nickb.kotlininst.data.firebase

import androidx.lifecycle.LiveData
import com.google.android.gms.tasks.Task
import ru.nickb.kotlininst.common.TaskSourceOnCompleteListener
import ru.nickb.kotlininst.common.ValueEventListenerAdapter
import ru.nickb.kotlininst.common.task
import ru.nickb.kotlininst.common.toUnit
import ru.nickb.kotlininst.data.FeedPostLike
import ru.nickb.kotlininst.data.FeedPostsRepository
import ru.nickb.kotlininst.data.common.map
import ru.nickb.kotlininst.data.firebase.common.*
import ru.nickb.kotlininst.models.FeedPost

class FirebaseFeedPostsRepository: FeedPostsRepository {


    override fun getLikes(postId: String): LiveData<List<FeedPostLike>> =
        FirebaseLiveData(database.child("likes").child(postId)).map {
            it.children.map { FeedPostLike(it.key) }
        }


    override fun toggleLike(postId: String, uid: String): Task<Unit> {
        val reference = database.child("likes").child(postId).child(uid)
        return task {taskSource ->
            reference.addListenerForSingleValueEvent(ValueEventListenerAdapter {
                reference.setValueTrueOrRemove(!it.exists())
                taskSource.setResult(Unit)
            })
        }

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





}