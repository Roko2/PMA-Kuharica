package com.example.pma_kuharica

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class MainActivity : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val bottomNavigation= findViewById<BottomNavigationView>(R.id.bottom_navigation)
        val gumbOdjava = findViewById<Button>(R.id.btnOdjava)
        gumbOdjava.setOnClickListener { signOut() }

        bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.page_1 -> {
                    Toast.makeText(this, "1!", Toast.LENGTH_LONG).show()
                }
                R.id.page_2 -> {
                    Toast.makeText(this, "2!", Toast.LENGTH_LONG).show()
                }
                R.id.page_3 -> {
                    Toast.makeText(this, "3!", Toast.LENGTH_LONG).show()
                }
            }
            true
        }
    }
        private fun signOut() {
            Firebase.auth.signOut()
            GoogleSignIn.getClient(
                this.applicationContext,
                GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()
            ).signOut()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }