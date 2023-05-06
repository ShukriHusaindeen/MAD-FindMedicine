package com.example.appointment.activities

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.example.appointment.R
import com.example.appointment.models.Model
import com.google.firebase.database.FirebaseDatabase

class EmployeeDetailsActivity : ComponentActivity() {


        private lateinit var tvEmpId: TextView
        private lateinit var tvEmpName: TextView
        private lateinit var tvEmpAge: TextView
        private lateinit var etDoctorName: TextView
        private lateinit var btnUpdate: Button
        private lateinit var btnDelete: Button


        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_employee_details)

            initView()
            setValuesToViews()

            btnUpdate.setOnClickListener {
                openUpdateDialog(
                    intent.getStringExtra("empId").toString(),
                    intent.getStringExtra("empName").toString()
                )
            }

            btnDelete.setOnClickListener {
                deleteRecord(
                    intent.getStringExtra("empId").toString()
                )
            }

        }

        private fun initView() {
            tvEmpId = findViewById(R.id.tvEmpId)
            tvEmpName = findViewById(R.id.tvEmpName)
            tvEmpAge = findViewById(R.id.tvEmpAge)
            etDoctorName = findViewById(R.id.etDoctorName)

            btnUpdate = findViewById(R.id.btnUpdate)
            btnDelete = findViewById(R.id.btnDelete)
        }

        private fun setValuesToViews() {
            tvEmpId.text = intent.getStringExtra("empId")
            tvEmpName.text = intent.getStringExtra("empName")
            tvEmpAge.text = intent.getStringExtra("empAge")
            etDoctorName.text = intent.getStringExtra("etDoctorName")

        }

        private fun deleteRecord(
            id: String
        ){
            val dbRef = FirebaseDatabase.getInstance().getReference("Employees").child(id)
            val mTask = dbRef.removeValue()

            mTask.addOnSuccessListener {
                Toast.makeText(this, "Employee data deleted", Toast.LENGTH_LONG).show()

                val intent = Intent(this, FetchingActivity::class.java)
                finish()
                startActivity(intent)
            }.addOnFailureListener{ error ->
                Toast.makeText(this, "Deleting Err ${error.message}", Toast.LENGTH_LONG).show()
            }
        }

        @SuppressLint("MissingInflatedId")
        private fun openUpdateDialog(
            empId: String,
            empName: String
        ) {
            val mDialog = AlertDialog.Builder(this)
            val inflater = layoutInflater
            val mDialogView = inflater.inflate(R.layout.update_dialog, null)

            mDialog.setView(mDialogView)

            val etEmpName = mDialogView.findViewById<EditText>(R.id.etEmpName)
            val etEmpAge = mDialogView.findViewById<EditText>(R.id.etEmpAge)
            val etDoctorName = mDialogView.findViewById<EditText>(R.id.etDoctorName)

            val btnUpdateData = mDialogView.findViewById<Button>(R.id.btnUpdateData)

            etEmpName.setText(intent.getStringExtra("empName").toString())
            etEmpAge.setText(intent.getStringExtra("empAge").toString())
            etDoctorName.setText(intent.getStringExtra("empSalary").toString())

            mDialog.setTitle("Updating $empName Record")

            val alertDialog = mDialog.create()
            alertDialog.show()

            btnUpdateData.setOnClickListener {
                updateEmpData(
                    empId,
                    etEmpName.text.toString(),
                    etEmpAge.text.toString(),
                    etDoctorName.text.toString()
                )

                Toast.makeText(applicationContext, "Employee Data Updated", Toast.LENGTH_LONG).show()

                //we are setting updated data to our textviews
                tvEmpName.text = etEmpName.text.toString()
                tvEmpAge.text = etEmpAge.text.toString()
                etDoctorName.setText(intent.getStringExtra("etDoctorName").toString())
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
            val empInfo = Model(id, name, age, salary)
            dbRef.setValue(empInfo)
        }

    }