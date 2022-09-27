package com.example.firebaseauthexampe

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.firebaseauthexampe.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth

class SignUpActivity : AppCompatActivity() {
    private var _signUpBinding: ActivitySignUpBinding? = null
    private val signUpBinding get() = _signUpBinding!!
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _signUpBinding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up)


        firebaseAuth = FirebaseAuth.getInstance()


        signUpBinding.textView.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }

        signUpBinding.buttonAdd.setOnClickListener {
            val email = signUpBinding.emailEt.text.toString()
            val pass = signUpBinding.passET.text.toString()
            val confPass = signUpBinding.confirmPassEt.text.toString()

            if (email.isNotEmpty() && pass.isNotEmpty() && confPass.isNotEmpty()) {
                if (pass == confPass) {
                    firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener {
                        if (it.isSuccessful) {
                            val intent = Intent(this, SignInActivity::class.java)
                            startActivity(intent)
                        } else {
                            Log.d(Constants.tAG, "onCreate: ${it.exception}")
                            Toast.makeText(this@SignUpActivity, it.exception.toString(), Toast.LENGTH_SHORT).show()

                        }
                    }
                } else {
                    Toast.makeText(this@SignUpActivity, "PassWord Not Matched", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this@SignUpActivity, "Empty Field not Allowed", Toast.LENGTH_SHORT).show()
            }


        }

    }


}