package com.example.mvvm.ui

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.mvvm.R
import com.example.mvvm.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Instanciar el ViewModel
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        // Referencias a la Vista XML
        val etEjercicio = findViewById<EditText>(R.id.etEjercicio)
        val etPeso = findViewById<EditText>(R.id.etPeso)
        val etReps = findViewById<EditText>(R.id.etReps)
        val btnGuardar = findViewById<Button>(R.id.btnGuardar)
        val tvMensaje = findViewById<TextView>(R.id.tvMensaje)

        // Observar los cambios de datos
        viewModel.mensajeEstado.observe(this) { mensaje ->
            tvMensaje.text = mensaje
        }

        // Notificar al ViewModel ante una interacción del usuario
        btnGuardar.setOnClickListener {
            viewModel.validarYGuardar(
                etEjercicio.text.toString(),
                etPeso.text.toString(),
                etReps.text.toString()
            )
        }
    }
}