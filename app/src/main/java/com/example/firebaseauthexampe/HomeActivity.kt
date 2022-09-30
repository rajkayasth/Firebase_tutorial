package com.example.firebaseauthexampe

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.firebaseauthexampe.databinding.ActivityGoogleSignInBinding
import com.example.firebaseauthexampe.databinding.ActivityHomeBinding
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth

class HomeActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth


    private var _binding: ActivityHomeBinding? = null
    private val binding get() = _binding!!

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = DataBindingUtil.setContentView(this, R.layout.activity_home)

        auth = FirebaseAuth.getInstance()

        val email = auth.currentUser?.phoneNumber
        val name = auth.currentUser?.displayName


        binding.cardSignOut.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this@HomeActivity, PhoneActivity::class.java))
        }

        binding.textViewEmail.text = "$email \n$name"
    }
}