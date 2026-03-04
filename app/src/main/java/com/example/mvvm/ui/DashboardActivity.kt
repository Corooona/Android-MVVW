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

        val btnNuevaSesion = findViewById<Button>(R.id.btnNuevaSesion)

        btnNuevaSesion.setOnClickListener {
            // Este botón nos lleva a la pantalla MVP que programamos anteriormente
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}