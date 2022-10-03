package com.example.firebaseauthexampe.realtimedb

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.firebaseauthexampe.R
import com.example.firebaseauthexampe.databinding.ActivityRealtimeDbBinding

class RealtimeDbActivity : AppCompatActivity() {



    private var _binding: ActivityRealtimeDbBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = DataBindingUtil.setContentView(this,R.layout.activity_realtime_db)

        binding.btnInsertData.setOnClickListener {
            startActivity(Intent(this,InsertionActivity::class.java))
        }

        binding.btnFetchData.setOnClickListener {

        }
    }
}