package com.example.mvvm.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mvvm.model.DataRepository
import com.example.mvvm.model.SessionSummary
import com.example.mvvm.model.SesionHistorial

class MainViewModel : ViewModel() {

    val repository = DataRepository()

    private val _mensajeEstado = MutableLiveData<String>()
    val mensajeEstado: LiveData<String> get() = _mensajeEstado

    private val _resumenSesion = MutableLiveData<SessionSummary?>()
    val resumenSesion: LiveData<SessionSummary?> get() = _resumenSesion

    private val _setsCount = MutableLiveData<Int>(0)
    val setsCount: LiveData<Int> get() = _setsCount

    private val _historial = MutableLiveData<List<SesionHistorial>>(emptyList())
    val historial: LiveData<List<SesionHistorial>> get() = _historial

    fun validarYGuardar(ejercicio: String, pesoStr: String, repsStr: String) {
        val peso = pesoStr.toFloatOrNull()
        val reps = repsStr.toIntOrNull()

        if (peso != null && reps != null && peso > 0 && reps > 0 && ejercicio.isNotEmpty()) {
            val exito = repository.guardarSetDummy(ejercicio, peso, reps)
            if (exito) {
                _mensajeEstado.value = "Serie agregada: $ejercicio ($peso kg, $reps reps)"
                _setsCount.value = repository.obtenerSetsActuales().size
            }
        } else {
            _mensajeEstado.value = "Error: Ingresa datos válidos mayores a 0"
        }
    }

    fun finalizarEntrenamiento() {
        val sets = repository.obtenerSetsActuales()

        if (sets.isEmpty()) {
            _mensajeEstado.value = "Error: Debes agregar al menos una serie para finalizar"
            return
        }

        val resumen = repository.finalizarSesion()
        _resumenSesion.value = resumen
        _setsCount.value = 0

        // Actualizar historial
        _historial.value = repository.obtenerHistorial()

        var mensajeFinal = "¡Sesión Finalizada!\n" +
                "Volumen Total: ${resumen.volumenTotal} kg\n" +
                "XP Ganada: ${resumen.xpGanada}"

        if (resumen.huboPR) mensajeFinal += "\n¡NUEVO RÉCORD PERSONAL! 🏆"
        if (resumen.subioDeNivel) mensajeFinal += "\n¡EL AVATAR HA EVOLUCIONADO! ⭐"

        _mensajeEstado.value = mensajeFinal
    }
}