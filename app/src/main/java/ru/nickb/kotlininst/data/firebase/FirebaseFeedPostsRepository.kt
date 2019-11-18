package ru.nickb.kotlininst.data.firebase

import com.google.android.gms.tasks.Task
import ru.nickb.kotlininst.common.TaskSourceOnCompleteListener
import ru.nickb.kotlininst.common.ValueEventListenerAdapter
import ru.nickb.kotlininst.common.task
import ru.nickb.kotlininst.common.toUnit
import ru.nickb.kotlininst.data.FeedPostsRepository
import ru.nickb.kotlininst.data.firebase.common.database

class FirebaseFeedPostsRepository: FeedPostsRepository {


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