package com.example.mvvm.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.mvvm.R

class DashboardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        // Recibir el userId que viene del Login
        val userId = intent.getStringExtra("USER_ID") ?: "Usuario"

        val btnNuevaSesion = findViewById<Button>(R.id.btnNuevaSesion)
        val btnVerProgreso = findViewById<Button>(R.id.btnVerProgreso)
        val btnEjercicios  = findViewById<Button>(R.id.btnEjercicios)

        btnNuevaSesion.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("USER_ID", userId)
            startActivity(intent)
        }

        btnVerProgreso.setOnClickListener {
            val intent = Intent(this, HistorialActivity::class.java)
            intent.putExtra("USER_ID", userId)
            startActivity(intent)
        }

        btnEjercicios.setOnClickListener {
            startActivity(Intent(this, ExercisesActivity::class.java))
        }
    }
    }