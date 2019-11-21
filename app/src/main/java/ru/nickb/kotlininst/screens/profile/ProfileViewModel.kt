package ru.nickb.kotlininst.screens.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import ru.nickb.kotlininst.data.UsersRepository

class ProfileViewModel(private val usersRepo: UsersRepository): ViewModel() {
    val user = usersRepo.getUser()
    lateinit var images: LiveData<List<String>>

    fun init(uid: String) {
        images = usersRepo.getImages(uid)
    }
}