package com.example.firebaseauthexampe.realtimedb.activitys

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.firebaseauthexampe.R
import com.example.firebaseauthexampe.databinding.ActivityInsertionBinding
import com.example.firebaseauthexampe.realtimedb.model.Employee
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class InsertionActivity : AppCompatActivity() {

    private var _binding: ActivityInsertionBinding? = null
    private val binding get() = _binding!!


    private lateinit var dbReference: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = DataBindingUtil.setContentView(this, R.layout.activity_insertion)


        dbReference = FirebaseDatabase.getInstance().getReference("Employees")

        binding.btnSave.setOnClickListener {
            saveEmployeeData()
        }

    }

    private fun saveEmployeeData() {
        /**Getting Values From Edittext*/

        val empName = binding.etEmpName.text.toString()
        val empAge = binding.etEmpAge.text.toString()
        val empSalary = binding.etEmpSalary.text.toString()

        if (empName.isEmpty()) {
            binding.etEmpName.error = "Please Enter EmployeeName"
        }
        if (empAge.isEmpty()) {
            binding.etEmpAge.error = "Please Enter Employee Age"
        }
        if (empSalary.isEmpty()) {
            binding.etEmpSalary.error = "Please Enter Employee Salary"
        }

        val empId = dbReference.push().key!!


        val employee = Employee(empId, empName, empAge, empSalary)

        dbReference.child(empId).setValue(employee).addOnCompleteListener {
            Toast.makeText(
                this@InsertionActivity,
                "Data Inserted Successfully ",
                Toast.LENGTH_SHORT
            ).show()
            binding.etEmpName.text.clear()
            binding.etEmpAge.text.clear()
            binding.etEmpSalary.text.clear()
            binding.etEmpName.requestFocus()
        }.addOnFailureListener { error ->
            Toast.makeText(this@InsertionActivity, error.message, Toast.LENGTH_SHORT).show()
            Log.d("TAG", "saveEmployeeData: ${error.message}")
        }
    }
}