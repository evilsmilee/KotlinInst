package ru.nickb.kotlininst.screens.search

import android.util.Log
import androidx.lifecycle.Observer
import ru.nickb.kotlininst.common.BaseEventListener
import ru.nickb.kotlininst.common.Event
import ru.nickb.kotlininst.common.EventBus
import ru.nickb.kotlininst.data.SearchRepository
import ru.nickb.kotlininst.models.SearchPost

class SearchPostCreator(searchRepo: SearchRepository): BaseEventListener() {

    init {
        EventBus.events.observe(this, Observer { it?.let {event ->
            when(event) {
                is Event.CreateFeedPost -> {
                   val searchPost =  with(event.post) {
                        SearchPost(
                            image = image,
                            caption = caption,
                            id = id!!
                        )
                    }
                    searchRepo.createPost(searchPost).addOnFailureListener {
                        Log.d("SPC", "Error")
                    }
                }
                else -> {

                }
            }
        } })
    }

}