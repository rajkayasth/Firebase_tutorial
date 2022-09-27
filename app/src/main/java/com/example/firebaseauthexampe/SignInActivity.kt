package com.example.firebaseauthexampe

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.firebaseauthexampe.databinding.ActivitySignInBinding
import com.google.firebase.auth.FirebaseAuth

class SignInActivity : AppCompatActivity() {
    private var _signInBinding: ActivitySignInBinding? = null
    private val signInBinding get() = _signInBinding!!
    private lateinit var firebaseAuth: FirebaseAuth



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _signInBinding = DataBindingUtil.setContentView(this, R.layout.activity_sign_in)


        firebaseAuth = FirebaseAuth.getInstance()


        signInBinding.textViewSendSignup.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        signInBinding.button.setOnClickListener {
            val email = signInBinding.emailEt.text.toString()
            val pass = signInBinding.passET.text.toString()


            if (email.isNotEmpty() && pass.isNotEmpty()) {

                firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener {
                    if (it.isSuccessful) {
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    } else {
                        Log.d(Constants.tAG, "onCreate: ${it.exception}")
                        Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()

                    }
                }
            } else {
                Toast.makeText(this, "Empty Field not Allowed", Toast.LENGTH_SHORT).show()
            }

        }

    }
    override fun onStart() {
        super.onStart()
        if (firebaseAuth.currentUser != null){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}