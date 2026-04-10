package com.example.mvvm.model

import com.google.gson.annotations.SerializedName

data class ExerciseTranslation(
    val name: String,
    val description: String,
    val language: Int
)

data class ExerciseCategory(
    val id: Int,
    val name: String
)

// En /exerciseinfo/ los músculos y equipamiento son objetos, no IDs simples
data class Muscle(
    val id: Int,
    val name: String
)

data class Equipment(
    val id: Int,
    val name: String
)

data class Exercise(
    val id: Int,
    val category: ExerciseCategory,
    val muscles: List<Muscle>,
    @SerializedName("muscles_secondary") val musclesSecondary: List<Muscle>,
    val equipment: List<Equipment>,
    val translations: List<ExerciseTranslation>
)

data class ExerciseResponse(
    val count: Int,
    val results: List<Exercise>
)
