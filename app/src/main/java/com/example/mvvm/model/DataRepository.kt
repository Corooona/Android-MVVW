package com.example.mvvm.model

data class SetLog(val ejercicio: String, val peso: Float, val reps: Int)

data class SessionSummary(
    val volumenTotal: Float,
    val xpGanada: Int,
    val huboPR: Boolean,
    val subioDeNivel: Boolean
)

class DataRepository {
    private val currentSessionSets = mutableListOf<SetLog>()

    // Simulando el guardado de un set en la sesión actual
    fun guardarSetDummy(ejercicio: String, peso: Float, reps: Int): Boolean {
        currentSessionSets.add(SetLog(ejercicio, peso, reps))
        return true
    }

    fun obtenerSetsActuales(): List<SetLog> = currentSessionSets

    fun finalizarSesion(): SessionSummary {
        val volumen = currentSessionSets.sumOf { (it.peso * it.reps).toDouble() }.toFloat()
        val xp = (volumen / 10).toInt()
        val huboPR = (1..10).random() > 7 // Simulación
        val subioDeNivel = (1..10).random() > 8 // Simulación
        
        currentSessionSets.clear()
        
        return SessionSummary(volumen, xp, huboPR, subioDeNivel)
    }
}