package com.example.mvvm.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.mvvm.R
import android.widget.EditText

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        Log.d("NAV_DEBUG", "LoginActivity iniciada")

        val btnEntrar = findViewById<Button>(R.id.btnEntrar)
        val etUsuario = findViewById<EditText>(R.id.etUsuario)

        btnEntrar.setOnClickListener {
            val userId = etUsuario.text.toString().trim()
            Log.d("NAV_DEBUG", "Intentando entrar con usuario: $userId")

            val intent = Intent(this, DashboardActivity::class.java)
            intent.putExtra("USER_ID", userId)
            startActivity(intent)
            finish()
        }
    }
}
