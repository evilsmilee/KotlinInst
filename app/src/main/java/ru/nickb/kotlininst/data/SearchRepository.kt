package ru.nickb.kotlininst.data

import androidx.lifecycle.LiveData
import com.google.android.gms.tasks.Task
import ru.nickb.kotlininst.models.SearchPost

interface SearchRepository {
    fun searchPosts(text: String): LiveData<List<SearchPost>>
    fun createPost(post: SearchPost): Task<Unit>
}