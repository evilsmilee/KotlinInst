package ru.nickb.kotlininst.screens.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.google.android.gms.tasks.OnFailureListener
import ru.nickb.kotlininst.data.SearchRepository
import ru.nickb.kotlininst.models.FeedPost
import ru.nickb.kotlininst.models.SearchPost
import ru.nickb.kotlininst.screens.common.BaseViewModel

class SearchViewModel(searchRepo: SearchRepository,
                      onFailureListener: OnFailureListener): BaseViewModel(onFailureListener) {

    private val searchText = MutableLiveData<String>()

    val posts: LiveData<List<SearchPost>> = Transformations.switchMap(searchText) { text ->
        searchRepo.searchPosts(text)
    }

    fun setSearchText(text: String) {
        searchText.value = text
    }
}