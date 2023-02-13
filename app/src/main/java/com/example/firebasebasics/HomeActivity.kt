package com.example.firebasebasics

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.firebasebasics.databinding.ActivityMainBinding
import com.example.firebasebasics.utils.FirebaseUtils.firebaseAuth

class HomeActivity : AppCompatActivity() {


    private val binding : ActivityMainBinding by lazy{
        ActivityMainBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.uiTvUserEmail.text = firebaseAuth.currentUser?.email.toString()
        setUpListerners()
    }

    private fun setUpListerners() {
        binding.uiBtSignOut.setOnClickListener {
            firebaseAuth.signOut()
            startActivity(Intent(this,LoginActivity::class.java))
            Toast.makeText(this,"signed out",Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}