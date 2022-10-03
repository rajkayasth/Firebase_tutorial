package com.example.firebaseauthexampe

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.firebaseauthexampe.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class SignUpActivity : AppCompatActivity() {
    private var _signUpBinding: ActivitySignUpBinding? = null
    private val signUpBinding get() = _signUpBinding!!
    private lateinit var firebaseAuth: FirebaseAuth

    private val personalCollectionRef = Firebase.firestore.collection("persons")

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
                            Toast.makeText(
                                this@SignUpActivity,
                                it.exception.toString(),
                                Toast.LENGTH_SHORT
                            ).show()

                        }
                    }
                } else {
                    Toast.makeText(this@SignUpActivity, "PassWord Not Matched", Toast.LENGTH_SHORT)
                        .show()
                }
            } else {
                Toast.makeText(this@SignUpActivity, "Empty Field not Allowed", Toast.LENGTH_SHORT)
                    .show()
            }

            val person = Person(firstName = email, password = pass)
            savePerson(person)

        }

    }

    private fun savePerson(person: Person) = CoroutineScope(Dispatchers.IO).launch {
        try {
            personalCollectionRef.add(person).await()
            withContext(Dispatchers.Main) {
                Toast.makeText(this@SignUpActivity, "Successfully Saved Data", Toast.LENGTH_LONG).show()
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(this@SignUpActivity, e.message.toString(), Toast.LENGTH_LONG).show()
            }
        }
    }

}