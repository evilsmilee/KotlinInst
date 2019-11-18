package ru.nickb.kotlininst.activities

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.tasks.OnFailureListener
import ru.nickb.kotlininst.activities.addfriends.AddFriendViewModel
import ru.nickb.kotlininst.data.firebase.FirebaseFeedPostsRepository
import ru.nickb.kotlininst.activities.editprofile.EditProfileViewModel
import ru.nickb.kotlininst.data.FirebaseUsersRepository

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(private val onFailureListener: OnFailureListener): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddFriendViewModel::class.java)) {
            return AddFriendViewModel(onFailureListener, FirebaseUsersRepository(), FirebaseFeedPostsRepository()) as T
        }
        else if (modelClass.isAssignableFrom(EditProfileViewModel::class.java)) {
            return EditProfileViewModel(onFailureListener,FirebaseUsersRepository()) as T
        }
        else {
            error("Unknown view model class $modelClass")
        }
    }
}