package com.example.firebasebasics

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import com.example.firebasebasics.databinding.ActivityRegisterBinding


import com.example.firebasebasics.utils.FirebaseUtils.firebaseAuth
import com.example.firebasebasics.utils.FirebaseUtils.firebaseUser
import com.google.firebase.auth.FirebaseUser

class RegisterActivity : AppCompatActivity() {

    private lateinit var userName:String
    private lateinit var userEmail:String
    private lateinit var userPassword:String
    private lateinit var userDetails : ArrayList<EditText>

    private val binding : ActivityRegisterBinding by lazy {
        ActivityRegisterBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        userDetails = arrayListOf(binding.uiTvSignInUserEmail,binding.uiTvConfirmPassword)
        setUpListeners()
    }

    override fun onStart() {
        super.onStart()
        val registeredUser: FirebaseUser? = firebaseAuth.currentUser
        registeredUser?.let {
            startActivity(Intent(this,HomeActivity::class.java))
            Toast.makeText(this,"welcome back",Toast.LENGTH_SHORT).show()
        }
    }

    private fun setUpListeners() {
        binding.uiBtSigup.setOnClickListener {
            createAccount()
        }

        binding.uiTvSignIn.setOnClickListener {
            startActivity(Intent(this,LoginActivity::class.java))
            Toast.makeText(this,"Please Sign into your Account",Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun checkValidInput(): Boolean {
        var valid = false
        if(isNotEmpty() && binding.uiTvUserPassword.text.toString().trim() == binding.uiTvConfirmPassword.text.toString().trim()){
            valid = true
        }else if(!isNotEmpty()){
            userDetails.forEach { input->
                if(input.text.toString().trim().isEmpty()){
                    input.error = "${input.hint} is required"
                }
            }
        }else{
            Toast.makeText(this,"Password are not matching !",Toast.LENGTH_LONG).show()
        }
        return valid
    }

    private fun isNotEmpty(): Boolean {
        return binding.uiTvSignInUserName.text.toString().trim().isNotEmpty() && binding.uiTvSignInUserEmail.text.toString().trim().isNotEmpty() && binding.uiTvConfirmPassword.text.toString().isNotEmpty()
    }

    private fun createAccount() {
        val loadingBar = ProgressDialog(this)
        loadingBar.setMessage("Creating Account..")
        loadingBar.setCanceledOnTouchOutside(false)
        loadingBar.show()
        if(checkValidInput()){
            userName = binding.uiTvSignInUserName.text.toString().trim()
            userEmail = binding.uiTvSignInUserEmail.text.toString().trim()
            userPassword = binding.uiTvConfirmPassword.text.toString().trim()

            firebaseAuth.createUserWithEmailAndPassword(userEmail,userPassword)
                .addOnCompleteListener{ task->
                    if(task.isSuccessful){
                        Toast.makeText(this,"created account successfully !",Toast.LENGTH_SHORT).show()
                        sendEmailVerification()
                        startActivity(Intent(this,HomeActivity::class.java))
                        finish()
                        loadingBar.dismiss()
                    }else{
                        Toast.makeText(this,"Failed To Authenticate...!",Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    private fun sendEmailVerification() {
        firebaseUser.let {user->
            user?.sendEmailVerification()?.addOnCompleteListener{ task->
                if(task.isSuccessful){
                    Toast.makeText(this,"email send to $userEmail",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}