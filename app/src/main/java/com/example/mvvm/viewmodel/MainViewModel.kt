package com.example.mvvm.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mvvm.model.DataRepository
import com.example.mvvm.model.Exercise
import com.example.mvvm.model.SessionSummary
import com.example.mvvm.model.SesionHistorial
import com.example.mvvm.network.RetrofitInstance
import kotlinx.coroutines.launch

// Sella los tres estados posibles de una llamada a la API
sealed class ExercisesState {
    object Loading : ExercisesState()
    data class Success(val exercises: List<Exercise>) : ExercisesState()
    data class Error(val message: String) : ExercisesState()
}

class MainViewModel : ViewModel() {

    val repository = DataRepository()

    // --- Estado de la llamada a la API ---
    private val _exercisesState = MutableLiveData<ExercisesState>()
    val exercisesState: LiveData<ExercisesState> get() = _exercisesState

    fun fetchExercises() {
        _exercisesState.value = ExercisesState.Loading

        // viewModelScope: la corrutina vive mientras el ViewModel exista.
        // Si el usuario sale de la pantalla, se cancela sola — sin memory leaks.
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.getExercises()
                _exercisesState.value = ExercisesState.Success(response.results)
            } catch (e: Exception) {
                _exercisesState.value = ExercisesState.Error(e.message ?: "Error desconocido")
            }
        }
    }

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