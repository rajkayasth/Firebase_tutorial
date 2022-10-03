package com.example.firebaseauthexampe.realtimedb.activitys

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.firebaseauthexampe.R
import com.example.firebaseauthexampe.databinding.ActivityEmployeeDetailsBinding

class EmployeeDetailsActivity : AppCompatActivity() {

    private var _binding: ActivityEmployeeDetailsBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = DataBindingUtil.setContentView(this, R.layout.activity_employee_details)
        setValuesToViews()
    }

    private fun setValuesToViews(){
        binding.tvEmpId.text = intent.getStringExtra("empId")
        binding.tvEmpName.text = intent.getStringExtra("empName")
        binding.tvEmpAge.text = intent.getStringExtra("empAge")
        binding.tvEmpSalary.text = intent.getStringExtra("empSalary")
    }
}