package ru.nickb.kotlininst.screens

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
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
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.feed_item.view.*
import ru.nickb.kotlininst.R
import ru.nickb.kotlininst.models.FeedPost
import ru.nickb.kotlininst.screens.common.*
import ru.nickb.kotlininst.common.ValueEventListenerAdapter
import ru.nickb.kotlininst.data.firebase.common.*
import ru.nickb.kotlininst.screens.common.setupBottomNavigation

class HomeActivity : BaseActivity(), FeedAdapter.Listener {


    private lateinit var mAdapter: FeedAdapter
    private lateinit var mFirebase: FirebaseHelper
    private var mLikesListeners: Map<String, ValueEventListener> = emptyMap()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setupBottomNavigation(0)
        mFirebase = FirebaseHelper(this)
        auth.addAuthStateListener {
            if (it.currentUser == null) {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }

    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser == null) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        } else {
           database.child("feed-posts").child(currentUser.uid)
                .addValueEventListener(ValueEventListenerAdapter {
                    val posts = it.children.map { it.asFeedPost()!! }
                        .sortedByDescending { it.timestampDate() }
                    mAdapter = FeedAdapter(this, posts)
                    feed_recycler.adapter = mAdapter
                    feed_recycler.layoutManager =
                        LinearLayoutManager(this)
                })
        }
    }

    override fun toogleLike(postId: String) {
        val reference = database.child("likes").child(postId).child(mFirebase.currentUid()!!)
        reference
            .addListenerForSingleValueEvent(ValueEventListenerAdapter {
                reference.setValueTrueOrRemove(!it.exists())

            })
    }

    override fun loadLikes(postId: String, position: Int) {
        fun createListener() =
            database.child("likes").child(postId).addValueEventListener(
                ValueEventListenerAdapter {
                    val usersLikes = it.children.map { it.key }.toSet()
                    val postLikes = FeedPostLikes(
                        usersLikes.size,
                        usersLikes.contains(mFirebase.currentUid())
                    )
                    mAdapter.updatePostLikes(position, postLikes)
                })
        if(mLikesListeners[postId] == null) {
            mLikesListeners += (postId to createListener())
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mLikesListeners.values.forEach{database.removeEventListener(it)}
    }
}

data class FeedPostLikes( val likesCount: Int, val likedByUser: Boolean)

class FeedAdapter(private val listener: Listener, private val posts: List<FeedPost>)
    : androidx.recyclerview.widget.RecyclerView.Adapter<FeedAdapter.ViewHolder>() {

    class ViewHolder(val view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view)

    private var postLikes: Map<Int, FeedPostLikes> = emptyMap()
    private  val defaultPostLikes =  FeedPostLikes(0, false)

    interface Listener{
        fun toogleLike(postId: String)
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
            like_image.setOnClickListener { listener.toogleLike(post.id) }
            like_image.setImageResource(
                if(likes.likedByUser)
                R.drawable.ic_likes_active
            else R.drawable.ic_likes_border)
            listener.loadLikes(post.id, position)
        }
    }

    private fun TextView.setCaptionText(username: String, caption: String) {
        val usernameSpannable = SpannableString(username)
        usernameSpannable.setSpan(StyleSpan(Typeface.BOLD), 0, usernameSpannable.length,
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



}