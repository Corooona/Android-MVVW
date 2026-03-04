package com.example.mvvm.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.mvvm.R

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val btnEntrar = findViewById<Button>(R.id.btnEntrar)

        btnEntrar.setOnClickListener {
            // El "Intent" es el puente para saltar de una pantalla a otra
            val intent = Intent(this, DashboardActivity::class.java)
            startActivity(intent)
            finish() // Cierra el login para que el usuario no pueda regresar con el botón "Atrás"
        }
    }
}