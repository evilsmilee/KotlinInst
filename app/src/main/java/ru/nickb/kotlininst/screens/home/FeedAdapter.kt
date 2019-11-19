package ru.nickb.kotlininst.screens.home

import android.graphics.Typeface
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.feed_item.view.*
import ru.nickb.kotlininst.R
import ru.nickb.kotlininst.models.FeedPost
import ru.nickb.kotlininst.screens.common.SimpleCallback
import ru.nickb.kotlininst.screens.common.loadImage
import ru.nickb.kotlininst.screens.common.loadUserPhoto
import ru.nickb.kotlininst.screens.common.showToast

class FeedAdapter(private val listener: Listener)
    : RecyclerView.Adapter<FeedAdapter.ViewHolder>() {

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    private var posts =  listOf<FeedPost>()
    private var postLikes: Map<Int, FeedPostLikes> = emptyMap()
    private  val defaultPostLikes = FeedPostLikes(0, false)

    interface Listener{
        fun toggleLike(postId: String)
        fun loadLikes(postId: String, position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.feed_item, parent, false)
        return ViewHolder(view)
    }

    fun updatePostLikes(position: Int, likes: FeedPostLikes) {
        postLikes += (position to likes)
        notifyItemChanged(position)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val post = posts[position]

        val likes = postLikes[position] ?: defaultPostLikes
        with(holder.view) {
           user_photo_image.loadUserPhoto(post.image)
           username_text.text = post.username
           post_image.loadImage(post.image)
           if(likes.likesCount == 0) {
               likes_text.visibility = View.GONE
           } else {
               likes_text.visibility = View.GONE
               val likesCountText = holder.view.context.resources.getQuantityString(
                   R.plurals.likes_count, likes.likesCount, likes.likesCount)
               likes_text.text = likesCountText
           }
            caption_text.setCaptionText(post.username, post.caption)
            like_image.setOnClickListener { listener.toggleLike(post.id) }
            like_image.setImageResource(
                if(likes.likedByUser)
                    R.drawable.ic_likes_active
            else R.drawable.ic_likes_border
            )
            listener.loadLikes(post.id, position)
        }
    }

    private fun TextView.setCaptionText(username: String, caption: String) {
        val usernameSpannable = SpannableString(username)
        usernameSpannable.setSpan(
            StyleSpan(Typeface.BOLD), 0, usernameSpannable.length,
            SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
        usernameSpannable.setSpan(object: ClickableSpan() {
            override fun onClick(widget: View) {
                widget.context.showToast(context.getString(R.string.user_name_is_clicked))
            }
            override fun updateDrawState(ds: TextPaint?) {}
        }, 0, usernameSpannable.length,
            SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)

      text = SpannableStringBuilder().append(usernameSpannable).append(" ")
            .append(caption)
       movementMethod = LinkMovementMethod.getInstance()
    }

    override fun getItemCount() = posts.size

    fun updatePosts(newPosts: List<FeedPost>) {
       val diffResult =  DiffUtil.calculateDiff(
           SimpleCallback(
               this.posts,
               posts
           ) { it.id })
        this.posts = newPosts
        diffResult.dispatchUpdatesTo(this)
    }

}

