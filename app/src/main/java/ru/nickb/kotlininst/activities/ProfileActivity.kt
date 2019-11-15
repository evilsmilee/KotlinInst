package ru.nickb.kotlininst.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import kotlinx.android.synthetic.main.activity_profile.*
import ru.nickb.kotlininst.R
import ru.nickb.kotlininst.activities.addfriends.AddFriendsActivity
import ru.nickb.kotlininst.activities.editprofile.EditProfileActivity
import ru.nickb.kotlininst.models.User
import ru.nickb.kotlininst.utils.FirebaseHelper
import ru.nickb.kotlininst.utils.ValueEventListenerAdapter


class ProfileActivity : BaseActivity(4) {
    private lateinit var mUser: User
    private lateinit var mFirebase: FirebaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        setupBottomNavigation()

        edit_profile_button.setOnClickListener {
            val intent = Intent(this, EditProfileActivity::class.java)
            startActivity(intent)
        }
        settings_image.setOnClickListener {
            val intent = Intent(this, ProfileSettingsActivity::class.java)
            startActivity(intent)
        }
        add_friends_image.setOnClickListener {
            val intent = Intent(this, AddFriendsActivity::class.java)
            startActivity(intent)
        }
        mFirebase = FirebaseHelper(this)
        mFirebase.currentUserReference().addValueEventListener(ValueEventListenerAdapter {
            mUser = it.asUser()!!
            profile_image.loadUserPhoto(mUser.photo)
            username_text.text = mUser.username
        })

        images_recycler.layoutManager = GridLayoutManager(this, 3)
        mFirebase.database.child("images").child(mFirebase.currentUid()!!)
            .addValueEventListener(ValueEventListenerAdapter{
              val images =   it.children.map { it.getValue(String::class.java)!! }
                images_recycler.adapter = ImagesAdapter(images + images + images + images)
            })
    }

}


class ImagesAdapter(private val images: List<String>) :
        RecyclerView.Adapter<ImagesAdapter.ViewHolder>() {

    class ViewHolder(val image: ImageView) : RecyclerView.ViewHolder(image)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val image = LayoutInflater.from(parent.context)
            .inflate(R.layout.image_item, parent, false) as ImageView
        return ViewHolder(image = image)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.image.loadImage(images[position])

    }



    override fun getItemCount(): Int = images.size
}

class SquareImageView(context: Context, attrs: AttributeSet) : ImageView(context, attrs) {
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec)
    }
}