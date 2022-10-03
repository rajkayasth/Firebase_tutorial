package com.example.firebaseauthexampe.realtimedb.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.firebaseauthexampe.R
import com.example.firebaseauthexampe.realtimedb.model.Employee

class EmpAdapter(private val empList: ArrayList<Employee>) :
    RecyclerView.Adapter<EmpAdapter.EmpViewHolder>() {

    private lateinit var mListener: onItemClickListener

    interface onItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(clickListener: onItemClickListener){
        mListener = clickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmpViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.emp_list_item,
            parent,
            false
        )
        return EmpViewHolder(view,mListener)
    }

    override fun onBindViewHolder(holder: EmpViewHolder, position: Int) {
        val currentEmp = empList[position]
        holder.tvEmployeeName.text = currentEmp.empName
    }

    override fun getItemCount(): Int {
        return empList.size
    }


    inner class EmpViewHolder(itemView: View,clickListener: onItemClickListener) : RecyclerView.ViewHolder(itemView) {
        val tvEmployeeName: TextView = itemView.findViewById(R.id.tvEmpName)

        init {
            itemView.setOnClickListener {
                clickListener.onItemClick(position = adapterPosition)
            }
        }
    }
}