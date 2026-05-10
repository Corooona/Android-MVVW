package com.example.mvvm.ui

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mvvm.R

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val etUsuario = findViewById<EditText>(R.id.etUsuario)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val etConfirmarPassword = findViewById<EditText>(R.id.etConfirmarPassword)
        val btnCrearCuenta = findViewById<Button>(R.id.btnCrearCuenta)
        val btnVolver = findViewById<Button>(R.id.btnVolver)

        btnCrearCuenta.setOnClickListener {
            val usuario = etUsuario.text.toString().trim()
            val password = etPassword.text.toString()
            val confirmar = etConfirmarPassword.text.toString()

            if (usuario.isEmpty() || password.isEmpty() || confirmar.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != confirmar) {
                Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val prefs = getSharedPreferences("usuarios", MODE_PRIVATE)

            if (prefs.contains(usuario)) {
                Toast.makeText(this, "Ese usuario ya existe", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            prefs.edit().putString(usuario, password).apply()
            Toast.makeText(this, "Cuenta creada. Ahora inicia sesión", Toast.LENGTH_SHORT).show()
            finish()
        }

        btnVolver.setOnClickListener {
            finish()
        }
    }
}
