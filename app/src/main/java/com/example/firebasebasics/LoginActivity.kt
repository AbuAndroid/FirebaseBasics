package com.example.firebasebasics

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import com.example.firebasebasics.databinding.ActivityLoginBinding
import com.example.firebasebasics.utils.FirebaseUtils.firebaseAuth

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
    }
    private fun isNotEmpty(): Boolean {
        return signInEmail.isNotEmpty() && signInPassword.isNotEmpty()
    }
    private fun checkUserSignIn() {
        signInEmail = binding.uiTvSignInUserEmail.text.toString().trim()
        signInPassword = binding.uiTvSignInUserPassword.text.toString().trim()
        
        if(isNotEmpty()){
            firebaseAuth.signInWithEmailAndPassword(signInEmail,signInPassword)
                .addOnCompleteListener{signIn->
                    if(signIn.isSuccessful){
                        startActivity(Intent(this,HomeActivity::class.java))
                        Toast.makeText(this,"signed in successfully..",Toast.LENGTH_SHORT).show()
                        finish()
                    }else{
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