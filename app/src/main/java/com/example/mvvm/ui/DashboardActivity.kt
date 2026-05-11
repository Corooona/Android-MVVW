package com.example.mvvm.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.mvvm.FitnessApp
import com.example.mvvm.R

class DashboardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        val userId = intent.getStringExtra("USER_ID") ?: "Usuario"
        val viewModel = (application as FitnessApp).mainViewModel
        viewModel.setCurrentUser(userId)

        findViewById<android.widget.TextView>(R.id.tvBienvenida).text = "¡Hola de nuevo, $userId!"

        val btnNuevaSesion = findViewById<Button>(R.id.btnNuevaSesion)
        val btnVerProgreso = findViewById<Button>(R.id.btnVerProgreso)

        btnNuevaSesion.setOnClickListener {
            val intent = Intent(this, TrainingActivity::class.java)
            intent.putExtra("USER_ID", userId)
            startActivity(intent)
        }

        btnVerProgreso.setOnClickListener {
            val intent = Intent(this, HistorialActivity::class.java)
            intent.putExtra("USER_ID", userId)
            startActivity(intent)
        }

        findViewById<Button>(R.id.btnCerrarSesion).setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }
}
