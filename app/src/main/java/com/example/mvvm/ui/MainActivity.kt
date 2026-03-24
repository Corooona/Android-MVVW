package com.example.mvvm.ui

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import android.content.Intent
import com.example.mvvm.FitnessApp
import com.example.mvvm.R
import com.example.mvvm.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Instanciar el ViewModel
        viewModel = (application as FitnessApp).mainViewModel

        // Referencias a la Vista XML
        val userId = intent.getStringExtra("USER_ID") ?: "Usuario"
        val etEjercicio = findViewById<EditText>(R.id.etEjercicio)
        val etPeso = findViewById<EditText>(R.id.etPeso)
        val etReps = findViewById<EditText>(R.id.etReps)
        val btnGuardar = findViewById<Button>(R.id.btnGuardar)
        val btnFinalizar = findViewById<Button>(R.id.btnFinalizar)
        val tvMensaje = findViewById<TextView>(R.id.tvMensaje)
        val tvSetsCount = findViewById<TextView>(R.id.tvSetsCount)

        // Observar los cambios de datos
        viewModel.mensajeEstado.observe(this) { mensaje ->
            tvMensaje.text = mensaje
        }

        viewModel.setsCount.observe(this) { count ->
            tvSetsCount.text = "Series en la sesión: $count"
        }

        // Notificar al ViewModel ante una interacción del usuario
        btnGuardar.setOnClickListener {
            viewModel.validarYGuardar(
                etEjercicio.text.toString(),
                etPeso.text.toString(),
                etReps.text.toString()
            )
            // Limpiar campos después de agregar (opcional, mejora UX)
            etPeso.text.clear()
            etReps.text.clear()
        }

        btnFinalizar.setOnClickListener {
            viewModel.finalizarEntrenamiento()

            // Regresar al Dashboard con el userId
            val intent = Intent(this, DashboardActivity::class.java)
            intent.putExtra("USER_ID", userId)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            finish()
        }
    }
}