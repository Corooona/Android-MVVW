package com.example.mvvm.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mvvm.R

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        Log.d("NAV_DEBUG", "LoginActivity iniciada")

        val etUsuario = findViewById<EditText>(R.id.etUsuario)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val btnEntrar = findViewById<Button>(R.id.btnEntrar)
        val btnRegistrar = findViewById<Button>(R.id.btnRegistrar)

        btnEntrar.setOnClickListener {
            val usuario = etUsuario.text.toString().trim()
            val password = etPassword.text.toString()

            if (usuario.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val prefs = getSharedPreferences("usuarios", MODE_PRIVATE)
            val passwordGuardada = prefs.getString(usuario, null)

            if (passwordGuardada == null) {
                Toast.makeText(this, "Usuario no encontrado", Toast.LENGTH_SHORT).show()
            } else if (passwordGuardada != password) {
                Toast.makeText(this, "Contraseña incorrecta", Toast.LENGTH_SHORT).show()
            } else {
                Log.d("NAV_DEBUG", "Login exitoso: $usuario")
                val intent = Intent(this, DashboardActivity::class.java)
                intent.putExtra("USER_ID", usuario)
                startActivity(intent)
                finish()
            }
        }

        btnRegistrar.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}
