package ru.nickb.kotlininst.screens.comments

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.comments_item.view.*
import ru.nickb.kotlininst.R
import ru.nickb.kotlininst.models.Comment
import ru.nickb.kotlininst.screens.common.SimpleCallback
import ru.nickb.kotlininst.screens.common.loadUserPhoto
import ru.nickb.kotlininst.screens.common.setCaptionText

class CommentsAdapter: RecyclerView.Adapter<CommentsAdapter.ViewHolder>() {
    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    private var comments = listOf<Comment>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.comments_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val comment = comments[position]
        with(holder.view) {
            photo.loadUserPhoto(comment.photo)
            text.setCaptionText(comment.username, comment.text, comment.timestampDate())
        }
    }

    fun updateComments(newComments: List<Comment>) {
        val diffResult = DiffUtil.calculateDiff(SimpleCallback(comments, newComments) {it.id})
        this.comments = newComments
        diffResult.dispatchUpdatesTo(this)
    }

    override fun getItemCount(): Int = comments.size

}
