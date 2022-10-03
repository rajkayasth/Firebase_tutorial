package com.example.firebaseauthexampe

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.firebaseauthexampe.databinding.ActivityHomeBinding
import com.example.firebaseauthexampe.realtimedb.RealtimeDbActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class HomeActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth


    private var _binding: ActivityHomeBinding? = null
    private val binding get() = _binding!!

    private val personalCollectionRef = Firebase.firestore.collection("persons")

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = DataBindingUtil.setContentView(this, R.layout.activity_home)

        auth = FirebaseAuth.getInstance()

        // subscribeToRealTimeUpdate()

        binding.cardSignOut.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this@HomeActivity, SignInActivity::class.java))
        }

        binding.btnGetData.setOnClickListener {
            retrievePerson()
        }

        binding.btnUpdateData.setOnClickListener {
            val oldPerson = getOldPerson()
            val newPersonMap = getNewPersonMap()
            updatePerson(oldPerson, newPersonMap)
        }

        binding.btnSaveData.setOnClickListener {
            val person = getOldPerson()
            savePerson(person)
        }

        binding.btnDeleteData.setOnClickListener {
            val person = getOldPerson()
            deletePerson(person)
        }

        binding.btnBatchWrite.setOnClickListener {
            changeName(
                "0Na6nc285tbZJequu2eI",
                "Elon"
            )
        }

        binding.btnRealTimeDb.setOnClickListener {
            startActivity(Intent(this@HomeActivity,RealtimeDbActivity::class.java))
        }

        binding.btnTransaction.setOnClickListener {
            startActivity(Intent(this@HomeActivity,UploadActivity::class.java))
        }

    }


    private fun getOldPerson(): Person {
        val email = binding.etEnterName.text.toString()
        val pass = binding.etEnterpassword.text.toString()
        return Person(email, pass)
    }

    private fun getNewPersonMap(): Map<String, Any> {
        val fistName = binding.etEnterNewName.text.toString()
        val password = binding.etEnterNewPassword.text.toString()
        val map = mutableMapOf<String, Any>()
        if (fistName.isNotEmpty()) {
            map["firstName"] = fistName
        }
        if (password.isNotEmpty()) {
            map["password"] = password
        }
        return map
    }


    private fun birthDay(personId: String) = CoroutineScope(Dispatchers.IO).launch {
        try {

            Firebase.firestore.runTransaction { transaction ->
                val personRef = personalCollectionRef.document(personId)

            }

        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(this@HomeActivity, e.message, Toast.LENGTH_SHORT)
                    .show()
            }

        }
    }


    private fun changeName(personId: String, newFirstName: String) =
        CoroutineScope(Dispatchers.IO).launch {

            try {
                Firebase.firestore.runBatch { batch ->
                    val personRef = personalCollectionRef.document(personId)
                    batch.update(personRef, "firstName", newFirstName)
                }.await()
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@HomeActivity, e.message, Toast.LENGTH_SHORT)
                        .show()
                }

            }

        }

    private fun savePerson(person: Person) = CoroutineScope(Dispatchers.IO).launch {
        try {
            personalCollectionRef.add(person).await()
            withContext(Dispatchers.Main) {
                Toast.makeText(this@HomeActivity, "Successfully Saved Data", Toast.LENGTH_LONG)
                    .show()
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(this@HomeActivity, e.message.toString(), Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun deletePerson(person: Person) =
        CoroutineScope(Dispatchers.IO).launch {
            val personQuery = personalCollectionRef
                .whereEqualTo("firstName", person.firstName)
                .whereEqualTo("password", person.password)
                .get()
                .await()

            if (personQuery.documents.isNotEmpty()) {
                for (documents in personQuery) {
                    try {
                        personalCollectionRef.document(documents.id).delete().await()

//                        personalCollectionRef.document(documents.id).update(mapOf(
//                            "firstName" to FieldValue.delete()
//                        ))

                    } catch (e: Exception) {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(this@HomeActivity, e.message, Toast.LENGTH_SHORT).show()
                            Log.d("TAG", "updatePerson: ${e.message}")
                        }
                    }
                }
            } else {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@HomeActivity, "No person matched query", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }

    private fun updatePerson(person: Person, newPersonMap: Map<String, Any>) =
        CoroutineScope(Dispatchers.IO).launch {
            val personQuery = personalCollectionRef
                .whereEqualTo("firstName", person.firstName)
                .whereEqualTo("password", person.password)
                .get()
                .await()

            if (personQuery.documents.isNotEmpty()) {
                for (documents in personQuery) {
                    try {
                        personalCollectionRef.document(documents.id)
                            .update("firstName", person.firstName)
                        personalCollectionRef.document(documents.id)
                            .set(newPersonMap, SetOptions.merge()).await()
                    } catch (e: Exception) {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(this@HomeActivity, e.message, Toast.LENGTH_SHORT).show()
                            Log.d("TAG", "updatePerson: ${e.message}")
                        }
                    }
                }
            } else {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@HomeActivity, "No person matched query", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }

    private fun retrievePerson() = CoroutineScope(Dispatchers.IO).launch {
        val name = binding.etEnterName.text?.trim().toString()
        try {
            val querySnapshot =
                personalCollectionRef.whereEqualTo(
                    "firstName",
                    name
                )
                    .get().await()
            val sb = StringBuilder()
            for (document in querySnapshot.documents) {
                val person = document.toObject<Person>()
                sb.append(
                    "$person\n"
                )
            }
            withContext(Dispatchers.Main) {
                binding.textViewEmail.text = sb.toString()
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(this@HomeActivity, e.message.toString(), Toast.LENGTH_LONG).show()
                Log.d("TAG", "retrievePerson: ${e.message}")
            }
        }
    }

    private fun subscribeToRealTimeUpdate() {
        personalCollectionRef.addSnapshotListener { querySnapshot, firebaseFireStoreExcaption ->
            firebaseFireStoreExcaption?.let {
                Toast.makeText(this@HomeActivity, it.message, Toast.LENGTH_LONG).show()
                return@addSnapshotListener
            }
            querySnapshot?.let {
                val sb = StringBuilder()
                for (document in querySnapshot.documents) {
                    val person = document.toObject<Person>()
                    sb.append(
                        "$person\n"
                    )

                }
                binding.textViewEmail.text = sb.toString()
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val a = Intent(Intent.ACTION_MAIN)
        a.addCategory(Intent.CATEGORY_HOME)
        a.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(a)
    }
}