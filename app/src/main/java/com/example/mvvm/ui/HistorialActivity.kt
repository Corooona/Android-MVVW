package com.example.mvvm.ui

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.mvvm.FitnessApp
import com.example.mvvm.R

class HistorialActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_historial)

        val userId = intent.getStringExtra("USER_ID") ?: "Usuario"

        val tvUserId = findViewById<TextView>(R.id.tvUserId)
        val lvHistorial = findViewById<ListView>(R.id.lvHistorial)
        val btnVolver = findViewById<Button>(R.id.btnVolver)

        tvUserId.text = "Usuario: $userId"

        // Obtener el ViewModel compartido
        val viewModel = (application as FitnessApp).mainViewModel

        // Observar el historial
        viewModel.historial.observe(this) { sesiones ->
            if (sesiones.isEmpty()) {
                val adapter = ArrayAdapter(
                    this,
                    android.R.layout.simple_list_item_1,
                    listOf("Aún no hay sesiones registradas")
                )
                lvHistorial.adapter = adapter
            } else {
                val items = sesiones.reversed().map { sesion ->
                    "${sesion.fecha} — ${sesion.ejercicios.size} ejercicios · ${sesion.volumenTotal.toInt()} kg · ${sesion.xpGanada} XP"
                }
                val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, items)
                lvHistorial.adapter = adapter
            }
        }

        btnVolver.setOnClickListener {
            finish()
        }
    }
}