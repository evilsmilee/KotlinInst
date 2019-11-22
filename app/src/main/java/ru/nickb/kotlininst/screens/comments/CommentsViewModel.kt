package ru.nickb.kotlininst.screens.comments

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.OnFailureListener
import ru.nickb.kotlininst.data.FeedPostsRepository
import ru.nickb.kotlininst.data.UsersRepository
import ru.nickb.kotlininst.models.Comment
import ru.nickb.kotlininst.models.User
import ru.nickb.kotlininst.screens.common.BaseViewModel

class CommentsViewModel(
    private val feedPostsRepo: FeedPostsRepository,
    onFailureListener: OnFailureListener,
    usersRepo: UsersRepository
) : BaseViewModel(onFailureListener) {

    val user: LiveData<User> = usersRepo.getUser()
    private lateinit var postId: String
    lateinit var comments : LiveData<List<Comment>>

        fun init(postId: String) {
            this.postId = postId
            comments = feedPostsRepo.getComments(postId)
        }

    fun createComment(text: String, user: User) {
        val comment = Comment(
                uid = user.uid,
                username = user.username,
                photo = user.photo,
                text = text)
        feedPostsRepo.createComment(postId, comment).addOnFailureListener(onFailureListener)
    }

}
