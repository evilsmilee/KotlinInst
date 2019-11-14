package ru.nickb.kotlininst.activities.addfriends

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import ru.nickb.kotlininst.activities.asUser
import ru.nickb.kotlininst.activities.map
import ru.nickb.kotlininst.activities.task
import ru.nickb.kotlininst.models.User
import ru.nickb.kotlininst.utils.FirebaseLiveData
import ru.nickb.kotlininst.utils.TaskSourceOnCompleteListener
import ru.nickb.kotlininst.utils.ValueEventListenerAdapter

class AddFriendViewModel(private val repository: AddFriendsRepository) : ViewModel() {

    val usersAndFriends: LiveData<Pair<User, List<User>>> =
        repository.getUsers()
        .map{allUsers ->
        val (userList, otherUserList) = allUsers.partition {
            it.uid == repository.currentUid()
        }
        userList.first() to otherUserList
    }

   fun setFollow(currentUid: String, uid: String, follow: Boolean): Task<Void> {

           return if (follow) {
               Tasks.whenAll(
                   repository.addFollow(currentUid, uid),
                   repository.addFollower(currentUid, uid),
                   repository.copyFeedPosts(postsAuthorUid = uid, uid = currentUid))
           } else {
               Tasks.whenAll(
                   repository.deleteFollow(currentUid, uid),
                   repository.deleteFollower(currentUid, uid),
                   repository.deleteFeedPosts(postsAuthorUid = uid, uid = currentUid))
           }

   }

}