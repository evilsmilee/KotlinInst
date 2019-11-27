package ru.nickb.kotlininst.data.firebase

import androidx.lifecycle.LiveData
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import ru.nickb.kotlininst.common.toUnit
import ru.nickb.kotlininst.data.SearchRepository
import ru.nickb.kotlininst.data.common.map
import ru.nickb.kotlininst.data.firebase.common.FirebaseLiveData
import ru.nickb.kotlininst.data.firebase.common.database
import ru.nickb.kotlininst.models.SearchPost

class FirebaseSearchRepository: SearchRepository {
   override fun createPost(post: SearchPost): Task<Unit> =
        database.child("search-posts").push()
            .setValue(post.copy(caption = post.caption.toLowerCase())).toUnit()

    override fun searchPosts(text: String): LiveData<List<SearchPost>> {
        val reference = database.child("search-posts")
        val query = if (text.isEmpty()) {
            reference
        } else {
         reference.orderByChild("caption")
             .startAt(text.toLowerCase()).endAt("${text.toLowerCase()}\\uf88ff")
            }
           return FirebaseLiveData(query)
               .map {
                it.children.map { it.asSearchPost()!! }
            }
        }
}


private fun DataSnapshot.asSearchPost(): SearchPost? =
    getValue(SearchPost::class.java)?.copy(id = key!!)