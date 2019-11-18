package ru.nickb.kotlininst.activities.addfriends

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import ru.nickb.kotlininst.activities.map
import ru.nickb.kotlininst.data.FeedPostsRepository
import ru.nickb.kotlininst.data.UsersRepository
import ru.nickb.kotlininst.models.User

class AddFriendViewModel(private val onFailureListener: OnFailureListener,
                         private val usersRepo: UsersRepository,
                         private val feedPostsRepo: FeedPostsRepository) : ViewModel() {

    val usersAndFriends: LiveData<Pair<User, List<User>>> =
        usersRepo.getUsers()
        .map{allUsers ->
        val (userList, otherUserList) = allUsers.partition {
            it.uid == usersRepo.currentUid()
        }
        userList.first() to otherUserList
    }

   fun setFollow(currentUid: String, uid: String, follow: Boolean): Task<Void> {
           return (if (follow) {
               Tasks.whenAll(
                   usersRepo.addFollow(currentUid, uid),
                   usersRepo.addFollower(currentUid, uid),
                   feedPostsRepo.copyFeedPosts(postsAuthorUid = uid, uid = currentUid))
           } else {
               Tasks.whenAll(
                   usersRepo.deleteFollow(currentUid, uid),
                   usersRepo.deleteFollower(currentUid, uid),
                   feedPostsRepo.deleteFeedPosts(postsAuthorUid = uid, uid = currentUid))
           }).addOnFailureListener(onFailureListener)

   }

}