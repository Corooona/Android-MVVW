package com.example.mvvm.model

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

data class SetLog(val ejercicio: String, val peso: Float, val reps: Int)

data class SessionSummary(
    val volumenTotal: Float,
    val xpGanada: Int,
    val huboPR: Boolean,
    val subioDeNivel: Boolean
)

data class SesionHistorial(
    val fecha: String,
    val ejercicios: List<SetLog>,
    val volumenTotal: Float,
    val xpGanada: Int
)

class DataRepository(context: Context) {
    private val prefs = context.getSharedPreferences("historial", Context.MODE_PRIVATE)
    private val gson = Gson()

    private val currentSessionSets = mutableListOf<SetLog>()
    private val historial: MutableList<SesionHistorial> = cargarHistorial()

    // Sistema de RPG derivado del historial persistido
    private var totalXP = historial.sumOf { it.xpGanada }
    private var nivel = (totalXP / 100) + 1

    private fun cargarHistorial(): MutableList<SesionHistorial> {
        val json = prefs.getString("sesiones", null) ?: return mutableListOf()
        val type = object : TypeToken<MutableList<SesionHistorial>>() {}.type
        return gson.fromJson(json, type)
    }

    private fun guardarHistorial() {
        prefs.edit().putString("sesiones", gson.toJson(historial)).apply()
    }

    fun guardarSetDummy(ejercicio: String, peso: Float, reps: Int): Boolean {
        currentSessionSets.add(SetLog(ejercicio, peso, reps))
        return true
    }

    fun obtenerSetsActuales(): List<SetLog> = currentSessionSets

    fun finalizarSesion(): SessionSummary {
        val volumen = currentSessionSets.sumOf { (it.peso * it.reps).toDouble() }.toFloat()
        val xp = (volumen / 10).toInt()

        totalXP += xp
        val nuevoNivel = (totalXP / 100) + 1
        val subioDeNivel = nuevoNivel > nivel
        nivel = nuevoNivel

        val huboPR = (1..10).random() > 7

        val fechaActual = java.text.SimpleDateFormat(
            "EEE dd MMM", java.util.Locale("es", "MX")
        ).format(java.util.Date())

        historial.add(
            SesionHistorial(
                fecha = fechaActual,
                ejercicios = currentSessionSets.toList(),
                volumenTotal = volumen,
                xpGanada = xp
            )
        )

        currentSessionSets.clear()
        guardarHistorial()
        Log.d("RPG_DEBUG", "XP Total: $totalXP | Nivel: $nivel")

        return SessionSummary(volumen, xp, huboPR, subioDeNivel)
    }

    fun obtenerHistorial(): List<SesionHistorial> = historial.toList()
    fun obtenerXP(): Int = totalXP
    fun obtenerNivel(): Int = nivel
}
