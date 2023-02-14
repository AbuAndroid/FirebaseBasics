package com.example.firebasebasics

import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.firebasebasics.databinding.ActivityLoginBinding
import com.example.firebasebasics.utils.FirebaseUtils.firebaseAuth
import com.google.android.gms.tasks.OnCompleteListener

class LoginActivity : AppCompatActivity() {

    lateinit var signInEmail : String
    lateinit var signInPassword : String
    lateinit var signInUser : ArrayList<EditText>

    private val binding : ActivityLoginBinding by lazy{
        ActivityLoginBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        signInUser = arrayListOf(binding.uiTvSignInUserEmail,binding.uiTvSignInUserPassword)

        setUpListeners()
    }

    private fun setUpListeners() {
        binding.uiTvSignIn.setOnClickListener {
            startActivity(Intent(this,RegisterActivity::class.java))
            finish()
        }
        binding.uiBtSigup.setOnClickListener {
            checkUserSignIn()
        }
        binding.uiTvForgetPassword.setOnClickListener {

            showRecoverPasswordDialog()
        }

    }

    private fun showRecoverPasswordDialog() {
        Log.e("test","clicking")
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle("Recover Password")
        val layout = LinearLayout(this)
        val emailId:EditText = EditText(this)

        emailId.hint = "Enter Your Email.."
        emailId.minEms = 16
        emailId.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
        layout.addView(emailId)
        layout.setPadding(10,10,10,10)

        alertDialogBuilder.setView(layout)

        alertDialogBuilder.setPositiveButton("Recover") { dialogInterface, i ->
            val recoverEmail = emailId.text.toString().trim()
            recoveryPassword(recoverEmail)
        }

        alertDialogBuilder.create().show()
    }

    private fun recoveryPassword(recoverEmail: String) {
        val loadingBar = ProgressDialog(this)
        loadingBar.setMessage("Sending Mail...")
        loadingBar.setCanceledOnTouchOutside(false)
        loadingBar.show()

        firebaseAuth.sendPasswordResetEmail(recoverEmail).addOnCompleteListener { task ->
            loadingBar.dismiss()
            if (task.isSuccessful) {
                Toast.makeText(this, "Check Your Gmail", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this, "Error Occured..", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener { task->
            loadingBar.dismiss()
            Toast.makeText(this,"Error Failed",Toast.LENGTH_SHORT).show()
        }
    }

    private fun isNotEmpty(): Boolean {
        return signInEmail.isNotEmpty() && signInPassword.isNotEmpty()
    }

    private fun checkUserSignIn() {
        val loadingBar = ProgressDialog(this)
        loadingBar.setMessage("Fetching User Details..")
        loadingBar.setCanceledOnTouchOutside(false)
        loadingBar.show()
        signInEmail = binding.uiTvSignInUserEmail.text.toString().trim()
        signInPassword = binding.uiTvSignInUserPassword.text.toString().trim()
        
        if(isNotEmpty()){
            firebaseAuth.signInWithEmailAndPassword(signInEmail,signInPassword)
                .addOnCompleteListener{signIn->
                    if(signIn.isSuccessful){
                        loadingBar.dismiss()
                        startActivity(Intent(this,HomeActivity::class.java))
                        Toast.makeText(this,"signed in successfully..",Toast.LENGTH_SHORT).show()
                        finish()
                    }else{
                        loadingBar.dismiss()
                        Toast.makeText(this,"sign In Failed..",Toast.LENGTH_SHORT).show()
                    }
                }
        }else{
            signInUser.forEach { input->
                if(input.text.toString().trim().isEmpty()){
                    input.error = "${input.hint} is required.."
                }
            }
        }
    }

}