package com.example.firebaseauthexampe.realtimedb.activitys

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.firebaseauthexampe.R
import com.example.firebaseauthexampe.databinding.ActivityFetchingBinding
import com.example.firebaseauthexampe.realtimedb.adapters.EmpAdapter
import com.example.firebaseauthexampe.realtimedb.model.Employee
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase

class FetchingActivity : AppCompatActivity() {

    private var _binding: ActivityFetchingBinding? = null
    private val binding get() = _binding!!
    private lateinit var dbReference: DatabaseReference


    private lateinit var empList: ArrayList<Employee>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = DataBindingUtil.setContentView(this, R.layout.activity_fetching)

        binding.rvEmployeeRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.rvEmployeeRecyclerView.setHasFixedSize(true)

        empList = arrayListOf()

        getEmployeeData()
    }

    private fun getEmployeeData() {
        /***/
        binding.rvEmployeeRecyclerView.visibility = View.GONE
        binding.tvLoadingData.visibility = View.VISIBLE

        dbReference = FirebaseDatabase.getInstance().getReference("Employees")

        dbReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                empList.clear()
                if (snapshot.exists()) {
                    for (empSnap in snapshot.children) {
                        val empData = empSnap.getValue(Employee::class.java)
                        empList.add(empData!!)
                    }
                    val mAdapter = EmpAdapter(empList)
                    binding.rvEmployeeRecyclerView.adapter = mAdapter


                    mAdapter.setOnItemClickListener(object : EmpAdapter.onItemClickListener {
                        override fun onItemClick(position: Int) {
                            val intent =
                                Intent(this@FetchingActivity, EmployeeDetailsActivity::class.java)
                            intent.putExtra("empId", empList[position].empId)
                            intent.putExtra("empName", empList[position].empName)
                            intent.putExtra("empAge", empList[position].empAge)
                            intent.putExtra("empSalary", empList[position].empSalary)
                            startActivity(intent)
                        }

                    })

                    binding.rvEmployeeRecyclerView.visibility = View.VISIBLE
                    binding.tvLoadingData.visibility = View.GONE
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }
}