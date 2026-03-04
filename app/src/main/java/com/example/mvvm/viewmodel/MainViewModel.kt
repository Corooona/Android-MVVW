package com.example.mvvm.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mvvm.model.DataRepository

class MainViewModel : ViewModel() {

    private val repository = DataRepository()
    val mensajeEstado = MutableLiveData<String>()

    fun validarYGuardar(ejercicio: String, pesoStr: String, repsStr: String) {
        val peso = pesoStr.toFloatOrNull()
        val reps = repsStr.toIntOrNull()

        if (peso != null && reps != null && peso > 0 && reps > 0 && ejercicio.isNotEmpty()) {
            val exito = repository.guardarSetDummy(ejercicio, peso, reps)
            if (exito) {
                mensajeEstado.value = "Progreso guardado: $ejercicio ($peso kg, $reps reps)"
            }
        } else {
            mensajeEstado.value = "Error: Ingresa datos válidos mayores a 0"
        }
    }
}