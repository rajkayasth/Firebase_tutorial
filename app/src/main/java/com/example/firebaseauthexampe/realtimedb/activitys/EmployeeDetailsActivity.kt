package com.example.firebaseauthexampe.realtimedb.activitys

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import com.example.firebaseauthexampe.R
import com.example.firebaseauthexampe.databinding.ActivityEmployeeDetailsBinding
import com.example.firebaseauthexampe.realtimedb.model.Employee
import com.google.firebase.database.FirebaseDatabase

class EmployeeDetailsActivity : AppCompatActivity() {

    private var _binding: ActivityEmployeeDetailsBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = DataBindingUtil.setContentView(this, R.layout.activity_employee_details)
        setValuesToViews()
        binding.btnUpdate.setOnClickListener {
            openUpdateDialog(
                intent.getStringExtra("empId").toString(),
                intent.getStringExtra("empName").toString()
            )
        }
        binding.btnDelete.setOnClickListener {
            deleteRecord(
                intent.getStringExtra("empId").toString()
            )
        }
    }

    private fun deleteRecord(id: String) {
        val dbRef = FirebaseDatabase.getInstance().getReference("Employees").child(id)
        val mTask = dbRef.removeValue()
        mTask.addOnSuccessListener {
            Toast.makeText(this@EmployeeDetailsActivity, "Employee Deleted..", Toast.LENGTH_SHORT)
                .show()
            val intent = Intent(this, FetchingActivity::class.java)
            finish()
            startActivity(intent)
            mTask.addOnFailureListener { error ->
                Toast.makeText(
                    this@EmployeeDetailsActivity,
                    "Deleteing Error ${error.message}",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        }
    }

    private fun openUpdateDialog(
        empId: String,
        empName: String
    ) {
        val mDialog = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val mDialogView = inflater.inflate(R.layout.update_dialog, null)

        mDialog.setView(mDialogView)

        val etEmpName = mDialogView.findViewById<EditText>(R.id.etEmpNameUpdate)
        val etEmpAge = mDialogView.findViewById<EditText>(R.id.etEmpAgeUpdate)
        val etEmpSalary = mDialogView.findViewById<EditText>(R.id.etEmpSalaryUpdate)
        val btnUpdateData = mDialogView.findViewById<Button>(R.id.btnUpdateDataDialog)

        etEmpName.setText(intent.getStringExtra("empName").toString())
        etEmpAge.setText(intent.getStringExtra("empAge").toString())
        etEmpSalary.setText(intent.getStringExtra("empSalary").toString())

        mDialog.setTitle("Updating $empName Record")
        val alertDialog = mDialog.create()
        alertDialog.show()

        btnUpdateData.setOnClickListener {
            updateEmpData(
                empId,
                etEmpName.text.toString(),
                etEmpAge.text.toString(),
                etEmpSalary.text.toString()
            )
            Toast.makeText(
                this@EmployeeDetailsActivity,
                "Employee data Updated",
                Toast.LENGTH_SHORT
            )
                .show()

            /**Setting Updated data to textViews */
            binding.tvEmpName.text = etEmpName.text.toString()
            binding.tvEmpAge.text = etEmpAge.text.toString()
            binding.tvEmpSalary.text = etEmpSalary.text.toString()

            alertDialog.dismiss()
        }

    }

    private fun updateEmpData(
        id: String,
        name: String,
        age: String,
        salary: String
    ) {
        val dbRef = FirebaseDatabase.getInstance().getReference("Employees").child(id)
        val empInfo = Employee(id, name, age, salary)
        dbRef.setValue(empInfo)
    }

    private fun setValuesToViews() {
        binding.tvEmpId.text = intent.getStringExtra("empId")
        binding.tvEmpName.text = intent.getStringExtra("empName")
        binding.tvEmpAge.text = intent.getStringExtra("empAge")
        binding.tvEmpSalary.text = intent.getStringExtra("empSalary")
    }
}