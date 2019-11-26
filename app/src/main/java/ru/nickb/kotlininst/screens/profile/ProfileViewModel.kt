package ru.nickb.kotlininst.screens.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.OnFailureListener
import ru.nickb.kotlininst.data.UsersRepository
import ru.nickb.kotlininst.screens.common.BaseViewModel

class ProfileViewModel(private val usersRepo: UsersRepository,
                       onFailureListener: OnFailureListener): BaseViewModel(onFailureListener) {
    val user = usersRepo.getUser()
    lateinit var images: LiveData<List<String>>

    fun init(uid: String) {
        if (!this::images.isInitialized) {
            images = usersRepo.getImages(uid)
        }

    }
}