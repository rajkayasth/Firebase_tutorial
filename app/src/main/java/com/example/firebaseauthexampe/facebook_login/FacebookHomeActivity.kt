package com.example.firebaseauthexampe.facebook_login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.firebaseauthexampe.R
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso

class FacebookHomeActivity : AppCompatActivity() {

    lateinit var auth: FirebaseAuth
    lateinit var name: TextView
    lateinit var image: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_facebook_home)

        auth = FirebaseAuth.getInstance()

        name = findViewById(R.id.tvFaceHome)

        image = findViewById(R.id.ivFaceHome)

        name.text = auth.currentUser?.displayName

        Picasso.get().load(auth.currentUser?.photoUrl).into(image)


        var btnSignout = findViewById<Button>(R.id.btnSignOut)

        btnSignout.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this, FacebookLoginActivity::class.java))
        }
    }
}